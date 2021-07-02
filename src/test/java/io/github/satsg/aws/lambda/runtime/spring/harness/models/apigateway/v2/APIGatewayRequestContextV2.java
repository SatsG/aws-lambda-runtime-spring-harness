package io.github.satsg.aws.lambda.runtime.spring.harness.models.apigateway.v2;

public class APIGatewayRequestContextV2 {
  private String accountId;
  private String apiId;
  private APIGatewayRequestContextAuthenticationV2 authentication;
  private APIGatewayRequestContextAuthorizerV2 authorizer;
  private String domainName;
  private String domainPrefix;
  private APIGatewayRequestContextHttpV2 http;
  private String requestId;
  private String routeKey;
  private String stage;
  private String time;
  private long timeEpoch;

  public APIGatewayRequestContextV2() {}

  public String getAccountId() {
    return accountId;
  }

  public void setAccountId(String accountId) {
    this.accountId = accountId;
  }

  public String getApiId() {
    return apiId;
  }

  public void setApiId(String apiId) {
    this.apiId = apiId;
  }

  public APIGatewayRequestContextAuthenticationV2 getAuthentication() {
    return authentication;
  }

  public void setAuthentication(APIGatewayRequestContextAuthenticationV2 authentication) {
    this.authentication = authentication;
  }

  public APIGatewayRequestContextAuthorizerV2 getAuthorizer() {
    return authorizer;
  }

  public void setAuthorizer(APIGatewayRequestContextAuthorizerV2 authorizer) {
    this.authorizer = authorizer;
  }

  public String getDomainName() {
    return domainName;
  }

  public void setDomainName(String domainName) {
    this.domainName = domainName;
  }

  public String getDomainPrefix() {
    return domainPrefix;
  }

  public void setDomainPrefix(String domainPrefix) {
    this.domainPrefix = domainPrefix;
  }

  public APIGatewayRequestContextHttpV2 getHttp() {
    return http;
  }

  public void setHttp(APIGatewayRequestContextHttpV2 http) {
    this.http = http;
  }

  public String getRequestId() {
    return requestId;
  }

  public void setRequestId(String requestId) {
    this.requestId = requestId;
  }

  public String getRouteKey() {
    return routeKey;
  }

  public void setRouteKey(String routeKey) {
    this.routeKey = routeKey;
  }

  public String getStage() {
    return stage;
  }

  public void setStage(String stage) {
    this.stage = stage;
  }

  public String getTime() {
    return time;
  }

  public void setTime(String time) {
    this.time = time;
  }

  public long getTimeEpoch() {
    return timeEpoch;
  }

  public void setTimeEpoch(long timeEpoch) {
    this.timeEpoch = timeEpoch;
  }
}
