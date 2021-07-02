package com.github.satsg.aws.lambda.runtime.spring.harness.event.reactive;

import java.util.List;
import java.util.Objects;
import org.springframework.http.server.reactive.ServerHttpRequest;

public class AWSReactiveEventResolver implements ReactiveEventResolver {

  private final List<ReactiveEventMapper> mappers;

  public AWSReactiveEventResolver(List<ReactiveEventMapper> mappers) {
    this.mappers = Objects.requireNonNull(mappers);
  }

  @Override
  public ServerHttpRequest resolve(Object event) {
    return mappers.stream()
        .filter(mapper -> mapper.matches(event))
        .findFirst()
        .map(mapper -> mapper.compose(event))
        .orElseThrow(
            () -> new IllegalArgumentException("Event doesn't match any supported mappings."));
  }
}
