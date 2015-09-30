package org.jimsey.projects;

import static org.hamcrest.Matchers.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.jimsey.projects.pojo.ConvertAndSendCall;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.messaging.simp.SimpMessageSendingOperations;

public class NoSuffixNoUserTest extends AbstractTestBase {

  SimpMessageSendingOperations mso;

  @Produce(uri = "direct:start")
  ProducerTemplate producer;

  @Before
  public void setup() {
  }

  @Test
  public void noSuffixNoUserNoHeadersTest() throws Exception {

    // TODO use lambda when upgraded to java 8...
    doAnswer(new Answer<Void>() {

      @Override
      public Void answer(InvocationOnMock invocation) throws Throwable {
        ConvertAndSendCall received = extractConvertAndSendParameters(invocation);
        assertThat(received.getDestination(),
            equalTo(String.format("/%s", destination)));
        assertThat(received.getBody().toString(), equalTo(body.toString()));
        assertThat(received.getHeaders(), hasKey(equalTo(Exchange.BREADCRUMB_ID)));
        assertThat(received.getHeaders().keySet(), hasSize(1));

        return null;
      }

    }).when(mso).convertAndSend(anyString(), anyObject(), anyMapOf(String.class, Object.class));

    MockEndpoint mock = getMockEndpoint("mock:result");
    mock.expectedMessageCount(1);
    mock.expectedBodiesReceived(body);

    producer.sendBody(body);

    assertMockEndpointsSatisfied();
  }

  @Test
  public void noSuffixNoUserWithHeadersTest() throws Exception {

    // TODO use lambda when upgraded to java 8...
    doAnswer(new Answer<Void>() {

      @Override
      public Void answer(InvocationOnMock invocation) throws Throwable {
        ConvertAndSendCall received = extractConvertAndSendParameters(invocation);
        assertThat(received.getDestination(), equalTo(String.format("/%s", destination)));
        assertThat(received.getBody().toString(), equalTo(body.toString()));
        assertThat(received.getHeaders(), hasEntry(equalTo(headerKey), equalTo(headerValue)));

        //
        return null;
      }

    }).when(mso).convertAndSend(anyString(), anyObject(), anyMapOf(String.class, Object.class));

    Map<String, Object> headers = new HashMap<>();
    headers.put(headerKey, headerValue);

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
            .to(String.format("test-springsm://%s", destination))
            .to(String.format("log:%s?level=INFO&showBody=true&showHeaders=true", this.getClass().getName()))
            .to("mock:result");
      }
    };
  }
}
