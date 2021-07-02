package io.github.satsg.aws.lambda.runtime.spring.harness.models.apigateway.v1;

import java.util.List;
import java.util.Map;

public class APIGatewayRequestContextAuthorizerV1 {
  private Map<String, String> claims;
  private List<String> scopes;

  public APIGatewayRequestContextAuthorizerV1() {}

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
