package io.github.satsg.aws.lambda.runtime.spring.harness.event;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class AWSLambdaUriBuilderFactoryTest {

  private static final AWSLambdaUriBuilderFactory FACTORY =
      new AWSLambdaUriBuilderFactory("host", "version");

  @Test
  void urlIsCorrect() {
    assertThat(FACTORY.builder().build().toString()).isEqualTo("http://host/version");
  }
}
