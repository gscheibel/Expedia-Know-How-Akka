import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.pattern.Patterns;
import akka.util.Timeout;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;

public class AkkaJavaApp {
    public static void main(String[] args) throws Exception {
        ActorSystem actorSystem = ActorSystem.create("Expedia-java-actor-system");
        LoggingAdapter logger = Logging.getLogger(actorSystem, AkkaJavaApp.class);

        ActorRef actorRef = actorSystem.actorOf(Props.create(FirstActorJava.class), "first-java-actor");

        actorRef.tell("Hello Expedia from Java app", ActorRef.noSender());
        Timeout timeout = new Timeout(Duration.create(500, TimeUnit.MILLISECONDS));

        Future<Object> potentialAnswer = Patterns.ask(actorRef, "Question: What is the answer to the Ultimate Question of Life, The Universe, and Everything?", timeout);

        Object result = Await.result(potentialAnswer, timeout.duration());

        if(result != null && result instanceof String) {
            String answer = (String) result;
            logger.info("The answer is '" + answer+"'");
        }

        actorSystem.terminate();
    }

    public static class FirstActorJava extends UntypedActor {
        LoggingAdapter logger = Logging.getLogger(getContext().system(), this);

        @Override
        public void onReceive(Object message) throws Exception {
            if( message instanceof String) {
                String msg = (String) message;

                if(msg.startsWith("Question:")) {
                    sender().tell("42", this.getSelf());
                }else {
                    logger.info("Message received:\n\t" + message);
                }
            }
        }
    }
}
