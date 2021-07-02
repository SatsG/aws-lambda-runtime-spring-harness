package com.github.satsg.aws.lambda.runtime.spring.harness.event.reactive;

import com.github.satsg.aws.lambda.runtime.spring.harness.event.EventMatcher;
import org.springframework.http.server.reactive.ServerHttpRequest;

public interface ReactiveEventMapper extends EventMatcher {
  ServerHttpRequest compose(Object event);
}
