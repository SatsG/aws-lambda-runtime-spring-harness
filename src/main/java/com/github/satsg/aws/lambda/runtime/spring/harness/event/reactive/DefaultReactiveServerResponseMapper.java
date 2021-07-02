package com.github.satsg.aws.lambda.runtime.spring.harness.event.reactive;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.satsg.aws.lambda.runtime.spring.harness.event.AWSLambdaCustomResponse;
import java.util.Objects;
import org.springframework.http.server.reactive.ServerHttpResponse;

public class DefaultReactiveServerResponseMapper implements ReactiveServerResponseMapper {

  private final ObjectMapper mapper;

  public DefaultReactiveServerResponseMapper(ObjectMapper mapper) {
    this.mapper = Objects.requireNonNull(mapper);
  }

  @Override
  public AWSLambdaCustomResponse response(ServerHttpResponse response) {
    try {
      ReactiveEventServerHttpResponse result = (ReactiveEventServerHttpResponse) response;
      AWSLambdaCustomResponse awsResponse = new AWSLambdaCustomResponse();
      awsResponse.setStatusCode(result.getStatusCode().value());
      awsResponse.setBody(
          result.getBody() != null ? mapper.readValue(result.getBody(), Object.class) : null);
      awsResponse.setHeaders(result.getHeaders().toSingleValueMap());
      awsResponse.setIsBase64Encoded(false);
      return awsResponse;
    } catch (Exception e) {
      throw new IllegalStateException("Unable to convert server response to lambda response.", e);
    }
  }
}
