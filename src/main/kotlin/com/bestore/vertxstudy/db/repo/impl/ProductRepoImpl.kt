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
import org.slf4j.LoggerFactory

class ProductRepoImpl(private var client: MySQLPool) : ProductRepository {
    private val log = LoggerFactory.getLogger(ProductRepoImpl::class.java)

    override fun findById(id: Int, handler: Handler<AsyncResult<JsonObject>>): ProductRepository {
        GlobalScope.launch {
            val conn = client.getConnectionAwait()
            val rowSet = conn.preparedQueryAwait("select * from product where ID = ?", Tuple.of(2))
            conn.close()
            val row = if (rowSet.size() == 0) throw RuntimeException("product not exist,id:$id") else rowSet.first()
            val resp = JsonObject()
            for (i in 0 until row.size()) resp.put(row.getColumnName(i), row.getValue(i))
            log.info("<ProductRepository>-<findById> resp:$resp")
            handler.handle(Future.succeededFuture(resp))
        }.invokeOnCompletion { if (it != null) handler.handle(Future.failedFuture(it)) }
        return this
    }
}
