package com.github.satsg.aws.lambda.runtime.spring.harness.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.web.reactive.DispatcherHandler;
import org.springframework.web.server.adapter.WebHttpHandlerBuilder;

@ConditionalOnProperty(
    value = {
      "satsg.enable.aws.lambda.runtime.configuration",
      "satsg.enable.aws.lambda.runtime.reactive"
    })
@Configuration
public class EventHttpHandlerAutoConfiguration {

  private final ApplicationContext applicationContext;

  public EventHttpHandlerAutoConfiguration(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }

  @Bean
  @ConditionalOnMissingBean(HttpHandler.class)
  public HttpHandler httpHandler() {
    return WebHttpHandlerBuilder.applicationContext(this.applicationContext).build();
  }

  @Bean
  @ConditionalOnMissingBean(DispatcherHandler.class)
  public DispatcherHandler webHandler() {
    return new DispatcherHandler();
  }
}
