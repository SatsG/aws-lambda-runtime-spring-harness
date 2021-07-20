package io.github.satsg.aws.lambda.runtime.spring.harness.event.reactive;

import io.github.satsg.aws.lambda.runtime.spring.harness.event.AWSLambdaCustomResponse;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import org.springframework.http.server.reactive.ServerHttpResponse;

public class DefaultReactiveServerResponseMapper implements ReactiveServerResponseMapper {

  @Override
  public AWSLambdaCustomResponse response(ServerHttpResponse response) {
    try {
      ReactiveEventServerHttpResponse result = (ReactiveEventServerHttpResponse) response;
      AWSLambdaCustomResponse awsResponse = new AWSLambdaCustomResponse();
      awsResponse.setStatusCode(result.getStatusCode().value());
      if (result.getBody() != null) {
        ByteBuffer body = result.getBody().blockLast();
        if (body != null) {
          awsResponse.setBody(new String(body.array(), StandardCharsets.UTF_8));
        }
      }
      awsResponse.setHeaders(result.getHeaders().toSingleValueMap());
      awsResponse.setIsBase64Encoded(false);
      return awsResponse;
    } catch (Exception e) {
      throw new IllegalStateException("Unable to convert server response to lambda response.", e);
    }
  }
}
