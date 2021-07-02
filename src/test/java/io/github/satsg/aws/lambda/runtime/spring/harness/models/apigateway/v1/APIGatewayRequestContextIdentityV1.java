package io.github.satsg.aws.lambda.runtime.spring.harness.models.apigateway.v1;

public class APIGatewayRequestContextIdentityV1 {
  private String accessKey;
  private String accountId;
  private String caller;
  private String cognitoAuthenticationProvider;
  private String cognitoAuthenticationType;
  private String cognitoIdentityId;
  private String cognitoIdentityPoolId;
  private String principalOrgId;
  private String sourceIp;
  private String user;
  private String userAgent;
  private String userArn;
  private APIGatewayRequestContextIdentityCertificateV1 clientCert;

  public APIGatewayRequestContextIdentityV1() {}

  public String getAccessKey() {
    return accessKey;
  }

  public void setAccessKey(String accessKey) {
    this.accessKey = accessKey;
  }

  public String getAccountId() {
    return accountId;
  }

  public void setAccountId(String accountId) {
    this.accountId = accountId;
  }

  public String getCaller() {
    return caller;
  }

  public void setCaller(String caller) {
    this.caller = caller;
  }

  public String getCognitoAuthenticationProvider() {
    return cognitoAuthenticationProvider;
  }

  public void setCognitoAuthenticationProvider(String cognitoAuthenticationProvider) {
    this.cognitoAuthenticationProvider = cognitoAuthenticationProvider;
  }

  public String getCognitoAuthenticationType() {
    return cognitoAuthenticationType;
  }

  public void setCognitoAuthenticationType(String cognitoAuthenticationType) {
    this.cognitoAuthenticationType = cognitoAuthenticationType;
  }

  public String getCognitoIdentityId() {
    return cognitoIdentityId;
  }

  public void setCognitoIdentityId(String cognitoIdentityId) {
    this.cognitoIdentityId = cognitoIdentityId;
  }

  public String getCognitoIdentityPoolId() {
    return cognitoIdentityPoolId;
  }

  public void setCognitoIdentityPoolId(String cognitoIdentityPoolId) {
    this.cognitoIdentityPoolId = cognitoIdentityPoolId;
  }

  public String getPrincipalOrgId() {
    return principalOrgId;
  }

  public void setPrincipalOrgId(String principalOrgId) {
    this.principalOrgId = principalOrgId;
  }

  public String getSourceIp() {
    return sourceIp;
  }

  public void setSourceIp(String sourceIp) {
    this.sourceIp = sourceIp;
  }

  public String getUser() {
    return user;
  }

  public void setUser(String user) {
    this.user = user;
  }

  public String getUserAgent() {
    return userAgent;
  }

  public void setUserAgent(String userAgent) {
    this.userAgent = userAgent;
  }

  public String getUserArn() {
    return userArn;
  }

  public void setUserArn(String userArn) {
    this.userArn = userArn;
  }

  public APIGatewayRequestContextIdentityCertificateV1 getClientCert() {
    return clientCert;
  }

  public void setClientCert(APIGatewayRequestContextIdentityCertificateV1 clientCert) {
    this.clientCert = clientCert;
  }
}
