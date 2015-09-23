package org.jimsey.projects;

import java.util.Map;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;

import org.apache.camel.impl.UriEndpointComponent;

/**
 * Represents the component that manages {@link CamelSpringSimpleMessagingEndpoint}.
 */
public class CamelSpringSimpleMessagingComponent extends UriEndpointComponent {
    
    public CamelSpringSimpleMessagingComponent() {
        super(CamelSpringSimpleMessagingEndpoint.class);
    }

    public CamelSpringSimpleMessagingComponent(CamelContext context) {
        super(context, CamelSpringSimpleMessagingEndpoint.class);
    }

    protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {
        Endpoint endpoint = new CamelSpringSimpleMessagingEndpoint(uri, this);
        setProperties(endpoint, parameters);
        return endpoint;
    }
}
