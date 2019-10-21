package com.bestore.vertxstudy.web

import com.bestore.vertxstudy.db.repo.PosterScanRecordRepo
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
        val sharingKey = req.getParam("sharing_key")
        log.info("receive req,resp hello world")
        val prodRepo = PosterScanRecordRepo.createProxy(vertx)
        prodRepo.findById(sharingKey.toLong()) {
          if (it.succeeded()) {
            req.response()
              .putHeader("content-type", "application/json")
              .end(it.result().toBuffer())
          } else {
            req.response()
              .putHeader("content-type", "application/json")
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
