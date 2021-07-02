package com.github.satsg.aws.lambda.runtime.spring.harness.config;

import com.github.satsg.aws.lambda.runtime.spring.harness.event.ServerlessEventLoop;
import com.github.satsg.aws.lambda.runtime.spring.harness.runner.EventLoopRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

@ConditionalOnProperty(value = "satsg.enable.aws.lambda.runtime")
@Configuration
public class EventRunnerAutoConfiguration {

  @Bean
  @Order(Ordered.LOWEST_PRECEDENCE - 10)
  public CommandLineRunner eventLoopRunner(ServerlessEventLoop eventLoop) {
    return new EventLoopRunner(eventLoop);
  }
}
