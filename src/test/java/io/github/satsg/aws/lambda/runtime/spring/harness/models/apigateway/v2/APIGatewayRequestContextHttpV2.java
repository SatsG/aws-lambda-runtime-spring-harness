package io.github.satsg.aws.lambda.runtime.spring.harness.models.apigateway.v2;

public class APIGatewayRequestContextHttpV2 {
  private String method;
  private String path;
  private String protocol;
  private String sourceIp;
  private String userAgent;

  public APIGatewayRequestContextHttpV2() {}

  public String getMethod() {
    return method;
  }

  public void setMethod(String method) {
    this.method = method;
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

  public String getSourceIp() {
    return sourceIp;
  }

  public void setSourceIp(String sourceIp) {
    this.sourceIp = sourceIp;
  }

  public String getUserAgent() {
    return userAgent;
  }

  public void setUserAgent(String userAgent) {
    this.userAgent = userAgent;
  }
}
