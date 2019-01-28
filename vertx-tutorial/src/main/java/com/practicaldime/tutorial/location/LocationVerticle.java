package com.practicaldime.tutorial.location;

import static com.practicaldime.tutorial.ActionHelper.created;
import static com.practicaldime.tutorial.ActionHelper.noContent;
import static com.practicaldime.tutorial.ActionHelper.ok;

import java.util.HashSet;
import java.util.Set;

import com.practicaldime.tutorial.entity.Location;

import io.vertx.config.ConfigRetriever;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.ext.web.handler.StaticHandler;

public class LocationVerticle extends AbstractVerticle {

	private final Logger logger = LoggerFactory.getLogger(LocationVerticle.class);
	
	private static final int PORT = 8082;
	
	private LocationService service;
	
	public void start(Future<Void> fut) {
		
		//create router
		Router router = Router.router(vertx);
		
		//CORS support
		Set<String> allowHeaders = new HashSet<>();
		allowHeaders.add("x-request-with");
		allowHeaders.add("Access-Control-Allow-Origin");
		allowHeaders.add("origin");
		allowHeaders.add("Content-Type");
		allowHeaders.add("accept");
		Set<HttpMethod> allowMethods = new HashSet<>();
		allowMethods.add(HttpMethod.GET);
		allowMethods.add(HttpMethod.POST);
		allowMethods.add(HttpMethod.DELETE);
		allowMethods.add(HttpMethod.PUT);
		
		router.route().handler(BodyHandler.create());
		router.route().handler(CorsHandler.create("*")
				.allowedHeaders(allowHeaders)
				.allowedMethods(allowMethods));
		
		//bind root context handler
		router.route("/").handler(rc -> {
			rc.response()
			.putHeader("Content-Type", "text/html")
			.end("<h1>Hello Http Vertx!</h1>");
		});
		
		//configure static resources
		router.route("/assets/*").handler(StaticHandler.create("assets"));
		
		// add /api/* handlers
		router.get(Constants.API_GET_LOCATION).handler(this::getAll);
		router.post(Constants.API_CREATE_LOCAION).handler(this::addOne);
		router.get(Constants.API_GET_LOCATION).handler(this::getOne);
		router.delete(Constants.API_DELETE_LOCATION).handler(this::deleteOne);
		router.put(Constants.API_UPDATE_LOCATION).handler(this::updateOne);
		
		//fire up server and use router
		ConfigRetriever retriever = ConfigRetriever.create(vertx);
		ConfigRetriever.getConfigAsFuture(retriever)
			.compose(config -> {
				//create jdbc client
				service = new JdbcLocationService(vertx, config);
				return initData().compose(v -> createHttpServer(config, router));
			}).setHandler(fut);
	}
	
	private Future<Void> createHttpServer(JsonObject config, Router router){
		Future<Void> future = Future.future();
		vertx.createHttpServer()
		.requestHandler(router)
		.listen(
			config.getInteger("http.port", PORT), 
			res -> future.handle(res.mapEmpty()));
		return future;
	}
	
	private Future<Void> initData() {
		return service.initData().setHandler(res -> {
	      if (res.failed()) {
	        logger.error("Persistence service is not running!");
	        res.cause().printStackTrace();
	      }
	      else{
	    	  logger.info("Persistence service initialized");
	      }
	    });
	}
	
	public void getAll(RoutingContext ctx) {
		service.getAll().setHandler(ok(ctx));
	}
	
	public void addOne(RoutingContext ctx) {
		Location loc = ctx.getBodyAsJson().mapTo(Location.class);
		service.insert(loc).setHandler(created(ctx));
	}
	
	public void getOne(RoutingContext ctx) {
		String id = ctx.pathParam("id");
		service.getCertain(id).setHandler(ok(ctx));
	}
	
	public void deleteOne(RoutingContext ctx) {
		String id = ctx.pathParam("id");
		service.delete(id).setHandler(noContent(ctx));
	}
	
	public void updateOne(RoutingContext ctx) {
		String id = ctx.pathParam("id");
        Location loc = ctx.getBodyAsJson().mapTo(Location.class);
        service.update(id, loc)
        .setHandler(noContent(ctx));
	}
}
