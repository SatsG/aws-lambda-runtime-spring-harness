package io.github.satsg.aws.lambda.runtime.spring.harness.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.satsg.aws.lambda.runtime.spring.harness.event.AWSEventHandler;
import io.github.satsg.aws.lambda.runtime.spring.harness.event.AWSLambdaEventLoop;
import io.github.satsg.aws.lambda.runtime.spring.harness.event.AWSLambdaRuntime;
import io.github.satsg.aws.lambda.runtime.spring.harness.event.AWSLambdaUriBuilderFactory;
import io.github.satsg.aws.lambda.runtime.spring.harness.event.DefaultEventErrorMapper;
import io.github.satsg.aws.lambda.runtime.spring.harness.event.EventErrorMapper;
import io.github.satsg.aws.lambda.runtime.spring.harness.event.LoopCondition;
import io.github.satsg.aws.lambda.runtime.spring.harness.event.ServerlessEventLoop;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;

@ConditionalOnProperty(value = "satsg.enable.aws.lambda.runtime.configuration")
@Configuration
public class LambdaEventLoopAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean(DataBufferFactory.class)
  public DataBufferFactory factory() {
    return new DefaultDataBufferFactory();
  }

  @Bean
  @ConditionalOnMissingBean(AWSLambdaUriBuilderFactory.class)
  public AWSLambdaUriBuilderFactory awsLambdaUriBuilderFactory(
      @Value("${AWS_LAMBDA_RUNTIME_API}") String runtime,
      @Value("${satsg.aws.lambda.runtime.version}") String version) {
    return new AWSLambdaUriBuilderFactory(runtime, version);
  }

  @Bean
  @ConditionalOnMissingBean(AWSLambdaRuntime.class)
  public AWSLambdaRuntime runtime(AWSLambdaUriBuilderFactory awsLambdaUriBuilderFactory) {
    return new AWSLambdaRuntime(new RestTemplateBuilder().build(), awsLambdaUriBuilderFactory);
  }

  @Bean
  @ConditionalOnMissingBean(EventErrorMapper.class)
  public EventErrorMapper eventErrorMapper() {
    return new DefaultEventErrorMapper();
  }

  @Bean
  @ConditionalOnMissingBean(LoopCondition.class)
  public LoopCondition condition() {
    return () -> true;
  }

  @Bean
  @ConditionalOnMissingBean(ServerlessEventLoop.class)
  public ServerlessEventLoop eventLoop(
      AWSLambdaRuntime runtime,
      AWSEventHandler handler,
      EventErrorMapper eventErrorMapper,
      LoopCondition condition,
      ObjectMapper mapper) {
    return new AWSLambdaEventLoop(runtime, handler, eventErrorMapper, condition, mapper);
  }
}
