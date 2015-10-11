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
    String destinationSuffix = getHeader(headers, SpringSimpleMessagingConstants.DESTINATION_SUFFIX);
    String user = getHeader(headers, SpringSimpleMessagingConstants.USER);

    if (destinationSuffix != null) {
      destination = destination.concat(destinationSuffix);
      message.removeHeader(SpringSimpleMessagingConstants.DESTINATION_SUFFIX);
    }

    if (user != null) {
      message.removeHeader(SpringSimpleMessagingConstants.USER);
    }

    if (LOG.isDebugEnabled()) {
      try {
        LOG.debug("sending message: {\"destination\":{}, \"payload\":{}, \"headers\":{}, \"user\":{}}",
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
      log.trace("unable to find header '{}'", header);
      return null;
    }
    if (headers.get(header) == null) {
      log.trace("header '{}' is null", header);
      return null;
    }
    return headers.get(header).toString();
  }

}
