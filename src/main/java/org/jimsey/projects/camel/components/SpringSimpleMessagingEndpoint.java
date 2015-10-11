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

import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.impl.DefaultEndpoint;
import org.apache.camel.spi.Metadata;
import org.apache.camel.spi.UriEndpoint;
import org.apache.camel.spi.UriParam;
import org.apache.camel.spi.UriPath;
import org.springframework.messaging.simp.SimpMessageSendingOperations;

/**
 * Represents a CamelSpringSimpleMessaging endpoint.
 */
@UriEndpoint(scheme = "springsm", title = "CamelSpringSimpleMessaging", syntax = "springsm:name",
    label = "CamelSpringSimpleMessaging", producerOnly = true)
public class SpringSimpleMessagingEndpoint extends DefaultEndpoint {
  @UriPath
  @Metadata(required = "true")
  private String name;

  @UriParam(defaultValue = "10")
  private int option = 10;

  /** the spring simple messaging template to use when sending messages */
  private SimpMessageSendingOperations messageSendingOperations;

  /** the destination **/
  private String destination;

  public SpringSimpleMessagingEndpoint() {
  }

  public SpringSimpleMessagingEndpoint(String uri, SpringSimpleMessagingComponent component) {
    super(uri, component);
  }

  public Producer createProducer() throws Exception {
    return new SpringSimpleMessagingProducer(this);
  }

  public Consumer createConsumer(Processor processor) throws Exception {
    throw new UnsupportedOperationException(
        "You cannot receive messages tfrom this endpoint:" + getEndpointUri());
  }

  public boolean isSingleton() {
    return true;
  }

  // -----------------------------------
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

  public SimpMessageSendingOperations getMessageSendingOperations() {
    return messageSendingOperations;
  }

  public void setMessageSendingOperations(SimpMessageSendingOperations mso) {
    this.messageSendingOperations = mso;
  }

  public String getDestination() {
    return destination;
  }

  public void setDestination(String destination) {
    this.destination = destination;
  }

}
