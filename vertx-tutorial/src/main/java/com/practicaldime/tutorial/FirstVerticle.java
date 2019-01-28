package com.practicaldime.tutorial;

import io.vertx.config.ConfigRetriever;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;

public class FirstVerticle extends AbstractVerticle {

	public void start(Future<Void> fut) {
		ConfigRetriever retriever = ConfigRetriever.create(vertx);
		retriever.getConfig(config -> {
			if(config.failed()) {
				fut.fail(config.cause());
			}
			else {
				vertx.createHttpServer().requestHandler(r -> {
					r.response()
					.end("<h1>Hello Vertx Tutorial</h1>");
				}).listen(config().getInteger("http.port", 8080), result -> {
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
