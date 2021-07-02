package com.github.satsg.aws.lambda.runtime.spring.harness.event.reactive;

import java.util.Objects;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.server.reactive.ServerHttpResponse;

public class DefaultReactiveServerResponseCreator implements ReactiveServerResponseCreator {

  private final DataBufferFactory factory;

  public DefaultReactiveServerResponseCreator(DataBufferFactory factory) {
    this.factory = Objects.requireNonNull(factory);
  }

  @Override
  public ServerHttpResponse create() {
    return new ReactiveEventServerHttpResponse(factory);
  }
}
