package org.jimsey.projects;

import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The CamelSpringSimpleMessaging producer.
 */
public class CamelSpringSimpleMessagingProducer extends DefaultProducer {
    private static final Logger LOG = LoggerFactory.getLogger(CamelSpringSimpleMessagingProducer.class);
    private CamelSpringSimpleMessagingEndpoint endpoint;

    public CamelSpringSimpleMessagingProducer(CamelSpringSimpleMessagingEndpoint endpoint) {
        super(endpoint);
        this.endpoint = endpoint;
    }

    public void process(Exchange exchange) throws Exception {
        System.out.println(exchange.getIn().getBody());    
    }

}
