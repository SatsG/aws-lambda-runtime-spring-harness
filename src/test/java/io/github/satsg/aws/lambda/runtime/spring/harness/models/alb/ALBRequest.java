package io.github.satsg.aws.lambda.runtime.spring.harness.models.alb;

import java.util.List;
import java.util.Map;

public class ALBRequest {
  private String path;
  private String httpMethod;
  private Map<String, String> headers;
  private Map<String, List<String>> multiValueHeaders;
  private Map<String, String> queryStringParameters;
  private Map<String, List<String>> multiValueQueryStringParameters;
  private ALBRequestContext requestContext;
  private boolean isBase64Encoded;
  private Object body;

  public ALBRequest() {}

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public String getHttpMethod() {
    return httpMethod;
  }

  public void setHttpMethod(String httpMethod) {
    this.httpMethod = httpMethod;
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

  public Map<String, String> getQueryStringParameters() {
    return queryStringParameters;
  }

  public void setQueryStringParameters(Map<String, String> queryStringParameters) {
    this.queryStringParameters = queryStringParameters;
  }

  public Map<String, List<String>> getMultiValueQueryStringParameters() {
    return multiValueQueryStringParameters;
  }

  public void setMultiValueQueryStringParameters(
      Map<String, List<String>> multiValueQueryStringParameters) {
    this.multiValueQueryStringParameters = multiValueQueryStringParameters;
  }

  public ALBRequestContext getRequestContext() {
    return requestContext;
  }

  public void setRequestContext(ALBRequestContext requestContext) {
    this.requestContext = requestContext;
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
