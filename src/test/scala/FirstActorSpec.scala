import akka.actor.ActorSystem
import akka.testkit.TestKit
import org.scalatest.{BeforeAndAfterAll, FunSpecLike, Matchers}

class FirstActorSpec(_system: ActorSystem) extends TestKit(_system: ActorSystem) with Matchers with FunSpecLike with BeforeAndAfterAll {

  def this() = this(ActorSystem("testActorSystem"))

  override protected def afterAll(): Unit = {
    TestKit.shutdownActorSystem(system)
    super.afterAll()
  }
}
