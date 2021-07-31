package io.github.satsg.aws.lambda.runtime.spring.harness.event.reactive;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;

class DefaultReactiveServerResponseCreatorTest {

  private static final DefaultReactiveServerResponseCreator CREATOR =
      new DefaultReactiveServerResponseCreator(new DefaultDataBufferFactory());

  @Test
  void isReactiveEventServerHttpResponse() {
    assertThat(CREATOR.create()).isInstanceOf(ReactiveEventServerHttpResponse.class);
  }
}
