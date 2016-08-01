package ex1.simonxikun

import com.twitter.finagle.http.Status._
import com.twitter.finatra.http.test.EmbeddedHttpServer
import com.twitter.inject.server.FeatureTest

class HelloWorldFeatureTest extends FeatureTest {
  override val server = new EmbeddedHttpServer(new BootstrapServer)

  "Server" should {
    "Say hi" in {
      server.httpGet(path = "/hi?name=Bob", andExpect = Ok, withBody = "hello Bob")
    }

    "Say hi for POST" in {
      server.httpPost(
        path = "/hi",
        postBody = """
         {
           "id":10,
           "name":"Sally"
         }
         """,
        andExpect = Ok,
        withBody = "hello Sally with id 10")
    }
  }

}