package io.github.satsg.aws.lambda.runtime.spring.harness.config;

import io.github.satsg.aws.lambda.runtime.spring.harness.event.LoopCondition;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestConfig {

  public static final String EVENT_LOOP_CONDITION_PROP = "event.loop.continue";

  @Bean
  public LoopCondition condition() {
    return () -> Boolean.parseBoolean(System.getProperty(EVENT_LOOP_CONDITION_PROP));
  }
}
