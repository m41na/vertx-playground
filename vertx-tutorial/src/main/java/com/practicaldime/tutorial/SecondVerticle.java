package com.practicaldime.tutorial;

import io.vertx.config.ConfigRetriever;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;

public class SecondVerticle extends AbstractVerticle {

	public void start(Future<Void> fut) {
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
}
