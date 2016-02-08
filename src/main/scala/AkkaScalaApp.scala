import akka.actor.ActorSystem

object AkkaScalaApp {
  def main(args: Array[String]) {
    val system = ActorSystem("Expedia-scala-actor-system")

    print(system)
  }
}
