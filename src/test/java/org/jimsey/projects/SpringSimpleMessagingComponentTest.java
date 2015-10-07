/**
 * The MIT License
 * Copyright (c) 2015 the-james-burton
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
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
import org.jimsey.projects.pojo.ConvertAndSendToUserCall;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.messaging.simp.SimpMessageSendingOperations;

public class SpringSimpleMessagingComponentTest extends AbstractTestBase {

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
            equalTo(String.format("%s", destination)));
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
        assertThat(received.getDestination(), equalTo(String.format("%s", destination)));
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

  @Test
  public void withSuffixNoUserTest() throws Exception {

    // TODO use lambda when upgraded to java 8...
    doAnswer(new Answer<Void>() {

      @Override
      public Void answer(InvocationOnMock invocation) throws Throwable {
        ConvertAndSendCall received = extractConvertAndSendParameters(invocation);
        assertThat(received.getDestination(),
            equalTo(String.format("%s%s", destination, destinationSuffix)));
        assertThat(received.getBody().toString(), equalTo(body.toString()));
        assertThat(received.getHeaders(), hasEntry(equalTo(headerKey), equalTo(headerValue)));
        assertThat(received.getHeaders(), not(hasKey(equalTo(SpringSimpleMessagingConstants.DESTINATION_SUFFIX))));

        return null;
      }

    }).when(mso).convertAndSend(anyString(), anyObject(), anyMapOf(String.class, Object.class));

    Map<String, Object> headers = new HashMap<>();
    headers.put(headerKey, headerValue);
    headers.put(SpringSimpleMessagingConstants.DESTINATION_SUFFIX, destinationSuffix);

    MockEndpoint mock = getMockEndpoint("mock:result");
    mock.expectedMessageCount(1);
    mock.expectedBodiesReceived(body);
    mock.expectedHeaderReceived(headerKey, headerValue);

    producer.sendBodyAndHeaders(body, headers);

    assertMockEndpointsSatisfied();
  }

  @Test
  public void withSuffixWithUserTest() throws Exception {

    // TODO use lambda when upgraded to java 8...
    doAnswer(new Answer<Void>() {

      @Override
      public Void answer(InvocationOnMock invocation) throws Throwable {
        ConvertAndSendToUserCall received = extractConvertAndSendToUserParameters(invocation);
        assertThat(received.getDestination(),
            equalTo(String.format("%s%s", destination, destinationSuffix)));
        assertThat(received.getUser(), equalTo(user));
        assertThat(received.getBody().toString(), equalTo(body.toString()));
        assertThat(received.getHeaders(), hasEntry(equalTo(headerKey), equalTo(headerValue)));
        assertThat(received.getHeaders(), not(hasKey(equalTo(SpringSimpleMessagingConstants.DESTINATION_SUFFIX))));
        assertThat(received.getHeaders(), not(hasKey(equalTo(SpringSimpleMessagingConstants.USER))));

        return null;
      }

    }).when(mso).convertAndSendToUser(anyString(), anyString(), anyObject(), anyMapOf(String.class, Object.class));

    Map<String, Object> headers = new HashMap<>();
    headers.put(headerKey, headerValue);
    headers.put(SpringSimpleMessagingConstants.DESTINATION_SUFFIX, destinationSuffix);
    headers.put(SpringSimpleMessagingConstants.USER, user);

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
