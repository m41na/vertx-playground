package com.practicaldime.vertx.wiki;

import com.practicaldime.vertx.wiki.dao.WikiDatabaseVerticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

public class MainVerticle extends AbstractVerticle {

	private static final Logger LOGGER = LoggerFactory.getLogger(MainVerticle.class);

	@Override
	public void start(Future<Void> startFuture) throws Exception {
		LOGGER.info("MainVerticle starting");
		Future<String> dbVerticleDeployment = Future.future();
		vertx.deployVerticle(new WikiDatabaseVerticle(), dbVerticleDeployment.completer());

		dbVerticleDeployment.compose(id -> {

			Future<String> httpVerticleDeployment = Future.future();
			vertx.deployVerticle("com.practicaldime.vertx.wiki.http.HttpServerVerticle",
					new DeploymentOptions().setInstances(2), httpVerticleDeployment.completer());

			return httpVerticleDeployment;

		}).setHandler(ar -> {
			if (ar.succeeded()) {
				startFuture.complete();
			} else {
				startFuture.fail(ar.cause());
			}
		});
	}
}
