package com.github.satsg.aws.lambda.runtime.spring.harness.models.apigateway.v2;

public class APIGatewayRequestContextAuthenticationCertificateValidityV2 {
  private String notBefore;
  private String notAfter;

  public APIGatewayRequestContextAuthenticationCertificateValidityV2() {}

  public String getNotBefore() {
    return notBefore;
  }

  public void setNotBefore(String notBefore) {
    this.notBefore = notBefore;
  }

  public String getNotAfter() {
    return notAfter;
  }

  public void setNotAfter(String notAfter) {
    this.notAfter = notAfter;
  }
}
