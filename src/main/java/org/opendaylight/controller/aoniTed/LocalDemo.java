package org.opendaylight.controller.aoniTed;

/**
 * Created by root on 11/15/17.
 */

import akka.actor.ActorSelection;
import akka.actor.UntypedActor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class LocalDemo {
    public static class LocalActor extends UntypedActor {
        private static final Logger LOG = LoggerFactory.getLogger(LocalActor.class);

        final String path = "akka.tcp://bundle-602-ActorSystem@10.108.50.48:3349/user/RemoteActor";
        final ActorSelection remote = getContext().actorSelection(path);
        private int count = 0;

        @Override
        public void onReceive(Object message) throws Exception {
            if (message == "START") {
                LOG.info("LocalActor received message : " + message );
                remote.tell("Hello from the LocalActor", self());
            } else if (message instanceof String) {
                if(count < 5) {
                    System.out.println("LocalActor received message : " + message);
                    LOG.info("LocalActor received message : " + message);
                    sender().tell("Hello back to you, I am Local", self());
                    count += 1;
                }
            } else if(message instanceof Map) {
                remote.tell(message, self());
                LOG.info("LocalActor received message : {}" , message);
            }
        }
    }

}

