/*
 * Copyright (c) 2014 Pacnet and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.controller.aoniTed;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.osgi.ActorSystemActivator;
import org.opendaylight.controller.sal.binding.api.BindingAwareBroker.ConsumerContext;
import org.opendaylight.controller.sal.binding.api.BindingAwareConsumer;
import org.opendaylight.controller.sal.binding.api.NotificationService;
import org.opendaylight.yangtools.concepts.Registration;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Activator extends ActorSystemActivator/* AbstractBindingAwareConsumer */ implements AutoCloseable, BindingAwareConsumer {


    private static final Logger LOG = LoggerFactory.getLogger(Activator.class);

    private AoniTedHandler aoniTedHandler;

    private NotificationService notificationService;
    private Registration tedRegistration;
    private ServiceRegistration<LinkPropertyService> serviceRegistration;
    public ActorRef localActor;

    @Override
    public void configure(BundleContext context, ActorSystem system) {
        LOG.info("Core bundle configured");
        registerGreetService(context, system);
        localActor = system.actorOf(Props.create(LocalDemo.LocalActor.class), "LocalActor");
        localActor.tell("START", ActorRef.noSender());
        CreateActorRef.localActorRef = system.actorOf(Props.create(LocalDemo.LocalActor.class), "LocalActorRef");
        //VnmManager.system = system;
        //   system.shutdown();
        LOG.info("akka service registered");
    }

    public void registerGreetService(BundleContext context, ActorSystem system) {
        context.registerService(ActorSystem.class, system, null);
        LinkPropertyService linkPropertyService = new LinkProperty();
        serviceRegistration = context.registerService(LinkPropertyService.class, linkPropertyService, null);
    }
    /*
    @Override
    protected void startImpl(BundleContext context) {
        LOG.info("16.40");
        LOG.info("startImpl() passing");
        //863 LYJ added
        LinkPropertyService linkPropertyService = new LinkProperty();
        serviceRegistration = context.registerService(LinkPropertyService.class, linkPropertyService, new Hashtable<String, String>());
    }
    */

    @Override
    public void onSessionInitialized(ConsumerContext session) {
        LOG.info("inSessionInitialized() passing");
        notificationService = session.getSALService(NotificationService.class);

        //863 LYJ added
        aoniTedHandler=new AoniTedHandler();
        //To send messages
        //aoniTedHandler.setPacketCvniMultipartRequestService(session.getRpcService(PacketCvniMultipartRequestService.class));
        LOG.info("Ted-Test-AoniTedHandler()");
        //To receive messages
        tedRegistration=notificationService.registerNotificationListener(aoniTedHandler);
        LOG.info("Ted-Test-NotificationService");
        //863 ending
    }

    @Override
    public void close() {
        LOG.info("close() passing");
    }


}