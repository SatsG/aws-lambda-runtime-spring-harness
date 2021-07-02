package com.github.satsg.aws.lambda.runtime.spring.harness.models.apigateway.v1;

public class APIGatewayRequestContextV1 {
  private String accountId;
  private String apiId;
  private APIGatewayRequestContextAuthorizerV1 authorizer;
  private String domainName;
  private String domainPrefix;
  private String extendedRequestId;
  private String httpMethod;
  private APIGatewayRequestContextIdentityV1 identity;
  private String path;
  private String protocol;
  private String requestId;
  private String requestTime;
  private long requestTimeEpoch;
  private String resourceId;
  private String resourcePath;
  private String stage;

  public APIGatewayRequestContextV1() {}

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

  public APIGatewayRequestContextAuthorizerV1 getAuthorizer() {
    return authorizer;
  }

  public void setAuthorizer(APIGatewayRequestContextAuthorizerV1 authorizer) {
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

  public String getExtendedRequestId() {
    return extendedRequestId;
  }

  public void setExtendedRequestId(String extendedRequestId) {
    this.extendedRequestId = extendedRequestId;
  }

  public String getHttpMethod() {
    return httpMethod;
  }

  public void setHttpMethod(String httpMethod) {
    this.httpMethod = httpMethod;
  }

  public APIGatewayRequestContextIdentityV1 getIdentity() {
    return identity;
  }

  public void setIdentity(APIGatewayRequestContextIdentityV1 identity) {
    this.identity = identity;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public String getProtocol() {
    return protocol;
  }

  public void setProtocol(String protocol) {
    this.protocol = protocol;
  }

  public String getRequestId() {
    return requestId;
  }

  public void setRequestId(String requestId) {
    this.requestId = requestId;
  }

  public String getRequestTime() {
    return requestTime;
  }

  public void setRequestTime(String requestTime) {
    this.requestTime = requestTime;
  }

  public long getRequestTimeEpoch() {
    return requestTimeEpoch;
  }

  public void setRequestTimeEpoch(long requestTimeEpoch) {
    this.requestTimeEpoch = requestTimeEpoch;
  }

  public String getResourceId() {
    return resourceId;
  }

  public void setResourceId(String resourceId) {
    this.resourceId = resourceId;
  }

  public String getResourcePath() {
    return resourcePath;
  }

  public void setResourcePath(String resourcePath) {
    this.resourcePath = resourcePath;
  }

  public String getStage() {
    return stage;
  }

  public void setStage(String stage) {
    this.stage = stage;
  }
}
