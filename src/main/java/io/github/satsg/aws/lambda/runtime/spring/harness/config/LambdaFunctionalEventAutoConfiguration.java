package io.github.satsg.aws.lambda.runtime.spring.harness.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.satsg.aws.lambda.runtime.spring.harness.event.AWSEventHandler;
import io.github.satsg.aws.lambda.runtime.spring.harness.event.functional.AWSFunctionalEventHandler;
import io.github.satsg.aws.lambda.runtime.spring.harness.event.functional.DefaultFunctionInputMapper;
import io.github.satsg.aws.lambda.runtime.spring.harness.event.functional.DefaultFunctionNameResolver;
import io.github.satsg.aws.lambda.runtime.spring.harness.event.functional.DefaultFunctionOutputMapper;
import io.github.satsg.aws.lambda.runtime.spring.harness.event.functional.FunctionInputMapper;
import io.github.satsg.aws.lambda.runtime.spring.harness.event.functional.FunctionNameResolver;
import io.github.satsg.aws.lambda.runtime.spring.harness.event.functional.FunctionOutputMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.function.context.FunctionCatalog;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@ConditionalOnProperty(
    value = {
      "satsg.enable.aws.lambda.runtime.configuration",
      "satsg.enable.aws.lambda.runtime.functional"
    })
@Configuration
public class LambdaFunctionalEventAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean(FunctionNameResolver.class)
  public FunctionNameResolver resolver(Environment environment) {
    return new DefaultFunctionNameResolver(environment);
  }

  @Bean
  @ConditionalOnMissingBean(FunctionInputMapper.class)
  public FunctionInputMapper inputMapper(ObjectMapper mapper) {
    return new DefaultFunctionInputMapper(mapper);
  }

  @Bean
  @ConditionalOnMissingBean(FunctionOutputMapper.class)
  public FunctionOutputMapper outputMapper() {
    return new DefaultFunctionOutputMapper();
  }

  @Bean
  @ConditionalOnMissingBean(AWSEventHandler.class)
  public AWSEventHandler handler(
      FunctionCatalog catalog,
      FunctionNameResolver resolver,
      FunctionInputMapper inputMapper,
      FunctionOutputMapper outputMapper) {
    return new AWSFunctionalEventHandler(catalog, resolver, inputMapper, outputMapper);
  }
}
