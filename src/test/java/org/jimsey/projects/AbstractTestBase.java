package org.jimsey.projects;

import static org.hamcrest.Matchers.*;

import java.util.Arrays;
import java.util.Map;

import org.apache.camel.test.junit4.CamelTestSupport;
import org.jimsey.projects.pojo.ConvertAndSendCall;
import org.jimsey.projects.pojo.ConvertAndSendToUserCall;
import org.json.JSONObject;
import org.mockito.invocation.InvocationOnMock;

public class AbstractTestBase extends CamelTestSupport {

  protected final String destination = "test/uri";

  protected final Object body = "test-body";

  protected final String headerKey = "test-header-key";

  protected final Object headerValue = "test-header-value";

  protected final String destinationSuffixHeaderKey = "suffix";

  protected final String destinationSuffixHeaderValue = ".test.suffix";

  protected final String userHeaderKey = "user";

  protected final String userHeaderValue = "test-user";

  protected ConvertAndSendCall extractConvertAndSendParameters(InvocationOnMock invocation) {
    assertThat(Arrays.asList(invocation.getArguments()), hasSize(3));
    String destination = invocation.getArgumentAt(0, String.class);
    Object body = invocation.getArgumentAt(1, Object.class);
    Map<String, Object> headers = invocation.getArgumentAt(2, Map.class);
    log.info("mso.conversAndSend('{}', '{}', {}",
        destination, body.toString(), new JSONObject(headers));
    return new ConvertAndSendCall(destination, headers, body);
  }

  protected ConvertAndSendToUserCall extractConvertAndSendToUserParameters(InvocationOnMock invocation) {
    assertThat(Arrays.asList(invocation.getArguments()), hasSize(4));
    String user = invocation.getArgumentAt(0, String.class);
    String destination = invocation.getArgumentAt(1, String.class);
    Object body = invocation.getArgumentAt(2, Object.class);
    Map<String, Object> headers = invocation.getArgumentAt(3, Map.class);
    log.info("mso.conversAndSendToUser('{}', '{}', '{}', {}",
        user, destination, body.toString(), new JSONObject(headers));
    return new ConvertAndSendToUserCall(destination, headers, body, user);
  }

}
