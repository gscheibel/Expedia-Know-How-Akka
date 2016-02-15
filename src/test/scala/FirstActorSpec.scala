import java.util.concurrent.TimeUnit

import akka.actor.{ActorSystem, Props}
import akka.pattern.ask
import akka.testkit.TestKit
import akka.util.Timeout
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{BeforeAndAfterAll, FunSpecLike, Matchers}

import scala.concurrent.{ExecutionContextExecutor, Future}

class FirstActorSpec(_system: ActorSystem) extends TestKit(_system: ActorSystem) with Matchers with FunSpecLike with BeforeAndAfterAll with ScalaFutures {

  implicit val defaultTimeout: Timeout = Timeout(500, TimeUnit.MILLISECONDS)
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher

  def this() = this(ActorSystem("testActorSystem"))

  override protected def afterAll(): Unit = {
    TestKit.shutdownActorSystem(system)
    super.afterAll()
  }

  describe("An actor") {
    it("must answer `42` when a question is asked") {
      val actorRef = system.actorOf(Props[FirstActorScala])

      val future: Future[String] = (actorRef ? "Question: This is a test question").mapTo[String]

      whenReady(future) { response =>
        response should be("42")
      }
    }
  }
}
