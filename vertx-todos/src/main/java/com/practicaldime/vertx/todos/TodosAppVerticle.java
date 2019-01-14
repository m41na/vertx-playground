package com.practicaldime.vertx.todos;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.practicaldime.vertx.todos.entity.Todo;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.DecodeException;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;

public class TodosAppVerticle extends AbstractVerticle {

	private static final Logger LOG = LoggerFactory.getLogger(TodosAppVerticle.class);

	private static final String HTTP_HOST = "0.0.0.0";
	private static final String MONGO_HOST = "127.0.0.1";
	private static final int HTTP_PORT = 8082;
	private static final int MONGO_PORT = 27017;

	private MongoClient mongo;

	@Override
	public void start(Future<Void> future) throws Exception {
		LOG.info("{} starting", TodosAppVerticle.class.getName());

		initData();

		Router router = Router.router(vertx);
		// CORS support
		Set<String> allowHeaders = new HashSet<>();
		allowHeaders.add("x-requested-with");
		allowHeaders.add("Access-Control-Allow-Origin");
		allowHeaders.add("origin");
		allowHeaders.add("Content-Type");
		allowHeaders.add("accept");

		Set<HttpMethod> allowMethods = new HashSet<>();
		allowMethods.add(HttpMethod.GET);
		allowMethods.add(HttpMethod.POST);
		allowMethods.add(HttpMethod.DELETE);
		allowMethods.add(HttpMethod.PATCH);

		router.route().handler(CorsHandler.create("*")
				.allowedHeaders(allowHeaders).allowedMethods(allowMethods));
		router.route().handler(BodyHandler.create());

		// routes
		router.get(Constants.API_GET).handler(this::handleGetTodo);
		router.get(Constants.API_LIST_ALL).handler(this::handleGetAll);
		router.post(Constants.API_CREATE).handler(this::handleCreateTodo);
		router.patch(Constants.API_UPDATE).handler(this::handleUpdateTodo);
		router.delete(Constants.API_DELETE).handler(this::handleDeleteOne);
		router.delete(Constants.API_DELETE_ALL).handler(this::handleDeleteAll);

		// start server
		vertx.createHttpServer() // <4>
				.requestHandler(router).listen(HTTP_PORT, HTTP_HOST, result -> {
					if (result.succeeded())
						future.complete();
					else
						future.fail(result.cause());
				});
	}

	private void initData() {
		mongo = MongoClient.createShared(vertx, new JsonObject()
				.put("db_name", "db_todos")
				.put("connection_string",
				"mongodb://" + MONGO_HOST + ":" + MONGO_PORT), "Todos");
		mongo.insert("db_todos",
				new JsonObject().put("title", "Sample").put("completed", false),
				insert -> {
					if (insert.succeeded()) {
						LOG.info("INSERT OK");
					} else {
						LOG.error("SAVE FAIL", insert.cause());
					}
				});
	}

	private void handleGetTodo(RoutingContext context) {
		String todoID = context.request().getParam("todoId");
		if (todoID == null)
			sendError(400, context.response());
		else {
			mongo.findOne("db_todos", new JsonObject().put("_id", todoID), new JsonObject(), find -> {
				if (find.failed()) {
					sendError(503, context.response());
				} else {
					JsonObject result = find.result();
					if (result == null)
						sendError(404, context.response());
					else {
						context.response().putHeader("content-type", "application/json")
								.end(Json.encodePrettily(result));
					}
				}
			});
		}
	}

	private void handleGetAll(RoutingContext context) {
		mongo.find("db_todos", new JsonObject(), find -> {
			if (find.succeeded()) {
				String encoded = Json
						.encodePrettily(find.result().stream().map(obj -> new Todo(obj)).collect(Collectors.toList()));
				context.response().putHeader("content-type", "application/json").end(encoded);
			} else {
				sendError(503, context.response());
			}
		});
	}

	private void handleCreateTodo(RoutingContext context) {
		try {
			final JsonObject todo = context.getBodyAsJson();
			mongo.insert("db_todos", todo, insert -> {
				if (insert.succeeded())
					context.response().setStatusCode(201).putHeader("content-type", "application/json; charset=utf-8")
							.end(insert.result());
				else
					sendError(503, context.response());
			});
		} catch (DecodeException e) {
			sendError(400, context.response());
		}
	}

	private void handleUpdateTodo(RoutingContext context) {
		try {
			String todoID = context.request().getParam("todoId");
			final JsonObject newTodo = context.getBodyAsJson();
			// handle error
			if (todoID == null || newTodo == null) {
				sendError(400, context.response());
				return;
			}

			mongo.updateCollection("db_todos", new JsonObject().put("id", todoID), newTodo, update -> {
				if (update.succeeded()) {
					JsonObject result = update.result().getDocUpsertedId();
					if (result == null)
						sendError(404, context.response());
					else {
						context.response().putHeader("content-type", "application/json")
								.end(Json.encodePrettily(result));
					}
				} else {
					sendError(503, context.response());
				}
			});
		} catch (DecodeException e) {
			sendError(400, context.response());
		}
	}

	private void handleDeleteOne(RoutingContext context) {
		String todoID = context.request().getParam("todoId");
		mongo.removeDocument("db_todos", new JsonObject().put("id", todoID), res -> {
			if (res.succeeded())
				context.response().setStatusCode(204).end();
			else
				sendError(503, context.response());
		});
	}

	private void handleDeleteAll(RoutingContext context) {
		mongo.removeDocuments("db_todos", new JsonObject(), res -> {
			if (res.succeeded())
				context.response().setStatusCode(204).end();
			else
				sendError(503, context.response());
		});
	}

	private void sendError(int statusCode, HttpServerResponse response) {
		response.setStatusCode(statusCode).end();
	}
}
