package io.github.satsg.aws.lambda.runtime.spring.harness.models.apigateway.v1;

public class APIGatewayRequestContextIdentityCertificateValidityV1 {
  private String notBefore;
  private String notAfter;

  public APIGatewayRequestContextIdentityCertificateValidityV1() {}

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
