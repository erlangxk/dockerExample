package ex1.simonxikun.web

import ex1.simonxikun.db.{ TrxClient, Users }
import com.twitter.finatra.http.Controller
import com.twitter.finagle.http.Request
import javax.inject.{ Inject, Singleton }
import scalaz.Kleisli.kleisliFn

case class HiRequest(id: Long, name: String)
case class CurrencyRequest(name: String)

@Singleton
class HelloWorldController @Inject() (val mysql: TrxClient) extends Controller {
  get("/hi") { req: Request =>
    info("hi")
    s"hello ${req.params.getOrElse("name", "unknown")}"
  }

  post("/hi") { req: HiRequest =>
    s"hello ${req.name} with id ${req.id}"
  }

  post("/currency") { req: CurrencyRequest =>
    Users.findCurrency(req.name)(mysql).map { r =>
      response.ok(s"currency of user:${req.name} is ${r}")
    }
  }
}