package org.jimsey.projects.pojo;

import java.util.Map;

public class ConvertAndSendToUserCall extends ConvertAndSendCall {

  private final String user;

  public ConvertAndSendToUserCall(String destination, Map<String, Object> headers, Object body, String user) {
    super(destination, headers, body);
    this.user = user;
  }

  // ---------------------------------
  public String getUser() {
    return user;
  }

}
