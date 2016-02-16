import java.util.concurrent.TimeUnit

import akka.actor.ActorSystem
import akka.event.Logging
import akka.pattern.ask
import akka.util.Timeout
import com.typesafe.config.ConfigFactory

import scala.concurrent.ExecutionContextExecutor
import scala.util.{Failure, Success}

object RemoteApp {

  def main(args: Array[String]) {
    val config = ConfigFactory.parseString("""akka.remote.netty.tcp.port="2553"""").withFallback(ConfigFactory.load())

    val system: ActorSystem = ActorSystem("remote-app", config)
    val logger = Logging.getLogger(system, this)

    implicit val defaultTimeout: Timeout = Timeout(500, TimeUnit.MILLISECONDS)
    implicit val executionContext: ExecutionContextExecutor = system.dispatcher

    val remoteActor = system.actorSelection("akka.tcp://Expedia-scala-actor-system@127.0.0.1:2552/user/first-scala-actor")

    val possibleAnswer = remoteActor ? "Question: What is the answer to the Ultimate Question of Life, The Universe, and Everything?"

    possibleAnswer.onComplete {
      case Success(answer) =>
        logger.info(s"The answer is '$answer'")
        system.terminate

      case Failure(error) =>
        logger.error(error, "No answer has been found")
        system.terminate
    }
  }

}
