package org.jimsey.projects;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessageSendingOperations;

public class SpringSimpleMessagingComponentTest extends CamelTestSupport {

  private static final Logger logger = LoggerFactory.getLogger(SpringSimpleMessagingComponentTest.class);

  SimpMessageSendingOperations mso;

  @Produce(uri = "direct:start")
  ProducerTemplate producer;

  @Before
  public void setup() {
  }

  @Test
  public void testCamelSpringSimpleMessaging() throws Exception {

    doAnswer(new Answer() {

      @Override
      public Object answer(InvocationOnMock invocation) throws Throwable {
        Object args[] = invocation.getArguments();
        for (Object arg : args) {
          logger.info(arg.toString());
        }
        return null;
      }

    }).when(mso).convertAndSend(anyString(), anyObject(), anyMapOf(String.class, Object.class));

    producer.sendBody("my test body");

    MockEndpoint mock = getMockEndpoint("mock:result");
    mock.expectedMinimumMessageCount(1);

    assertMockEndpointsSatisfied();
  }

  @Override
  protected RouteBuilder createRouteBuilder() throws Exception {
    mso = mock(SimpMessageSendingOperations.class);
    context.addComponent("test-springsm", new SpringSimpleMessagingComponent(mso));

    return new RouteBuilder() {
      public void configure() {
        from("direct://start")
            .to("test-springsm://test-uri")
            .to("mock:result");
      }
    };
  }
}
