package io.github.satsg.aws.lambda.runtime.spring.harness.event;

import java.util.Map;

public class AWSLambdaCustomResponse {
  private int statusCode;
  private Map<String, String> headers;
  private boolean isBase64Encoded;
  private Object body;

  public AWSLambdaCustomResponse() {
    this.statusCode = 200;
    this.headers = null;
    this.isBase64Encoded = false;
    this.body = null;
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

  public boolean getIsBase64Encoded() {
    return isBase64Encoded;
  }

  public void setIsBase64Encoded(boolean isBase64Encoded) {
    this.isBase64Encoded = isBase64Encoded;
  }

  public Object getBody() {
    return body;
  }

  public void setBody(Object body) {
    this.body = body;
  }
}
