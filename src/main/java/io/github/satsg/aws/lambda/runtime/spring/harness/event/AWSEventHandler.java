package io.github.satsg.aws.lambda.runtime.spring.harness.event;

public interface AWSEventHandler {
  AWSLambdaCustomResponse handle(Object event);
}
