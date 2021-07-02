package com.github.satsg.aws.lambda.runtime.spring.harness.models.apigateway.v2;

public class APIGatewayRequestContextAuthenticationV2 {
  private APIGatewayRequestContextAuthenticationCertificateV2 clientCert;

  public APIGatewayRequestContextAuthenticationV2() {}

  public APIGatewayRequestContextAuthenticationCertificateV2 getClientCert() {
    return clientCert;
  }

  public void setClientCert(APIGatewayRequestContextAuthenticationCertificateV2 clientCert) {
    this.clientCert = clientCert;
  }
}
