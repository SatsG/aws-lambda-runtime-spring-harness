package io.github.satsg.aws.lambda.runtime.spring.harness.models.apigateway.v1;

import java.util.List;
import java.util.Map;

public class APIGatewayRequestV1 {
  private String version;
  private String resource;
  private String path;
  private String httpMethod;
  private Map<String, String> headers;
  private Map<String, List<String>> multiValueHeaders;
  private Map<String, String> queryStringParameters;
  private Map<String, List<String>> multiValueQueryStringParameters;
  private APIGatewayRequestContextV1 requestContext;
  private Map<String, String> pathParameters;
  private Map<String, String> stageVariables;
  private boolean isBase64Encoded;
  private Object body;

  public APIGatewayRequestV1() {}

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public String getResource() {
    return resource;
  }

  public void setResource(String resource) {
    this.resource = resource;
  }

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

  public APIGatewayRequestContextV1 getRequestContext() {
    return requestContext;
  }

  public void setRequestContext(APIGatewayRequestContextV1 requestContext) {
    this.requestContext = requestContext;
  }

  public Map<String, String> getPathParameters() {
    return pathParameters;
  }

  public void setPathParameters(Map<String, String> pathParameters) {
    this.pathParameters = pathParameters;
  }

  public Map<String, String> getStageVariables() {
    return stageVariables;
  }

  public void setStageVariables(Map<String, String> stageVariables) {
    this.stageVariables = stageVariables;
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
