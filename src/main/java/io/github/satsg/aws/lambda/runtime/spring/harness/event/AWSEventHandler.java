package io.github.satsg.aws.lambda.runtime.spring.harness.event;

public interface AWSEventHandler {
  Object handle(Object event);
}
