package ex1.simonxikun

import com.twitter.finatra.http.Controller
import com.twitter.finagle.http.Request

case class HiRequest(id: Long, name: String)

class HelloWorldController extends Controller {
  get("/hi") { req: Request =>
    info("hi")
    s"hello ${req.params.getOrElse("name", "unknown")}"
  }

  post("/hi") { req: HiRequest =>
    s"hello ${req.name} with id ${req.id}"
  }
}