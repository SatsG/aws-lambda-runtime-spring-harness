package io.github.satsg.aws.lambda.runtime.spring.harness.models.apigateway.v2;

public class APIGatewayRequestContextAuthorizerV2 {
  private APIGatewayRequestContextAuthorizerJwtV2 jwt;

  public APIGatewayRequestContextAuthorizerV2() {}

  public APIGatewayRequestContextAuthorizerJwtV2 getJwt() {
    return jwt;
  }

  public void setJwt(APIGatewayRequestContextAuthorizerJwtV2 jwt) {
    this.jwt = jwt;
  }
}
