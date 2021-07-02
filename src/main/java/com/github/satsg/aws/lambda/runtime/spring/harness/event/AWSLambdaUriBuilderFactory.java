package com.github.satsg.aws.lambda.runtime.spring.harness.event;

import java.util.Objects;
import org.springframework.web.util.DefaultUriBuilderFactory;

public class AWSLambdaUriBuilderFactory extends DefaultUriBuilderFactory {
  private static final String LAMBDA_RUNTIME_URL_PATTERN = "http://%s/%s";

  public AWSLambdaUriBuilderFactory(String runtimeHost, String version) {
    super(
        String.format(
            LAMBDA_RUNTIME_URL_PATTERN,
            Objects.requireNonNull(runtimeHost),
            Objects.requireNonNull(version)));
  }
}
