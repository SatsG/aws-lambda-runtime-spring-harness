package io.github.satsg.aws.lambda.runtime.spring.harness.config;

import io.github.satsg.aws.lambda.runtime.spring.harness.event.AWSEventHandler;
import io.github.satsg.aws.lambda.runtime.spring.harness.event.reactive.AWSReactiveEventHandler;
import io.github.satsg.aws.lambda.runtime.spring.harness.event.reactive.ReactiveEventMapper;
import io.github.satsg.aws.lambda.runtime.spring.harness.event.reactive.mappers.APIGatewayV1EventMapper;
import java.util.List;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.web.util.DefaultUriBuilderFactory;

@ConditionalOnProperty(
    value = {
      "satsg.enable.aws.lambda.runtime.configuration",
      "satsg.enable.aws.lambda.runtime.reactive"
    })
@Configuration
public class LambdaReactiveEventAutoConfiguration {

  @Bean
  @Order
  public ReactiveEventMapper apiGatewayV1EventMapper(DataBufferFactory factory) {
    return new APIGatewayV1EventMapper(factory, new DefaultUriBuilderFactory());
  }

  @Bean
  @ConditionalOnMissingBean(AWSEventHandler.class)
  public AWSEventHandler handler(List<ReactiveEventMapper> mappers, HttpHandler httpHandler) {
    return new AWSReactiveEventHandler(mappers, httpHandler);
  }
}
