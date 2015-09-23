package org.jimsey.projects;

import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.impl.DefaultEndpoint;
import org.apache.camel.spi.Metadata;
import org.apache.camel.spi.UriEndpoint;
import org.apache.camel.spi.UriParam;
import org.apache.camel.spi.UriPath;

/**
 * Represents a CamelSpringSimpleMessaging endpoint.
 */
@UriEndpoint(scheme = "simpmsg", title = "CamelSpringSimpleMessaging", syntax="simpmsg:name", consumerClass = CamelSpringSimpleMessagingConsumer.class, label = "CamelSpringSimpleMessaging")
public class CamelSpringSimpleMessagingEndpoint extends DefaultEndpoint {
    @UriPath @Metadata(required = "true")
    private String name;
    @UriParam(defaultValue = "10")
    private int option = 10;

    public CamelSpringSimpleMessagingEndpoint() {
    }

    public CamelSpringSimpleMessagingEndpoint(String uri, CamelSpringSimpleMessagingComponent component) {
        super(uri, component);
    }

    public CamelSpringSimpleMessagingEndpoint(String endpointUri) {
        super(endpointUri);
    }

    public Producer createProducer() throws Exception {
        return new CamelSpringSimpleMessagingProducer(this);
    }

    public Consumer createConsumer(Processor processor) throws Exception {
        return new CamelSpringSimpleMessagingConsumer(this, processor);
    }

    public boolean isSingleton() {
        return true;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setOption(int option) {
        this.option = option;
    }

    public int getOption() {
        return option;
    }
}
