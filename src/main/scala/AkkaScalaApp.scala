import java.util.concurrent.TimeUnit

import akka.actor._
import akka.event.Logging
import akka.pattern.Patterns._
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.ExecutionContextExecutor
import scala.util.{Failure, Success}

class FirstActorScala extends Actor {
  val guessActor = context.actorOf(Props[GuessActor], "guessActor")
  implicit val defaultTimeout: Timeout = Timeout(200, TimeUnit.MILLISECONDS)
  implicit val executionContext: ExecutionContextExecutor = context.dispatcher

  private val logger = Logging.getLogger(context.system, this)

  override def receive: Receive = {
    case question: String if question.startsWith("Question:") =>
      logger.info(s"Received a question:\n\t$question")

      val futureAnswer = (guessActor ? question).mapTo[String]
      val origin = sender()

      futureAnswer.map(guessedAnswer =>
        origin ! guessedAnswer
      )
  }
}

class GuessActor extends Actor {
  private val logger = Logging.getLogger(context.system, this)

  override def receive: Receive = {
    case msg: String =>
      logger.info("Let me think about it...")
      Thread.sleep(200)
      sender ! "42"
  }
}

object AkkaScalaApp {
  def main(args: Array[String]) {
    val system: ActorSystem = ActorSystem("Expedia-scala-actor-system")
    val logger = Logging.getLogger(system, this)

    implicit val defaultTimeout: Timeout = Timeout(500, TimeUnit.MILLISECONDS)
    implicit val executionContext: ExecutionContextExecutor = system.dispatcher

    val actorRef: ActorRef = system.actorOf(Props[FirstActorScala], "first-scala-actor")

    val possibleAnswer = actorRef ? "Question: What is the answer to the Ultimate Question of Life, The Universe, and Everything?"

    possibleAnswer.onComplete {
      case Success(answer) => logger.info(s"The answer is '$answer'")
      case Failure(error) => logger.error(error, "No answer has been found")
    }

    system.terminate()
  }
}