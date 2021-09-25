package io.github.satsg.aws.lambda.runtime.spring.harness.models.alb;

public class ALBRequestContextELB {
  private String targetGroupArn;

  public ALBRequestContextELB() {}

  public String getTargetGroupArn() {
    return targetGroupArn;
  }

  public void setTargetGroupArn(String targetGroupArn) {
    this.targetGroupArn = targetGroupArn;
  }
}
