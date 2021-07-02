package io.github.satsg.aws.lambda.runtime.spring.harness.event.reactive;

import io.github.satsg.aws.lambda.runtime.spring.harness.event.EventMatcher;
import org.springframework.http.server.reactive.ServerHttpRequest;

public interface ReactiveEventMapper extends EventMatcher {
  ServerHttpRequest compose(Object event);
}
