package io.github.satsg.aws.lambda.runtime.spring.harness.config;

import io.github.satsg.aws.lambda.runtime.spring.harness.event.AWSEventHandler;
import io.github.satsg.aws.lambda.runtime.spring.harness.event.mappers.APIGatewayV1EventMapper;
import io.github.satsg.aws.lambda.runtime.spring.harness.event.reactive.AWSReactiveEventHandler;
import io.github.satsg.aws.lambda.runtime.spring.harness.event.reactive.AWSReactiveEventResolver;
import io.github.satsg.aws.lambda.runtime.spring.harness.event.reactive.DefaultReactiveServerResponseCreator;
import io.github.satsg.aws.lambda.runtime.spring.harness.event.reactive.DefaultReactiveServerResponseMapper;
import io.github.satsg.aws.lambda.runtime.spring.harness.event.reactive.ReactiveEventMapper;
import io.github.satsg.aws.lambda.runtime.spring.harness.event.reactive.ReactiveEventResolver;
import io.github.satsg.aws.lambda.runtime.spring.harness.event.reactive.ReactiveServerResponseCreator;
import io.github.satsg.aws.lambda.runtime.spring.harness.event.reactive.ReactiveServerResponseMapper;
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
  @ConditionalOnMissingBean(ReactiveEventResolver.class)
  public ReactiveEventResolver resolver(List<ReactiveEventMapper> mappers) {
    return new AWSReactiveEventResolver(mappers);
  }

  @Bean
  @ConditionalOnMissingBean(ReactiveServerResponseCreator.class)
  public ReactiveServerResponseCreator creator(DataBufferFactory factory) {
    return new DefaultReactiveServerResponseCreator(factory);
  }

  @Bean
  @ConditionalOnMissingBean(ReactiveServerResponseMapper.class)
  public ReactiveServerResponseMapper mapper() {
    return new DefaultReactiveServerResponseMapper();
  }

  @Bean
  @ConditionalOnMissingBean(AWSEventHandler.class)
  public AWSEventHandler handler(
      ReactiveEventResolver resolver,
      ReactiveServerResponseCreator creator,
      HttpHandler httpHandler,
      ReactiveServerResponseMapper mapper) {
    return new AWSReactiveEventHandler(resolver, creator, httpHandler, mapper);
  }
}
