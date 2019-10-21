package com.bestore.vertxstudy.db.repo;

import com.bestore.vertxstudy.db.repo.impl.PosterScanRecordRepoImpl;
import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.mysqlclient.MySQLPool;

/**
 * @author Sapphire
 * @since 2019/10/17 21:38
 **/
@VertxGen
@ProxyGen
public interface PosterScanRecordRepo {

  @Fluent
  PosterScanRecordRepo findById(Long sharingKey, Handler<AsyncResult<JsonObject>> handler);

  static PosterScanRecordRepo create(MySQLPool client) {
    return new PosterScanRecordRepoImpl(client);
  }

  static PosterScanRecordRepo createProxy(Vertx vertx) {
    return new PosterScanRecordRepoVertxEBProxy(vertx, PosterScanRecordRepo.class.getSimpleName());
  }
}
