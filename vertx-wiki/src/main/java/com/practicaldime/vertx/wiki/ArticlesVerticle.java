package com.practicaldime.vertx.wiki;

import java.util.List;
import java.util.stream.Collectors;

import com.practicaldime.vertx.wiki.model.Article;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.SQLConnection;
import io.vertx.ext.sql.UpdateResult;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;

public class ArticlesVerticle extends AbstractVerticle {

	private JDBCClient jdbc;

	@Override
	public void start(Future<Void> fut) {
		// Create a JDBC client
		jdbc = JDBCClient.createShared(vertx, config(), "My Reading List");

		startBackend((connection) -> createSomeData(connection,
				(nothing) -> startWebApp((http) -> completeStartup(http, fut)), fut), fut);
	}

	private void startBackend(Handler<AsyncResult<SQLConnection>> next, Future<Void> fut) {
		jdbc.getConnection(ar -> {
			if (ar.failed()) {
				fut.fail(ar.cause());
			} else {
				next.handle(Future.succeededFuture(ar.result()));
			}
		});
	}

	private void startWebApp(Handler<AsyncResult<HttpServer>> next) {

		// Create a router object.
		Router router = Router.router(vertx);

		// Bind "/" to our hello message - so we are still compatible.
		router.route("/").handler(routingContext -> {
			HttpServerResponse response = routingContext.response();
			response.putHeader("content-type", "text/html").end("<h1>Hello from my first Vert.x 3 application</h1>");
		});
		// Serve static resources from the /assets directory
		router.route("/assets/*").handler(StaticHandler.create("assets"));
		router.get("/api/articles").handler(this::getAll);
		router.get("/api/articles/:id").handler(this::getOne);
		router.route("/api/articles*").handler(BodyHandler.create());
		router.post("/api/articles").handler(this::addOne);
		router.delete("/api/articles/:id").handler(this::deleteOne);
		router.put("/api/articles/:id").handler(this::updateOne);

		// Create the HTTP server and pass the "accept" method to the request handler.
		vertx.createHttpServer().requestHandler(router).listen(config().getInteger("http.port", 8080), next::handle);
	}

	private void completeStartup(AsyncResult<HttpServer> http, Future<Void> fut) {
		if (http.succeeded()) {
			fut.complete();
		} else {
			fut.fail(http.cause());
		}
	}

	@Override
	public void stop() throws Exception {
		// Close the JDBC client.
		jdbc.close();
	}

	private void createSomeData(AsyncResult<SQLConnection> result, Handler<AsyncResult<Void>> next, Future<Void> fut) {
		if (result.failed()) {
			fut.fail(result.cause());
		} else {
			SQLConnection connection = result.result();
			connection.execute(
					"CREATE TABLE IF NOT EXISTS Articles (id INTEGER AUTO_INCREMENT PRIMARY KEY, title varchar(100), url varchar(100))",
					ar -> {
						if (ar.failed()) {
							fut.fail(ar.cause());
							connection.close();
							return;
						}
						connection.query("SELECT * FROM Articles", select -> {
							if (select.failed()) {
								fut.fail(ar.cause());
								connection.close();
								return;
							}
							if (select.result().getNumRows() == 0) {
								insert(new Article("Fallacies of distributed computing",
										"https://en.wikipedia.org/wiki/Fallacies_of_distributed_computing"), connection,
										(v) -> insert(
												new Article("Reactive Manifesto", "https://www.reactivemanifesto.org/"),
												connection, (r) -> {
													next.handle(Future.<Void>succeededFuture());
													connection.close();
												}));
							} else {
								next.handle(Future.<Void>succeededFuture());
								connection.close();
							}
						});
					});
		}
	}

	private void addOne(RoutingContext routingContext) {
		jdbc.getConnection(ar -> {
			// Read the request's content and create an instance of Article.
			final Article whisky = Json.decodeValue(routingContext.getBodyAsString(), Article.class);
			SQLConnection connection = ar.result();
			insert(whisky, connection, (r) -> routingContext.response().setStatusCode(201)
					.putHeader("content-type", "application/json; charset=utf-8").end(Json.encodePrettily(r.result())));
			connection.close();
		});
	}

	private void getOne(RoutingContext routingContext) {
		final String id = routingContext.request().getParam("id");
		if (id == null) {
			routingContext.response().setStatusCode(400).end();
		} else {
			jdbc.getConnection(ar -> {
				// Read the request's content and create an instance of Article.
				SQLConnection connection = ar.result();
				select(id, connection, result -> {
					if (result.succeeded()) {
						routingContext.response().setStatusCode(200)
								.putHeader("content-type", "application/json; charset=utf-8")
								.end(Json.encodePrettily(result.result()));
					} else {
						routingContext.response().setStatusCode(404).end();
					}
					connection.close();
				});
			});
		}
	}

	private void updateOne(RoutingContext routingContext) {
		final String id = routingContext.request().getParam("id");
		JsonObject json = routingContext.getBodyAsJson();
		if (id == null || json == null) {
			routingContext.response().setStatusCode(400).end();
		} else {
			jdbc.getConnection(ar -> update(id, json, ar.result(), (article) -> {
				if (article.failed()) {
					routingContext.response().setStatusCode(404).end();
				} else {
					routingContext.response().putHeader("content-type", "application/json; charset=utf-8")
							.end(Json.encodePrettily(article.result()));
				}
				ar.result().close();
			}));
		}
	}

	private void deleteOne(RoutingContext routingContext) {
		String id = routingContext.request().getParam("id");
		if (id == null) {
			routingContext.response().setStatusCode(400).end();
		} else {
			jdbc.getConnection(ar -> {
				SQLConnection connection = ar.result();
				connection.execute("DELETE FROM Articles WHERE id='" + id + "'", result -> {
					routingContext.response().setStatusCode(204).end();
					connection.close();
				});
			});
		}
	}

	private void getAll(RoutingContext routingContext) {
		jdbc.getConnection(ar -> {
			SQLConnection connection = ar.result();
			connection.query("SELECT * FROM Articles", result -> {
				List<Article> articles = result.result().getRows().stream().map(Article::new)
						.collect(Collectors.toList());
				routingContext.response().putHeader("content-type", "application/json; charset=utf-8")
						.end(Json.encodePrettily(articles));
				connection.close();
			});
		});
	}

	private void insert(Article article, SQLConnection connection, Handler<AsyncResult<Article>> next) {
		String sql = "INSERT INTO Articles (title, url) VALUES (?, ?)";
		connection.updateWithParams(sql, new JsonArray().add(article.getTitle()).add(article.getUrl()), (ar) -> {
			if (ar.failed()) {
				next.handle(Future.failedFuture(ar.cause()));
				connection.close();
				return;
			}
			UpdateResult result = ar.result();
			// Build a new article instance with the generated id.
			Article w = new Article(result.getKeys().getLong(0), article.getTitle(), article.getUrl());
			next.handle(Future.succeededFuture(w));
		});
	}

	private void select(String id, SQLConnection connection, Handler<AsyncResult<Article>> resultHandler) {
		connection.queryWithParams("SELECT * FROM Articles WHERE id=?", new JsonArray().add(id), ar -> {
			if (ar.failed()) {
				resultHandler.handle(Future.failedFuture("Article not found"));
			} else {
				if (ar.result().getNumRows() >= 1) {
					resultHandler.handle(Future.succeededFuture(new Article(ar.result().getRows().get(0))));
				} else {
					resultHandler.handle(Future.failedFuture("Article not found"));
				}
			}
		});
	}

	private void update(String id, JsonObject content, SQLConnection connection, Handler<AsyncResult<Article>> resultHandler) {
		String sql = "UPDATE Articles SET title=?, url=? WHERE id=?";
		connection.updateWithParams(sql,
				new JsonArray().add(content.getString("title")).add(content.getString("url")).add(id), update -> {
					if (update.failed()) {
						resultHandler.handle(Future.failedFuture("Cannot update the article"));
						return;
					}
					if (update.result().getUpdated() == 0) {
						resultHandler.handle(Future.failedFuture("Article not found"));
						return;
					}
					resultHandler.handle(Future.succeededFuture(
							new Article(Long.valueOf(id), content.getString("title"), content.getString("url"))));
				});
	}
}
