package com.github.satsg.aws.lambda.runtime.spring.harness.event;

public interface EventMatcher {
  boolean matches(Object event);
}
