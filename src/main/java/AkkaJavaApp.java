import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class AkkaJavaApp {
    public static void main(String[] args) {
        ActorSystem actorSystem = ActorSystem.create("Expedia-java-actor-system");

        ActorRef actorRef = actorSystem.actorOf(Props.create(FirstActorJava.class), "first-java-actor");

        actorRef.tell("Hello Expedia from Java app", ActorRef.noSender());

        actorSystem.terminate();
    }

    public static class FirstActorJava extends UntypedActor {
        LoggingAdapter logger = Logging.getLogger(getContext().system(), this);

        @Override
        public void onReceive(Object message) throws Exception {
            logger.info("Message received:\n\t" + message);
        }
    }
}
