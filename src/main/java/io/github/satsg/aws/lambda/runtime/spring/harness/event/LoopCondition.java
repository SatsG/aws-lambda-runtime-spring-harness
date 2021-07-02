package io.github.satsg.aws.lambda.runtime.spring.harness.event;

public interface LoopCondition {
  boolean shouldContinue();
}
