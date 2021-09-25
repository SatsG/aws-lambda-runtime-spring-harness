package io.github.satsg.aws.lambda.runtime.spring.harness.event;

import java.util.List;
import java.util.Map;

public class AWSLambdaCustomResponse {
  private int statusCode;
  private Map<String, String> headers;
  private Map<String, List<String>> multiValueHeaders;
  private boolean isBase64Encoded;
  private String body;

  public AWSLambdaCustomResponse() {
    this.statusCode = 200;
    this.headers = null;
    this.multiValueHeaders = null;
    this.isBase64Encoded = false;
    this.body = "";
  }

  public int getStatusCode() {
    return statusCode;
  }

  public void setStatusCode(int statusCode) {
    this.statusCode = statusCode;
  }

  public Map<String, String> getHeaders() {
    return headers;
  }

  public void setHeaders(Map<String, String> headers) {
    this.headers = headers;
  }

  public Map<String, List<String>> getMultiValueHeaders() {
    return multiValueHeaders;
  }

  public void setMultiValueHeaders(Map<String, List<String>> multiValueHeaders) {
    this.multiValueHeaders = multiValueHeaders;
  }

  public boolean getIsBase64Encoded() {
    return isBase64Encoded;
  }

  public void setIsBase64Encoded(boolean isBase64Encoded) {
    this.isBase64Encoded = isBase64Encoded;
  }

  public String getBody() {
    return body;
  }

  public void setBody(String body) {
    this.body = body;
  }
}
