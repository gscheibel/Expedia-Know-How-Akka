import java.util.concurrent.TimeUnit

import akka.actor.{ActorSystem, Props}
import akka.pattern.ask
import akka.testkit.{TestKit, TestProbe}
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
      val guessProbe = TestProbe()

      val actorRef = system.actorOf(Props(new FirstActorScala {
        override val guessActor = guessProbe.ref
      }))

      val question = "Question: This is a test question"
      val future: Future[String] = (actorRef ? question).mapTo[String]

      guessProbe.expectMsg(question)
      guessProbe.reply("42")

      whenReady(future) { response =>
        response should be("42")
      }
    }

    it("must be able to add 2 numbers") {
      val fas = system.actorOf(Props[FirstActorScala])
      system.actorOf(Props(classOf[MathActor], 42), "mathActor")

      val futureMagicAdd = (fas ? Add(1,1)).mapTo[Result]

      whenReady(futureMagicAdd) {
        result => result.value should be(44)
      }
    }
  }
}
