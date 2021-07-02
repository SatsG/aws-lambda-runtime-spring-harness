package com.github.satsg.aws.lambda.runtime.spring.harness.event.reactive;

import com.github.satsg.aws.lambda.runtime.spring.harness.event.AWSLambdaCustomResponse;
import org.springframework.http.server.reactive.ServerHttpResponse;

public interface ReactiveServerResponseMapper {
  AWSLambdaCustomResponse response(ServerHttpResponse response);
}
