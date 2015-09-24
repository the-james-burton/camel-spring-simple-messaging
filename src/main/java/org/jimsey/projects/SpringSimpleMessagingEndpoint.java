package org.jimsey.projects;

import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.impl.DefaultEndpoint;
import org.apache.camel.spi.Metadata;
import org.apache.camel.spi.UriEndpoint;
import org.apache.camel.spi.UriParam;
import org.apache.camel.spi.UriPath;

/**
 * Represents a CamelSpringSimpleMessaging endpoint.
 */
@UriEndpoint(scheme = "springsm", title = "CamelSpringSimpleMessaging", syntax = "springsm:name",
    consumerClass = SpringSimpleMessagingConsumer.class, label = "CamelSpringSimpleMessaging")
public class SpringSimpleMessagingEndpoint extends DefaultEndpoint {
  @UriPath
  @Metadata(required = "true")
  private String name;

  @UriParam(defaultValue = "10")
  private int option = 10;

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
    return new SpringSimpleMessagingConsumer(this, processor);
  }

  public boolean isSingleton() {
    return true;
  }

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
}
