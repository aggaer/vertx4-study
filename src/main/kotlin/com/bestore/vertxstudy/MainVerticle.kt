package com.bestore.vertxstudy

import com.bestore.vertxstudy.db.DatabaseVerticle
import com.bestore.vertxstudy.web.WebVerticle
import io.vertx.config.ConfigRetriever
import io.vertx.config.ConfigRetrieverOptions
import io.vertx.config.ConfigStoreOptions
import io.vertx.core.AbstractVerticle
import io.vertx.core.Context
import io.vertx.core.Promise
import io.vertx.core.Vertx
import io.vertx.core.json.JsonObject
import io.vertx.core.spi.resolver.ResolverProvider.DISABLE_DNS_RESOLVER_PROP_NAME
import io.vertx.kotlin.config.getConfigAwait
import io.vertx.kotlin.core.deployVerticleAwait
import io.vertx.kotlin.core.deploymentOptionsOf
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainVerticle : AbstractVerticle() {

  override fun init(vertx: Vertx, context: Context) {
    System.setProperty(DISABLE_DNS_RESOLVER_PROP_NAME, "true")
    super.init(vertx, context)
  }

  override fun start(startPromise: Promise<Void>) {
    //加载配置
    GlobalScope.launch {
      println("load main verticle")
      val config = loadConfigs().getConfigAwait()
      val deploymentOptions = deploymentOptionsOf(config)
      vertx.deployVerticleAwait(DatabaseVerticle(), deploymentOptions)
      vertx.deployVerticleAwait(WebVerticle(), deploymentOptions)
    }.invokeOnCompletion {
      if (it != null) startPromise.fail(it) else startPromise.complete()
    }
  }

  private fun loadConfigs(): ConfigRetriever {
    val activeEnv = System.getProperty("env", "dev")
    val baseStore = ConfigStoreOptions()
      .setType("file")
      .setFormat("yaml")
      .setConfig(JsonObject().put("path", "application.yml"))
    val devStore = ConfigStoreOptions()
      .setType("file")
      .setFormat("yaml")
      .setConfig(JsonObject().put("path", String.format("application-%s.yml", activeEnv)))
    return ConfigRetriever.create(vertx, ConfigRetrieverOptions().addStore(baseStore).addStore(devStore))
  }
}

fun main() {
  val vertx = Vertx.vertx()
  vertx.deployVerticle(MainVerticle())
}
