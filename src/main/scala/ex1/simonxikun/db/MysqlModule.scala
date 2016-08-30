package ex1.simonxikun.db

import com.twitter.inject.{ Injector, TwitterModule }
import java.net.InetSocketAddress
import com.twitter.finagle.exp.Mysql
import com.twitter.finagle.exp.mysql.{ Parameter, Row, Client, Transactions, StringValue, PreparedStatement }
import com.google.inject.{ Singleton, Provides }
import com.twitter.util.Future
import scalaz._
import Scalaz._

object MysqlModule extends TwitterModule {

  val username = flag("mysql-username", "ts1", "mysql database username")
  val password = flag("mysql-password", "111111", "mysql database password")
  val host = flag("mysql-dbserver", new InetSocketAddress("localhost", 3306), "mysql database server address")
  val dbname = flag("mysql-dbname", "alottodb", "mysql database name")

  override def singletonShutdown(injector: Injector) {
    val c = injector.instance(classOf[Client])
    c.close()
  }

  @Singleton
  @Provides
  def providesMysqlClient(): TrxClient = Mysql.client
    .withCredentials(username(), password())
    .withDatabase(dbname())
    .newRichClient(s"${host().getHostName}:${host().getPort()}")

}

object Users {

  def findCurrency(id: String): Reader[TrxClient, Future[Option[String]]] = Reader((client: TrxClient) => {
    val sql = client.prepare("select currency from lotto_players where id = ?")
    selectOne(sql, id) { row =>
      val StringValue(currency) = row("currency").get
      currency
    }
  })

  def selectOne[A](sql: PreparedStatement, params: Parameter*)(f: Row => A): Future[Option[A]] = {
    sql.select(params: _*)(f).flatMap {
      case Nil        => Future.value(None)
      case Seq(value) => Future.value(Some(value))
      case _          => Future.exception(new IllegalStateException("multiple values found"))
    }
  }
}

object Main extends App {
  import com.twitter.util.Await
  val x: Future[Option[String]] = Users.findCurrency("simon")(MysqlModule.providesMysqlClient())
  val f = Await.ready(x.map(println))
}