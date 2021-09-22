package io.github.satsg.aws.lambda.runtime.spring.harness.models;

public class FunctionInput {
  private String boop;

  public FunctionInput() {}

  public FunctionInput(String boop) {
    this.boop = boop;
  }

  public String getBoop() {
    return boop;
  }

  public void setBoop() {
    this.boop = boop;
  }
}
