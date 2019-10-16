package com.bestore.vertxstudy.db.repo.impl

import com.bestore.vertxstudy.db.repo.ProductRepository
import io.vertx.core.AsyncResult
import io.vertx.core.Future
import io.vertx.core.Handler
import io.vertx.core.json.JsonObject
import io.vertx.kotlin.sqlclient.getConnectionAwait
import io.vertx.kotlin.sqlclient.preparedQueryAwait
import io.vertx.mysqlclient.MySQLPool
import io.vertx.sqlclient.Tuple
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ProductRepoImpl(private var client: MySQLPool) : ProductRepository {

  override fun findByid(id: Int, handler: Handler<AsyncResult<JsonObject>>): ProductRepository {
    GlobalScope.launch {
      val conn = client.getConnectionAwait()
      val rowSet = conn.preparedQueryAwait("select * from product where ID = ?", Tuple.of(2))
      conn.close()
      handler.handle(Future.succeededFuture(JsonObject.mapFrom(rowSet)))
    }.invokeOnCompletion { if (it != null) handler.handle(Future.failedFuture(it)) }
    return this
  }
}
