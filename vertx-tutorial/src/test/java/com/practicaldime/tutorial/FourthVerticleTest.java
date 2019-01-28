package com.practicaldime.tutorial;

import java.io.IOException;
import java.net.ServerSocket;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;

@RunWith(VertxUnitRunner.class)
public class FourthVerticleTest {

	private Vertx vertx;
	private int port = 8081;

	@Before
	public void setUp(TestContext context) throws IOException {
		vertx = Vertx.vertx();
		
		// Pick an available and random
		ServerSocket socket = new ServerSocket(0);
		port = socket.getLocalPort();
		socket.close();
		
		//add to options
		DeploymentOptions options = new DeploymentOptions()
			.setConfig(new JsonObject()
				.put("http.port", port)
				.put("url", "jdbc:h2:mem:test?shutdown=true")
		        .put("driver_class", "org.h2.Driver")
		        .put("user", "sa")
		        .put("password", "sa")
		);
		
		vertx.deployVerticle(FourthVerticle.class.getName(), options, context.asyncAssertSuccess());
	}

	@After
	public void tearDoen(TestContext context) {
		vertx.close();
	}

	@Test
	public void testFirstVerticle(TestContext context) {
		final Async async = context.async();

		vertx.createHttpClient().getNow(port, "localhost", "/", res -> {
			res.bodyHandler(body -> {
				context.assertTrue(body.toString().contains("Hello"));
				async.complete();
			});
		});
	}
}
