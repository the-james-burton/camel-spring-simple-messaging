package org.jimsey.projects;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

public class CamelSpringSimpleMessagingComponentTest extends CamelTestSupport {

    @Test
    public void testCamelSpringSimpleMessaging() throws Exception {
        MockEndpoint mock = getMockEndpoint("mock:result");
        mock.expectedMinimumMessageCount(1);       
        
        assertMockEndpointsSatisfied();
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            public void configure() {
                from("simpmsg://foo")
                  .to("simpmsg://bar")
                  .to("mock:result");
            }
        };
    }
}
