package com.practicaldime.vertx.bricks;

import io.vertx.core.Handler;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.bridge.BridgeEventType;
import io.vertx.ext.web.handler.sockjs.BridgeEvent;

public class BricksClientHandler implements Handler<BridgeEvent>{

	private static final Logger logger = LoggerFactory.getLogger(BricksClientHandler.class);
    private final EventBus eventBus;

    public BricksClientHandler(EventBus eventBus) {
        this.eventBus = eventBus;
        logger.info("{} created", BricksClientHandler.class.getName());
    }

    @Override
    public void handle(BridgeEvent event) {
        if (event.type() == BridgeEventType.SOCKET_CREATED)
        	eventBus.publish("utx", "{\"op\":\"unconfirmed_sub\"}");

        if (event.type() == BridgeEventType.UNREGISTER)
        	eventBus.publish("utx", "{\"op\":\"unconfirmed_unsub\"}");
        
        if (event.type() == BridgeEventType.RECEIVE)
        	eventBus.publish("bricks.storage.save", event.getRawMessage());

        event.complete(true);
    }
}
