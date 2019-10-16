package com.bestore.vertxstudy.db

import com.bestore.vertxstudy.db.repo.ProductRepository
import io.vertx.core.AbstractVerticle
import io.vertx.core.Context
import io.vertx.core.Vertx
import io.vertx.kotlin.sqlclient.poolOptionsOf
import io.vertx.mysqlclient.MySQLConnectOptions
import io.vertx.mysqlclient.MySQLPool
import io.vertx.serviceproxy.ServiceBinder
import org.slf4j.LoggerFactory
import java.util.*

class DatabaseVerticle : AbstractVerticle() {
  private val log = LoggerFactory.getLogger(DatabaseVerticle::class.java)
  private lateinit var client: MySQLPool

  override fun init(vertx: Vertx?, context: Context?) {
    super.init(vertx, context)
    val datasourceConf = Objects.requireNonNull(config().getJsonObject("dataSource"))
    log.info("data source config:{}", datasourceConf)
    val mySQLConnectOptions = MySQLConnectOptions(datasourceConf)
    val poolOptions = poolOptionsOf(maxSize = Runtime.getRuntime().availableProcessors())
    client = MySQLPool.pool(vertx, mySQLConnectOptions, poolOptions)
  }

  override fun start() {
    super.start()
    val serviceBinder = ServiceBinder(vertx)
    serviceBinder.setAddress(ProductRepository::class.java.simpleName)
      .register(ProductRepository::class.java, ProductRepository.create(client))
  }
}
