package ex1.simonxikun.db

import com.twitter.inject.{ Injector, TwitterModule }
import java.net.InetSocketAddress
import com.twitter.finagle.exp.Mysql
import com.twitter.finagle.exp.mysql.{ Client, Transactions }
import com.google.inject.{ Singleton, Provides }
import com.twitter.util.Future

object MysqlModule extends TwitterModule {

  val username = flag("mysql-username", "ts1", "mysql database username")
  val password = flag("mysql-password", "111111", "mysql database password")
  val host = flag("mysql-dbserver", new InetSocketAddress("localhost", 3306), "mysql database server address")
  val dbname = flag("mysql-dbname", "alottodb", "mysql database name")

  type TClient = Client with Transactions

  override def singletonShutdown(injector: Injector) {
    val c = injector.instance(classOf[Client])
    c.close()
  }

  @Singleton
  @Provides
  def providesMysqlClient(): TClient = Mysql.client
    .withCredentials(username(), password())
    .withDatabase(dbname())
    .newRichClient(s"${host().getHostName}:${host().getPort()}")

}

object Main extends App {
  import com.twitter.util.Await
  import com.twitter.finagle.exp.mysql._
  val sql = "select * from lotto_players"
  val c = MysqlModule.providesMysqlClient()
  val result = c.select(sql) { row =>
    val StringValue(id) = row("id").get
    val nickname = row("nickname").collect {
      case StringValue(nickname) => nickname
    }
    (id, nickname)
  }

  val f = result.foreach { r =>
    r.foreach(r2 => println(r2))
  }.rescue {
    case e =>
      e.printStackTrace()
      Future.exception(e)
  }
  Await.ready(f)
}