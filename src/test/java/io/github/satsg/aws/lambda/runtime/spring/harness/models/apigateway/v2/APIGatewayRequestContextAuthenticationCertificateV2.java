package io.github.satsg.aws.lambda.runtime.spring.harness.models.apigateway.v2;

public class APIGatewayRequestContextAuthenticationCertificateV2 {
  private String clientCertPem;
  private String subjectDN;
  private String issuerDN;
  private String serialNumber;
  private APIGatewayRequestContextAuthenticationCertificateValidityV2 validity;

  public APIGatewayRequestContextAuthenticationCertificateV2() {}

  public String getClientCertPem() {
    return clientCertPem;
  }

  public void setClientCertPem(String clientCertPem) {
    this.clientCertPem = clientCertPem;
  }

  public String getSubjectDN() {
    return subjectDN;
  }

  public void setSubjectDN(String subjectDN) {
    this.subjectDN = subjectDN;
  }

  public String getIssuerDN() {
    return issuerDN;
  }

  public void setIssuerDN(String issuerDN) {
    this.issuerDN = issuerDN;
  }

  public String getSerialNumber() {
    return serialNumber;
  }

  public void setSerialNumber(String serialNumber) {
    this.serialNumber = serialNumber;
  }

  public APIGatewayRequestContextAuthenticationCertificateValidityV2 getValidity() {
    return validity;
  }

  public void setValidity(APIGatewayRequestContextAuthenticationCertificateValidityV2 validity) {
    this.validity = validity;
  }
}
