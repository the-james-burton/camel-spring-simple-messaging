package org.jimsey.projects;

import static org.mockito.Matchers.anyMapOf;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

import java.util.HashMap;
import java.util.Map;

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

    // TODO use lambda when upgraded to java 8...
    doAnswer(new Answer() {

      @Override
      public Object answer(InvocationOnMock invocation) throws Throwable {
        Object args[] = invocation.getArguments();
        for (Object arg : args) {
          logger.info(arg == null ? "" : arg.toString());
        }
        return null;
      }

    }).when(mso).convertAndSend(anyString(), anyObject(), anyMapOf(String.class, Object.class));

    String body = "test-body";
    String key = "test-header-key";
    String value = "test-header-value";

    Map<String, Object> headers = new HashMap<>();
    headers.put(key, value);

    MockEndpoint mock = getMockEndpoint("mock:result");
    mock.expectedMessageCount(1);
    mock.expectedBodiesReceived(body);

    producer.sendBodyAndHeaders(body, headers);

    assertMockEndpointsSatisfied();
  }

  @Override
  protected RouteBuilder createRouteBuilder() throws Exception {
    mso = mock(SimpMessageSendingOperations.class);
    context.addComponent("test-springsm", new SpringSimpleMessagingComponent(mso));

    return new RouteBuilder() {
      public void configure() {
        from("direct://start")
            .to("test-springsm://test/uri")
            .to(String.format("log:%s?level=INFO&showBody=true&showHeaders=true", this.getClass().getName()))
            .to("mock:result");
      }
    };
  }
}
