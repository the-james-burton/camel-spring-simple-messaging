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

import static org.hamcrest.Matchers.*;

import java.util.Arrays;
import java.util.Map;

import org.apache.camel.test.junit4.CamelTestSupport;
import org.jimsey.projects.camel.components.pojo.ConvertAndSendCall;
import org.jimsey.projects.camel.components.pojo.ConvertAndSendToUserCall;
import org.json.JSONObject;
import org.mockito.invocation.InvocationOnMock;

public class AbstractTestBase extends CamelTestSupport {

  protected final String destination = "/test/uri";

  protected final Object body = "test-body";

  protected final String headerKey = "test-header-key";

  protected final Object headerValue = "test-header-value";

  protected final String destinationSuffix = ".test.suffix";

  protected final String user = "test-user";

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
