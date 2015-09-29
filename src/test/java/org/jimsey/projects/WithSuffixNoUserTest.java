package org.jimsey.projects;

import static org.hamcrest.Matchers.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.messaging.simp.SimpMessageSendingOperations;

public class WithSuffixNoUserTest extends CamelTestSupport {

  SimpMessageSendingOperations mso;

  @Produce(uri = "direct:start")
  ProducerTemplate producer;

  private final String expectedDestination = "test/uri";

  private final String expectedDestinationSuffixHeaderKey = "suffix";

  private final String expectedDestinationSuffixHeaderValue = ".test.suffix";

  private final Object expectedBody = "test-body";

  private final String expectedHeaderKey = "test-header-key";

  private final Object expectedHeaderValue = "test-header-value";

  @Before
  public void setup() {
  }

  @Test
  public void withSuffixNoUserTest() throws Exception {

    // TODO use lambda when upgraded to java 8...
    doAnswer(new Answer<Void>() {

      @Override
      public Void answer(InvocationOnMock invocation) throws Throwable {
        String receivedDestination = invocation.getArgumentAt(0, String.class);
        Object receivedBody = invocation.getArgumentAt(1, Object.class);
        Map<String, Object> receivedHeaders = invocation.getArgumentAt(2, Map.class);
        log.info("mso.conversAndSend('{}', '{}', {}",
            receivedDestination, receivedBody.toString(), new JSONObject(receivedHeaders));

        assertThat(receivedDestination, equalTo(String.format("/%s%s", expectedDestination, expectedDestinationSuffixHeaderValue)));
        assertThat(receivedBody.toString(), equalTo(expectedBody.toString()));
        assertThat(receivedHeaders, hasEntry(equalTo(expectedHeaderKey), equalTo(expectedHeaderValue)));
        assertThat(receivedHeaders, not(hasKey(equalTo(expectedDestinationSuffixHeaderKey))));

        return null;
      }

    }).when(mso).convertAndSend(anyString(), anyObject(), anyMapOf(String.class, Object.class));

    Map<String, Object> headers = new HashMap<>();
    headers.put(expectedHeaderKey, expectedHeaderValue);
    headers.put(expectedDestinationSuffixHeaderKey, expectedDestinationSuffixHeaderValue);

    MockEndpoint mock = getMockEndpoint("mock:result");
    mock.expectedMessageCount(1);
    mock.expectedBodiesReceived(expectedBody);
    mock.expectedHeaderReceived(expectedHeaderKey, expectedHeaderValue);

    producer.sendBodyAndHeaders(expectedBody, headers);

    assertMockEndpointsSatisfied();
  }

  @Override
  protected RouteBuilder createRouteBuilder() throws Exception {
    mso = mock(SimpMessageSendingOperations.class);
    context.addComponent("test-springsm", new SpringSimpleMessagingComponent(mso));

    return new RouteBuilder() {
      public void configure() {
        from("direct://start")
            .to(String.format("test-springsm://%s?destinationSuffixHeader=%s",
                expectedDestination, expectedDestinationSuffixHeaderKey))
            .to(String.format("log:%s?level=INFO&showBody=true&showHeaders=true", this.getClass().getName()))
            .to("mock:result");
      }
    };
  }
}
