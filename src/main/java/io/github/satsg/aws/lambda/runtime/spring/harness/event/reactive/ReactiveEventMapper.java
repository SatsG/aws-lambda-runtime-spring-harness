package io.github.satsg.aws.lambda.runtime.spring.harness.event.reactive;

import io.github.satsg.aws.lambda.runtime.spring.harness.event.EventMatcher;
import java.util.List;
import java.util.Map;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;

public interface ReactiveEventMapper extends EventMatcher {
  ServerHttpRequest compose(Object event, Map<String, List<String>> headers);

  ServerHttpResponse create();

  Object respond(ServerHttpResponse response);
}
