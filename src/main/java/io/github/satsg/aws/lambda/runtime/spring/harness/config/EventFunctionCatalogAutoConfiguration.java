package io.github.satsg.aws.lambda.runtime.spring.harness.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.function.context.config.ContextFunctionCatalogAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@ConditionalOnProperty(
    value = {
      "satsg.enable.aws.lambda.runtime.configuration",
      "satsg.enable.aws.lambda.runtime.functional"
    })
@Configuration
@Import(ContextFunctionCatalogAutoConfiguration.class)
public class EventFunctionCatalogAutoConfiguration {}
