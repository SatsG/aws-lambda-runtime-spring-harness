package com.github.satsg.aws.lambda.runtime.spring.harness.event;

public interface EventErrorMapper {
  AWSLambdaErrorResponse error(Throwable cause);
}
