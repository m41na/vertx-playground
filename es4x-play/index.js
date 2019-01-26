import {Router} from '@vertx/web';

const app = Router.router(vertx);

app.route().handler(function(ctx){
  ctx.response().end('Hello from ES4X Vert.x Web!');
});

vertx.createHttpServer()
  .requestHandler(app)
  .listen(8080);
