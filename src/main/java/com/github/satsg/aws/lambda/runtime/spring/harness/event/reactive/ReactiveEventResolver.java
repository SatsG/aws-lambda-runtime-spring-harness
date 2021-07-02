package com.github.satsg.aws.lambda.runtime.spring.harness.event.reactive;

import org.springframework.http.server.reactive.ServerHttpRequest;

public interface ReactiveEventResolver {
  ServerHttpRequest resolve(Object event);
}
