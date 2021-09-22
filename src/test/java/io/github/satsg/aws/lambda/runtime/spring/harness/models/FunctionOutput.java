package io.github.satsg.aws.lambda.runtime.spring.harness.models;

public class FunctionOutput {
  private String soop;

  public FunctionOutput() {}

  public FunctionOutput(String soop) {
    this.soop = soop;
  }

  public String getSoop() {
    return soop;
  }

  public void setSoop(String soop) {
    this.soop = soop;
  }
}
