package com.bestore.vertxstudy.db.repo;

import com.bestore.vertxstudy.db.repo.impl.ProductRepoImpl;
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
 * @since 2019/10/16 19:32
 **/
@VertxGen
@ProxyGen
public interface ProductRepository {

  /**
   * 通过商品id查询商品信息
   *
   * @param id 商品id
   * @return 商品信息
   */
  @Fluent
  ProductRepository findByid(Integer id, Handler<AsyncResult<JsonObject>> handler);

  static ProductRepository create(MySQLPool client) {
    return new ProductRepoImpl(client);
  }

  static ProductRepository createProxy(Vertx vertx) {
    return new ProductRepositoryVertxEBProxy(vertx, ProductRepository.class.getSimpleName());
  }
}
