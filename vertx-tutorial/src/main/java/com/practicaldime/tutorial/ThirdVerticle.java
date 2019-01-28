package com.practicaldime.tutorial;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import com.practicaldime.tutorial.entity.Location;

import io.vertx.config.ConfigRetriever;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;

public class ThirdVerticle extends AbstractVerticle {

	private Map<Long, Location> data = new HashMap<>();
	private AtomicLong ids = new AtomicLong(0);
	
	public void start(Future<Void> fut) {
		
		createSomeData();
		
		//create router
		Router router = Router.router(vertx);
		
		//bind handlers to router
		router.route("/").handler(rc -> {
			rc.response()
			.putHeader("Content-Type", "text/html")
			.end("<h1>Hello Http Vertx!</h1>");
		});
		
		//configure static resources
		router.route("/assets/*").handler(StaticHandler.create("assets"));
		
		// add /api/* handlers
		router.get("/api/location").handler(this::getAll);
		router.route("/api/location*").handler(BodyHandler.create());
		router.post("/api/location").handler(this::addOne);
		router.get("/api/location/:id").handler(this::getOne);
		router.delete("/api/location/:id").handler(this::deleteOne);
		router.put("/api/location/:id").handler(this::updateOne);
		
		//fire up server and use router
		ConfigRetriever retriever = ConfigRetriever.create(vertx);
		retriever.getConfig(config -> {
			if(config.failed()) {
				fut.fail(config.cause());
			}
			else {
				vertx.createHttpServer()
				.requestHandler(router)
				.listen(config().getInteger("http.port", 8080), result -> {
					if (result.succeeded()) {
						fut.complete();
					} else {
						fut.fail(result.cause());
					}
				});
			}
		});
	}

	private void createSomeData() {
		Location chicago = new Location(ids.incrementAndGet(), "chicago", "IL", "60455", "US");
		data.put(chicago.getId(), chicago);
		Location madison = new Location(ids.incrementAndGet(), "madison", "WI", "53718", "US");
		data.put(madison.getId(), madison);
		Location seattle = new Location(ids.incrementAndGet(), "seattle", "WA", "98030", "US");
		data.put(seattle.getId(), seattle);
		Location houston = new Location(ids.incrementAndGet(), "houston", "TX", "75499", "US");
		data.put(houston.getId(), houston);
		Location nairobi = new Location(ids.incrementAndGet(), "nairobi", "KE", "00100", "KE");
		data.put(nairobi.getId(), nairobi);
	}
	
	public void getAll(RoutingContext ctx) {
		ctx.response()
		.putHeader("Content-Type", "application/json")
		.end(Json.encodePrettily(data.values()));
	}
	
	public void addOne(RoutingContext ctx) {
		Location loc = ctx.getBodyAsJson().mapTo(Location.class);
		Long id = ids.incrementAndGet();
		loc.setId(id);
		data.put(id, loc);
		ctx.response().setStatusCode(201)
		.putHeader("Content-Type", "application/json;charset=utf-8")
		.end(Json.encodePrettily(loc));
	}
	
	public void getOne(RoutingContext ctx) {
		String id = ctx.request().getParam("id");
		try {
			Long longId = Long.valueOf(id);
			Location loc = data.get(longId);
			ctx.response().setStatusCode(200).end(Json.encode(loc));
		}
		catch(NumberFormatException nfe) {
			ctx.response().setStatusCode(400).end();
		}
	}
	
	public void deleteOne(RoutingContext ctx) {
		String id = ctx.request().getParam("id");
		try {
			Long longId = Long.valueOf(id);
			data.remove(longId);
			ctx.response().setStatusCode(204).end();
		}
		catch(NumberFormatException nfe) {
			ctx.response().setStatusCode(400).end();
		}
	}
	
	public void updateOne(RoutingContext ctx) {
		String id = ctx.request().getParam("id");
        try {
            Long longId = Long.valueOf(id);
            Location loc = data.get(longId);
            if (loc == null) {
                // Not found
                ctx.response().setStatusCode(404).end();
            } else {
                JsonObject body = ctx.getBodyAsJson();
                loc.setCity(body.getString("city"));
                loc.setState(body.getString("state"));
                data.put(longId, loc);
                ctx.response()
                    .setStatusCode(200)
                    .putHeader("content-type", "application/json; charset=utf-8")
                    .end(Json.encodePrettily(loc));
            }
        } catch (NumberFormatException e) {
            ctx.response().setStatusCode(400).end();
        }
	}
}
