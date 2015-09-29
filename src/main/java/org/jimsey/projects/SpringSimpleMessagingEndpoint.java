package org.jimsey.projects;

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
  private SimpMessageSendingOperations mso;

  /** the destination **/
  private String destination;

  /** the header in which to find the destination suffix, optional **/
  @UriParam
  private String destinationSuffixHeader;

  public SpringSimpleMessagingEndpoint() {
  }

  public SpringSimpleMessagingEndpoint(String uri, SpringSimpleMessagingComponent component) {
    super(uri, component);
  }

  public SpringSimpleMessagingEndpoint(String endpointUri) {
    super(endpointUri);
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
    return mso;
  }

  public void setMessageSendingOperations(SimpMessageSendingOperations mso) {
    this.mso = mso;
  }

  public String getDestination() {
    return destination;
  }

  public void setDestination(String destination) {
    this.destination = destination;
  }

  public String getDestinationSuffixHeader() {
    return destinationSuffixHeader;
  }

  public void setDestinationSuffixHeader(String destinationSuffixHeader) {
    this.destinationSuffixHeader = destinationSuffixHeader;
  }

}
