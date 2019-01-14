package com.practicaldime.vertx.bricks;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.bridge.PermittedOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.handler.sockjs.BridgeOptions;
import io.vertx.ext.web.handler.sockjs.SockJSHandler;

public class BricksServiceVerticle extends AbstractVerticle {

	private static final Logger LOG = LoggerFactory.getLogger(BricksServiceVerticle.class);

	@Override
	public void start(Future<Void> startFuture) throws Exception {
		LOG.info("{} starting", BricksServiceVerticle.class.getName());
		Router router = Router.router(vertx);

        router.route("/eventbus/*").handler(eventBusHandler());
        router.route().handler(staticHandler());

        vertx.createHttpServer()
                .requestHandler(router)
                .listen(8080);
	}
	
	private SockJSHandler eventBusHandler() {
        BridgeOptions options = new BridgeOptions()
                .addOutboundPermitted(new PermittedOptions().setAddressRegex("out"))
                .addInboundPermitted(new PermittedOptions().setAddressRegex("in"));

        EventBus eventBus = vertx.eventBus();

        SockJSHandler sockJSHandler = SockJSHandler.create(vertx);
        return sockJSHandler.bridge(options, new BricksClientHandler(eventBus));
    }

    private StaticHandler staticHandler() {
        return StaticHandler.create()
                .setCachingEnabled(false);
    }
}
