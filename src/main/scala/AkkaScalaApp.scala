import akka.actor.{ActorRef, Props, Actor, ActorSystem}
import akka.event.Logging

class FirstActorScala extends Actor {
  private val logger = Logging.getLogger(context.system, this)

  override def receive: Receive = {
    case message: String => logger.info(s"Message received:\n\t$message")
  }
}

object AkkaScalaApp {
  def main(args: Array[String]) {
    val system: ActorSystem = ActorSystem("Expedia-scala-actor-system")
    val actorRef: ActorRef = system.actorOf(Props[FirstActorScala], "first-scala-actor")

    actorRef ! "Hello Expedia from Scala app"

    system.terminate()
  }
}