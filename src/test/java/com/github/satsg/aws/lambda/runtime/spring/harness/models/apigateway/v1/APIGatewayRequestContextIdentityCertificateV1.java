package com.github.satsg.aws.lambda.runtime.spring.harness.models.apigateway.v1;

public class APIGatewayRequestContextIdentityCertificateV1 {
  private String clientCertPem;
  private String subjectDN;
  private String issuerDN;
  private String serialNumber;
  private APIGatewayRequestContextIdentityCertificateValidityV1 validity;

  public APIGatewayRequestContextIdentityCertificateV1() {}

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

  public APIGatewayRequestContextIdentityCertificateValidityV1 getValidity() {
    return validity;
  }

  public void setValidity(APIGatewayRequestContextIdentityCertificateValidityV1 validity) {
    this.validity = validity;
  }
}
