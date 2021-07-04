package io.github.satsg.aws.lambda.runtime.spring.harness.config;

import java.util.Objects;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.web.reactive.config.WebFluxConfigurationSupport;
import org.springframework.web.server.adapter.WebHttpHandlerBuilder;

@ConditionalOnProperty(
    value = {
      "satsg.enable.aws.lambda.runtime.configuration",
      "satsg.enable.aws.lambda.runtime.reactive"
    })
@Configuration
public class EventHttpHandlerAutoConfiguration extends WebFluxConfigurationSupport {

  @Bean
  @ConditionalOnMissingBean(HttpHandler.class)
  public HttpHandler httpHandler() {
    return WebHttpHandlerBuilder.applicationContext(Objects.requireNonNull(getApplicationContext()))
        .build();
  }
}
