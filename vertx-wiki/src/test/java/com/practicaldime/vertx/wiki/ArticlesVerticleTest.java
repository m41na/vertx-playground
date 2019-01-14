package com.practicaldime.vertx.wiki;

import java.io.IOException;
import java.net.ServerSocket;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.practicaldime.vertx.wiki.ArticlesVerticle;
import com.practicaldime.vertx.wiki.model.Article;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;

@RunWith(VertxUnitRunner.class)
public class ArticlesVerticleTest {

	private Vertx vertx;
	private int port;

	@Before
	public void setUp(TestContext context) throws IOException {
		vertx = Vertx.vertx();

		ServerSocket socket = new ServerSocket(0);
		port = socket.getLocalPort();
		socket.close();

		DeploymentOptions options = new DeploymentOptions()
		        .setConfig(new JsonObject()
		                .put("http.port", port)
		                .put("url", "jdbc:mysql://localhost/db_vertx_play?user=vertx&password=vertx")
		                .put("driver_class", "com.mysql.cj.jdbc.MysqlConnectionPoolDataSource")
		            );
		vertx.deployVerticle(ArticlesVerticle.class.getName(), options, context.asyncAssertSuccess());
	}

	@After
	public void tearDown(TestContext context) {
		vertx.close(context.asyncAssertSuccess());
	}

	@Test
	public void testArticlesVerticle(TestContext context) {
		final Async async = context.async();

		vertx.createHttpClient().getNow(port, "localhost", "/", response -> {
			response.handler(body -> {
				context.assertTrue(body.toString().contains("Hello"));
				async.complete();
			});
		});
	}
	
	@Test
	  public void checkThatTheIndexPageIsServed(TestContext context) {
	    Async async = context.async();
	    vertx.createHttpClient().getNow(port, "localhost", "/assets/index.html", response -> {
	      context.assertEquals(response.statusCode(), 200);
	      context.assertEquals(response.headers().get("content-type"), "text/html;charset=UTF-8");
	      response.bodyHandler(body -> {
	        context.assertTrue(body.toString().contains("<title>My Reading List</title>"));
	        async.complete();
	      });
	    });
	  }
	
	@Test
	  public void checkThatWeCanAdd(TestContext context) {
	    Async async = context.async();
	    final String json = Json.encodePrettily(new Article("To run or to jump", "http://lifeskills.org"));
	    vertx.createHttpClient().post(port, "localhost", "/api/articles")
	        .putHeader("content-type", "application/json")
	        .putHeader("content-length", Integer.toString(json.length()))
	        .handler(response -> {
	          context.assertEquals(response.statusCode(), 201);
	          context.assertTrue(response.headers().get("content-type").contains("application/json"));
	          response.bodyHandler(body -> {
	            final Article article = Json.decodeValue(body.toString(), Article.class);
	            context.assertEquals(article.getTitle(), "To run or to jump");
	            context.assertEquals(article.getUrl(), "http://lifeskills.org");
	            context.assertNotNull(article.getId());
	            async.complete();
	          });
	        })
	        .write(json)
	        .end();
	  }
}
