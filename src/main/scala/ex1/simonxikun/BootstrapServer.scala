package ex1.simonxikun

import com.twitter.finagle.http.{ Request, Response }
import com.twitter.finatra.http.HttpServer
import com.twitter.finatra.http.filters.{ CommonFilters, LoggingMDCFilter, TraceIdMDCFilter }
import com.twitter.finatra.http.routing.HttpRouter
import ex1.simonxikun.db.MysqlModule
import ex1.simonxikun.web.HelloWorldController
import ex1.simonxikun.web.ProcessingTimeFilter

class BootstrapServer extends HttpServer {

  override def modules = Seq(MysqlModule)

  override def configureHttp(router: HttpRouter) {
    router.filter[LoggingMDCFilter[Request, Response]]
      .filter[TraceIdMDCFilter[Request, Response]]
      .filter[CommonFilters]
      .filter[ProcessingTimeFilter[Request]]
      .add[HelloWorldController]
  }
}

object BootstrapServerMain extends BootstrapServer