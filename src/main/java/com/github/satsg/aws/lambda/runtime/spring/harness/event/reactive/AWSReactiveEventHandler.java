package com.github.satsg.aws.lambda.runtime.spring.harness.event.reactive;

import com.github.satsg.aws.lambda.runtime.spring.harness.event.AWSEventHandler;
import com.github.satsg.aws.lambda.runtime.spring.harness.event.AWSLambdaCustomResponse;
import java.util.Objects;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;

public class AWSReactiveEventHandler implements AWSEventHandler {

  private final ReactiveEventResolver resolver;
  private final ReactiveServerResponseCreator creator;
  private final HttpHandler httpHandler;
  private final ReactiveServerResponseMapper mapper;

  public AWSReactiveEventHandler(
      ReactiveEventResolver resolver,
      ReactiveServerResponseCreator creator,
      HttpHandler httpHandler,
      ReactiveServerResponseMapper mapper) {
    this.resolver = Objects.requireNonNull(resolver);
    this.creator = Objects.requireNonNull(creator);
    this.httpHandler = Objects.requireNonNull(httpHandler);
    this.mapper = Objects.requireNonNull(mapper);
  }

  @Override
  public AWSLambdaCustomResponse handle(Object event) {
    ServerHttpRequest request = resolver.resolve(event);
    ServerHttpResponse response = creator.create();
    httpHandler.handle(request, response).block();
    return mapper.response(response);
  }
}
