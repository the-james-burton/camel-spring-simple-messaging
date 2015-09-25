package org.jimsey.projects;

import java.util.Map;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.impl.UriEndpointComponent;
import org.springframework.messaging.simp.SimpMessageSendingOperations;

/**
 * Represents the component that manages {@link SpringSimpleMessagingEndpoint}.
 */
public class SpringSimpleMessagingComponent extends UriEndpointComponent {

  /** the spring simple messaging template to use when sending messages */
  private SimpMessageSendingOperations mso;

  /** main constructor to use */
  public SpringSimpleMessagingComponent(SimpMessageSendingOperations mso) {
    this();
    this.setMso(mso);
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
    endpoint.setMessageSendingOperations(mso);
    endpoint.setDestination(remaining);
    return endpoint;
  }

  // --------------------------------------------------------
  public SimpMessageSendingOperations getMso() {
    return mso;
  }

  public void setMso(SimpMessageSendingOperations mso) {
    this.mso = mso;
  }
}
