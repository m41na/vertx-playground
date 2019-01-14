package com.practicaldime.vertx.bricks;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.mongo.FindOptions;
import io.vertx.ext.mongo.MongoClient;

public class BricksMongoVerticle extends AbstractVerticle {

	private static final Logger LOG = LoggerFactory.getLogger(BricksMongoVerticle.class);
	private static final int MONGO_PORT = 27017;

	@Override
	public void start(Future<Void> startFuture) throws Exception {
		LOG.info("{} starting", BricksMongoVerticle.class.getName());
		final EventBus eb = vertx.eventBus();
		final MongoClient mongo = MongoClient.createShared(vertx, 
			new JsonObject()
				.put("db_name", "db_bricks")
				.put("connection_string", "mongodb://localhost:" + MONGO_PORT), "Bricks");
		
		eb.consumer("bricks.storage.save", msg -> {
			mongo.insert("utx", (JsonObject) msg.body(), result -> {
				if(result.succeeded()) {
					LOG.info("SAVE OK");
					eb.publish("bricks.data.updates", msg.body());
				}
				else {
					LOG.error("SAVE FAIL", result.cause());
				}
			});
		});
		
		eb.consumer("bricks.storage.find", msg -> {
			FindOptions opts = new FindOptions((JsonObject) msg.body());
			mongo.findWithOptions("utx", new JsonObject(), opts, find -> {
				if(find.failed()) {
					LOG.error("FIND FAIL", find.cause().getMessage());
				}
				else {
					LOG.info("FIND OK");
					msg.reply(new JsonArray(find.result()));
				}
			});
		});
	}
}
