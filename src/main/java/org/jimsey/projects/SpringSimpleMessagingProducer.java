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
    Message message = exchange.getIn();
    Object payload = message.getBody();
    Map<String, Object> headers = message.getHeaders();

    String destination = endpoint.getDestination();
    String destinationSuffix = getHeader(headers, endpoint.getDestinationSuffixHeader());
    String user = getHeader(headers, endpoint.getUserHeader());

    if (destinationSuffix != null) {
      destination = destination.concat(destinationSuffix);
      message.removeHeader(endpoint.getDestinationSuffixHeader());
    }

    if (user != null) {
      message.removeHeader(endpoint.getUserHeader());
    }

    if (LOG.isDebugEnabled()) {
      try {
        LOG.debug("seding message: {\"destination\":{}, \"payload\":{}, \"headers\":{}, \"user\":{}}",
            destination, payload, new JSONObject(headers), user);
      } catch (Exception e) {
        LOG.error("error trying to log body or header: {}", e.getMessage());
      }
    }

    if (user == null) {
      endpoint.getMessageSendingOperations().convertAndSend(destination, payload, headers);
    } else {
      endpoint.getMessageSendingOperations().convertAndSendToUser(user, destination, payload, headers);
    }
  }

  private String getHeader(Map<String, Object> headers, String header) {
    if (header == null || headers == null) {
      return null;
    }
    if (!headers.containsKey(header)) {
      log.warn("unable to find header '{}'", header);
      return null;
    }
    if (headers.get(header) == null) {
      log.warn("header '{}' is null", header);
      return null;
    }
    return headers.get(header).toString();
  }

}
