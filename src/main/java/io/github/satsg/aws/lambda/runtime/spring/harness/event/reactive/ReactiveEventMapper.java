package io.github.satsg.aws.lambda.runtime.spring.harness.event.reactive;

import io.github.satsg.aws.lambda.runtime.spring.harness.event.EventMatcher;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;

public interface ReactiveEventMapper extends EventMatcher {
  ServerHttpRequest compose(Object event);

  ServerHttpResponse create();

  Object respond(ServerHttpResponse response);
}
