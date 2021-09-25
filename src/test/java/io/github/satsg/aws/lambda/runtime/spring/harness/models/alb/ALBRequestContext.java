package io.github.satsg.aws.lambda.runtime.spring.harness.models.alb;

public class ALBRequestContext {
  private ALBRequestContextELB elb;

  public ALBRequestContext() {}

  public ALBRequestContextELB getElb() {
    return elb;
  }

  public void setElb(ALBRequestContextELB elb) {
    this.elb = elb;
  }
}
