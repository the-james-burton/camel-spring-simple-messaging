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
package org.jimsey.projects.camel.components;

import java.util.Map;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.impl.UriEndpointComponent;
import org.springframework.messaging.simp.SimpMessageSendingOperations;

/**
<strong>Camel spring simple messaging component</strong>
<p>
This is a component for apache camel that lets you send messages to an implementation of Spring's {@code SimpMessageSendingOperations}  such as {@code SimpMessagingTemplate}.
<p>
This is useful when you want to send (for example) STOMP messages over websockets using Spring (see Ch.21.4) when using Camel routes.
<p>
<em>This is a producer-only component. Consuming messages is not currently supported.</em>
<p>
<strong>How to use it</strong>
<p>
A sample {@code configure}  method implementation in your Camel {@code RouteBuilder}  might look like the below. Your implementation of {@code AbstractWebSocketMessageBrokerConfigurer}  will inject the {@code SimpMessagingTemplate}.
<pre><code>
{@literal @}Autowired
private SimpMessagingTemplate template;

{@literal @}Override
public void configure() throws Exception {

  getContext().addComponent("ssm", new SpringSimpleMessagingComponent(template));

  from("direct:your.message.source")
  .to("ssm://topic/your.topic.name")
  .end();
}
</code></pre>
<p>
This component will use the camel exchange {@code in}  body as the {@code SimpMessagingTemplate} payload. There are no special requirements for the payload beyond simply being a java {@code Object} .
<p>
This component will also pass through all headers, including Camel headers except for the control headers documented below.
<p>
<strong>URI format</strong>
<pre>{@code
<name>://destinaton
}</pre>
<p>
Where {@code <name>} is the name you gave it when you added the component and {@code destination} is where you want to send it to via the Spring {@code SimpMessageSendingOperations.convertAndSend} methods. See the options below for how to customise the destination dynamically, via a header.
<p>
<strong>Options</strong>
<p>
There are no URI options in this component.
<p>
<strong>Headers</strong>
<p>
These messaging control headers are removed before sending to the {@code SimpMessagingTemplate}. All other headers will remain on the message.
<p>
{@code SpringSimpleMessagingConstants.DESTINATION_SUFFIX} : An optional suffix to add to the destination. Useful to control message routing dynamically.
<p>
{@code SpringSimpleMessagingConstants.USER} : If present, the component will call the {@code convertAndSendToUser} message on the {@code SimpMessagingTemplate} with the {@code user} parameter set to the value of this header. If not present then the {@code convertAndSend} method will be used instead, which does not require a {@code user} parameter.
 */
public class SpringSimpleMessagingComponent extends UriEndpointComponent {

  /** the spring simple messaging template to use when sending messages */
  private SimpMessageSendingOperations messageSendingOperations;

  /** main constructor to use 
   * @param mso the message sending implementation to use */
  public SpringSimpleMessagingComponent(SimpMessageSendingOperations mso) {
    this();
    this.setMessageSendingOperations(mso);
  }

  public SpringSimpleMessagingComponent() {
    super(SpringSimpleMessagingEndpoint.class);
  }

  public SpringSimpleMessagingComponent(CamelContext context) {
    super(context, SpringSimpleMessagingEndpoint.class);
  }

  protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {
    SpringSimpleMessagingEndpoint endpoint = new SpringSimpleMessagingEndpoint(uri, this);
    setProperties(endpoint, parameters);
    endpoint.setMessageSendingOperations(messageSendingOperations);
    endpoint.setDestination(remaining);
    return endpoint;
  }

  // --------------------------------------------------------
  public SimpMessageSendingOperations getMessageSendingOperations() {
    return messageSendingOperations;
  }

  public void setMessageSendingOperations(SimpMessageSendingOperations mso) {
    this.messageSendingOperations = mso;
  }
}
