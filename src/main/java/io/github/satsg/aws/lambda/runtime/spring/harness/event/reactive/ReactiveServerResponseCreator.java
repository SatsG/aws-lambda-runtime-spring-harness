package io.github.satsg.aws.lambda.runtime.spring.harness.event.reactive;

import org.springframework.http.server.reactive.ServerHttpResponse;

public interface ReactiveServerResponseCreator {
  ServerHttpResponse create();
}
