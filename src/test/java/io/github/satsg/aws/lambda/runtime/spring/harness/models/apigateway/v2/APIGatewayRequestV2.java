package io.github.satsg.aws.lambda.runtime.spring.harness.models.apigateway.v2;

import java.util.List;
import java.util.Map;

public class APIGatewayRequestV2 {
  private String version;
  private String routeKey;
  private String rawPath;
  private String rawQueryString;
  private List<String> query;
  private Map<String, String> headers;
  private Map<String, String> queryStringParameters;
  private APIGatewayRequestContextV2 requestContext;
  private Map<String, String> pathParameters;
  private Map<String, String> stageVariables;
  private boolean isBase64Encoded;
  private Object body;

  public APIGatewayRequestV2() {}

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public String getRouteKey() {
    return routeKey;
  }

  public void setRouteKey(String routeKey) {
    this.routeKey = routeKey;
  }

  public String getRawPath() {
    return rawPath;
  }

  public void setRawPath(String rawPath) {
    this.rawPath = rawPath;
  }

  public String getRawQueryString() {
    return rawQueryString;
  }

  public void setRawQueryString(String rawQueryString) {
    this.rawQueryString = rawQueryString;
  }

  public List<String> getQuery() {
    return query;
  }

  public void setQuery(List<String> query) {
    this.query = query;
  }

  public Map<String, String> getHeaders() {
    return headers;
  }

  public void setHeaders(Map<String, String> headers) {
    this.headers = headers;
  }

  public Map<String, String> getQueryStringParameters() {
    return queryStringParameters;
  }

  public void setQueryStringParameters(Map<String, String> queryStringParameters) {
    this.queryStringParameters = queryStringParameters;
  }

  public APIGatewayRequestContextV2 getRequestContext() {
    return requestContext;
  }

  public void setRequestContext(APIGatewayRequestContextV2 requestContext) {
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

  public boolean isBase64Encoded() {
    return isBase64Encoded;
  }

  public void setBase64Encoded(boolean base64Encoded) {
    isBase64Encoded = base64Encoded;
  }

  public Object getBody() {
    return body;
  }

  public void setBody(Object body) {
    this.body = body;
  }
}
