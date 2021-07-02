package io.github.satsg.aws.lambda.runtime.spring.harness.models.apigateway.v2;

import java.util.List;
import java.util.Map;

public class APIGatewayRequestContextAuthorizerJwtV2 {
  private Map<String, String> claims;
  private List<String> scopes;

  public APIGatewayRequestContextAuthorizerJwtV2() {}

  public Map<String, String> getClaims() {
    return claims;
  }

  public void setClaims(Map<String, String> claims) {
    this.claims = claims;
  }

  public List<String> getScopes() {
    return scopes;
  }

  public void setScopes(List<String> scopes) {
    this.scopes = scopes;
  }
}
