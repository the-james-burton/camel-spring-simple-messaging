package org.jimsey.projects.pojo;

import java.util.Map;

public class ConvertAndSendCall {

  private final String destination;

  private final Map<String, Object> headers;

  private final Object body;

  public ConvertAndSendCall(String destination, Map<String, Object> headers, Object body) {
    super();
    this.destination = destination;
    this.headers = headers;
    this.body = body;
  }

  // ---------------------------------
  public String getDestination() {
    return destination;
  }

  public Map<String, Object> getHeaders() {
    return headers;
  }

  public Object getBody() {
    return body;
  }

}
