/*
 * FIXME replace with the text from your license
 * The text of the license.
 */
package com.tailrocks.example.api.client;

import io.micronaut.context.event.BeanCreatedEvent;
import io.micronaut.context.event.BeanCreatedEventListener;

import javax.inject.Singleton;

@Singleton
public class TailrocksExampleClientInitializer implements BeanCreatedEventListener<TailrocksExampleClient> {

    @Override
    public TailrocksExampleClient onCreated(BeanCreatedEvent<TailrocksExampleClient> event) {
        event.getBean().setDefaultTenant("testing");
        return event.getBean();
    }

}
