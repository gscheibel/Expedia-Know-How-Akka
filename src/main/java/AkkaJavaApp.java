import akka.actor.ActorSystem;

public class AkkaJavaApp {
    public static void main(String[] args) {
        ActorSystem actorSystem = ActorSystem.create("Expedia-java-actor-system");

        System.out.println(actorSystem);
    }
}
