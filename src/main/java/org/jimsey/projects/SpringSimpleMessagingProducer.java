package org.jimsey.projects;

import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.impl.DefaultProducer;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The CamelSpringSimpleMessaging producer.
 */
public class SpringSimpleMessagingProducer extends DefaultProducer {
  private static final Logger LOG = LoggerFactory.getLogger(SpringSimpleMessagingProducer.class);

  private SpringSimpleMessagingEndpoint endpoint;

  public SpringSimpleMessagingProducer(SpringSimpleMessagingEndpoint endpoint) {
    super(endpoint);
    this.endpoint = endpoint;
  }

  public void process(Exchange exchange) throws Exception {
    // System.out.println(exchange.getIn().getBody());
    String destination = endpoint.getDestination();
    Message message = exchange.getIn();
    Object payload = message.getBody();
    Map<String, Object> headers = message.getHeaders();
    if (LOG.isDebugEnabled()) {
      try {
        LOG.debug("body: {}", payload);
        LOG.debug("headers: {}", new JSONObject(headers));
      } catch (Exception e) {
        LOG.error("error trying to log body or header: {}", e.getMessage());
      }
    }
    endpoint.getMessageSendingOperations().convertAndSend(destination, payload, headers);
  }

}
