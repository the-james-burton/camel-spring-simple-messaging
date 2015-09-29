package org.jimsey.projects;

import java.util.Map;

import org.apache.camel.test.junit4.CamelTestSupport;
import org.jimsey.projects.pojo.ConvertAndSendCall;
import org.json.JSONObject;
import org.mockito.invocation.InvocationOnMock;

public class AbstractTestBase extends CamelTestSupport {

  protected final String expectedDestination = "test/uri";

  protected final Object expectedBody = "test-body";

  protected final String expectedHeaderKey = "test-header-key";

  protected final Object expectedHeaderValue = "test-header-value";

  protected final String expectedDestinationSuffixHeaderKey = "suffix";

  protected final String expectedDestinationSuffixHeaderValue = ".test.suffix";

  protected ConvertAndSendCall extractConvertAndSendParameters(InvocationOnMock invocation) {
    String destination = invocation.getArgumentAt(0, String.class);
    Object body = invocation.getArgumentAt(1, Object.class);
    Map<String, Object> headers = invocation.getArgumentAt(2, Map.class);
    log.info("mso.conversAndSend('{}', '{}', {}",
        destination, body.toString(), new JSONObject(headers));
    return new ConvertAndSendCall(destination, headers, body, null);
  }

}
