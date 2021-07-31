package io.github.satsg.aws.lambda.runtime.spring.harness.config;

import io.github.satsg.aws.lambda.runtime.spring.harness.event.LoopCondition;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestConfig {

  @Bean
  public LoopCondition condition() {
    return new BoundedLoop(1);
  }

  public static class BoundedLoop implements LoopCondition {

    private int loops = 0;
    private final int bound;

    public BoundedLoop(int bound) {
      assert bound >= 0;
      this.bound = bound;
    }

    @Override
    public boolean shouldContinue() {
      return loops++ < bound;
    }
  }
}
