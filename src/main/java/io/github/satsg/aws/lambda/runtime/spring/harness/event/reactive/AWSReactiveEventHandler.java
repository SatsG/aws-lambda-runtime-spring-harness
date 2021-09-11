package io.github.satsg.aws.lambda.runtime.spring.harness.event.reactive;

import io.github.satsg.aws.lambda.runtime.spring.harness.event.AWSEventHandler;
import java.util.List;
import java.util.Objects;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;

public class AWSReactiveEventHandler implements AWSEventHandler {

  private final List<ReactiveEventMapper> mappers;
  private final HttpHandler httpHandler;

  public AWSReactiveEventHandler(List<ReactiveEventMapper> mappers, HttpHandler httpHandler) {
    this.mappers = Objects.requireNonNull(mappers);
    this.httpHandler = Objects.requireNonNull(httpHandler);
  }

  @Override
  public Object handle(Object event) {
    ReactiveEventMapper mapper = resolve(event);
    ServerHttpRequest request = mapper.compose(event);
    ServerHttpResponse response = mapper.create();
    httpHandler.handle(request, response).block();
    return mapper.respond(response);
  }

  private ReactiveEventMapper resolve(Object event) {
    return mappers.stream()
        .filter(mapper -> mapper.matches(event))
        .findFirst()
        .orElseThrow(
            () -> new IllegalArgumentException("Event doesn't match any supported mappings."));
  }
}
