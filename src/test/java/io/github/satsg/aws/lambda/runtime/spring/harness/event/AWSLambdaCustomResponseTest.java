package io.github.satsg.aws.lambda.runtime.spring.harness.event;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class AWSLambdaCustomResponseTest {

  private static final AWSLambdaCustomResponse RESPONSE = new AWSLambdaCustomResponse();

  @Test
  void statusIsOk() {
    assertThat(RESPONSE.getStatusCode()).isEqualTo(200);
  }

  @Test
  void headersIsNull() {
    assertThat(RESPONSE.getHeaders()).isNull();
  }

  @Test
  void bodyIsNull() {
    assertThat(RESPONSE.getBody()).isNull();
  }

  @Test
  void base64IsFalse() {
    assertThat(RESPONSE.getIsBase64Encoded()).isFalse();
  }
}
