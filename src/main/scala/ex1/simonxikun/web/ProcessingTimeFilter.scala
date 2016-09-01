package ex1.simonxikun.web

import com.twitter.util.{ Future, Stopwatch }
import javax.inject.Singleton
import com.twitter.finagle.http.{ Request, Response }
import com.twitter.finagle.{ Service, SimpleFilter }

@Singleton
class ProcessingTimeFilter[R <: Request] extends SimpleFilter[R, Response] {
  override def apply(request: R, service: Service[R, Response]): Future[Response] = {
    val elapsed = Stopwatch.start()
    service(request).map { response =>
      val time = elapsed().toString()
      response.headerMap.set("processing-time", time)
      response
    }
  }
}