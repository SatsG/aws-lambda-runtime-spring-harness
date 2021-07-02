package com.github.satsg.aws.lambda.runtime.spring.harness.event;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DefaultEventErrorMapper implements EventErrorMapper {
  @Override
  public AWSLambdaErrorResponse error(Throwable cause) {
    return new AWSLambdaErrorResponse(
        cause.getMessage(),
        "Runtime.UnexpectedError",
        Stream.of(cause.getStackTrace())
            .map(StackTraceElement::toString)
            .collect(Collectors.toList()));
  }
}
