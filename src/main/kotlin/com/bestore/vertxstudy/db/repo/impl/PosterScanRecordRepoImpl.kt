package com.bestore.vertxstudy.db.repo.impl

import com.bestore.vertxstudy.db.repo.PosterScanRecordRepo
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

class PosterScanRecordRepoImpl(private val client: MySQLPool) : PosterScanRecordRepo {
  private val log = LoggerFactory.getLogger(PosterScanRecordRepoImpl::class.java)

  override fun findById(sharingKey: Long, handler: Handler<AsyncResult<JsonObject>>): PosterScanRecordRepo {
    GlobalScope.launch {
      val conn = client.getConnectionAwait()
      val rowSet =
        conn.preparedQueryAwait("select * from pt_poster_scan_record where sharing_key = ?", Tuple.of(sharingKey))
      conn.close()
      val row =
        if (rowSet.size() == 0) throw RuntimeException("pt_poster_scan_record not exist,sharingKey:$sharingKey") else rowSet.first()
      val resp = JsonObject()
      for (i in 0 until 10) resp.put(row.getColumnName(i), row.getValue(i))
      log.info("<pt_poster_scan_record>-<findById> resp:$resp")
      handler.handle(Future.succeededFuture(resp))
    }.invokeOnCompletion { if (it != null) handler.handle(Future.failedFuture(it)) }
    return this
  }
}
