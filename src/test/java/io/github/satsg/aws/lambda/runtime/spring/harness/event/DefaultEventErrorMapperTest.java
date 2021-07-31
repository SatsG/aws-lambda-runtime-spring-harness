package io.github.satsg.aws.lambda.runtime.spring.harness.event;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

class DefaultEventErrorMapperTest {

  private static final DefaultEventErrorMapper MAPPER = new DefaultEventErrorMapper();

  @Test
  void createResponseCorrectly() {
    RuntimeException exception = new RuntimeException("Whoops!");
    AWSLambdaErrorResponse response = MAPPER.error(exception);

    assertThat(response.getErrorMessage()).isEqualTo("Whoops!");
    assertThat(response.getErrorType()).isEqualTo("Runtime.UnexpectedError");
    assertThat(response.getStackTrace())
        .isEqualTo(
            Stream.of(exception.getStackTrace())
                .map(StackTraceElement::toString)
                .collect(Collectors.toList()));
  }
}
