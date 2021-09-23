package io.github.satsg.aws.lambda.runtime.spring.harness.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.reactive.HttpHandlerAutoConfiguration;
import org.springframework.boot.autoconfigure.web.reactive.WebFluxAutoConfiguration;
import org.springframework.boot.autoconfigure.web.reactive.error.ErrorWebFluxAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.WebFluxConfigurationSupport;

@ConditionalOnProperty(
    value = {
      "satsg.enable.aws.lambda.runtime.configuration",
      "satsg.enable.aws.lambda.runtime.reactive"
    })
@Configuration
public class EventHttpHandlerAutoConfiguration extends WebFluxConfigurationSupport {

  @Configuration
  public static class HandlerAutoConfiguration extends HttpHandlerAutoConfiguration {}

  @Configuration
  public static class ReactiveAutoConfiguration extends WebFluxAutoConfiguration {}

  @Configuration
  public static class ErrorAutoConfiguration extends ErrorWebFluxAutoConfiguration {
    public ErrorAutoConfiguration(ServerProperties serverProperties) {
      super(serverProperties);
    }
  }
}
