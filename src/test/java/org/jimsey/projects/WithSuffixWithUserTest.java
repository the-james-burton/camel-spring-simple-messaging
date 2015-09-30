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
import org.jimsey.projects.pojo.ConvertAndSendToUserCall;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.messaging.simp.SimpMessageSendingOperations;

public class WithSuffixWithUserTest extends AbstractTestBase {

  SimpMessageSendingOperations mso;

  @Produce(uri = "direct:start")
  ProducerTemplate producer;

  @Before
  public void setup() {
  }

  @Test
  public void withSuffixWithUserTest() throws Exception {

    // TODO use lambda when upgraded to java 8...
    doAnswer(new Answer<Void>() {

      @Override
      public Void answer(InvocationOnMock invocation) throws Throwable {
        ConvertAndSendToUserCall received = extractConvertAndSendToUserParameters(invocation);
        assertThat(received.getDestination(),
            equalTo(String.format("/%s%s", destination, destinationSuffixHeaderValue)));
        assertThat(received.getUser(), equalTo(userHeaderValue));
        assertThat(received.getBody().toString(), equalTo(body.toString()));
        assertThat(received.getHeaders(), hasEntry(equalTo(headerKey), equalTo(headerValue)));
        assertThat(received.getHeaders(), not(hasKey(equalTo(destinationSuffixHeaderKey))));
        assertThat(received.getHeaders(), not(hasKey(equalTo(userHeaderKey))));

        return null;
      }

    }).when(mso).convertAndSendToUser(anyString(), anyString(), anyObject(), anyMapOf(String.class, Object.class));

    Map<String, Object> headers = new HashMap<>();
    headers.put(headerKey, headerValue);
    headers.put(destinationSuffixHeaderKey, destinationSuffixHeaderValue);
    headers.put(userHeaderKey, userHeaderValue);

    MockEndpoint mock = getMockEndpoint("mock:result");
    mock.expectedMessageCount(1);
    mock.expectedBodiesReceived(body);
    mock.expectedHeaderReceived(headerKey, headerValue);

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
            .to(String.format("test-springsm://%s?destinationSuffixHeader=%s&userHeader=%s",
                destination, destinationSuffixHeaderKey, userHeaderKey))
            .to(String.format("log:%s?level=INFO&showBody=true&showHeaders=true", this.getClass().getName()))
            .to("mock:result");
      }
    };
  }
}
