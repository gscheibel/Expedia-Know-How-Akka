import akka.actor.AbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.pf.ReceiveBuilder;

public class LambdaActor extends AbstractActor {
    LoggingAdapter logger = Logging.getLogger(getContext().system(), this);

    public LambdaActor() {
        receive(ReceiveBuilder
                .match(String.class, logger::info)
                .match(Integer.class, i -> i % 2 == 0, i -> sender().tell(i * i, this.self()))
                .build());
    }
}
