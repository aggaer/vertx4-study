package com.bestore.vertxstudy.web

import com.bestore.vertxstudy.db.repo.ProductRepository
import io.vertx.core.AbstractVerticle
import org.slf4j.LoggerFactory
import java.util.*

class WebVerticle : AbstractVerticle() {
  private val log = LoggerFactory.getLogger(WebVerticle::class.java)

  override fun start() {
    super.start()
    val serverConfig = Objects.requireNonNull(config().getJsonObject("server"))
    val port = serverConfig.getInteger("port")
    vertx.createHttpServer()
      .requestHandler { req ->
        log.info("receive req,resp hello world")
        val prodRepo = ProductRepository.createProxy(vertx)
        prodRepo.findByid(2) {
          if (it.succeeded()) {
            req.response()
              .putHeader("content-type", "text/plain")
              .end(it.result().toBuffer())
          } else {
            req.response()
              .putHeader("content-type", "text/plain")
              .end(it.cause().message)
          }
        }
      }.listen(port) { http ->
        if (http.succeeded()) {
          log.info("HTTP server started on port:{}", port)
        } else {
          log.error("HTTP server started failed,err:${http.cause().message}")
        }
      }
  }
}
