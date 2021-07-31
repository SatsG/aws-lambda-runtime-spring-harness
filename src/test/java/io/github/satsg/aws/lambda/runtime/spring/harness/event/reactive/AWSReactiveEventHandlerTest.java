package io.github.satsg.aws.lambda.runtime.spring.harness.event.reactive;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import io.github.satsg.aws.lambda.runtime.spring.harness.event.AWSLambdaCustomResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
class AWSReactiveEventHandlerTest {

  @Mock private ServerHttpRequest request;
  @Mock private ServerHttpResponse response;
  @Mock private AWSLambdaCustomResponse awsResponse;

  @Mock private ReactiveEventResolver resolver;
  @Mock private ReactiveServerResponseCreator creator;
  @Mock private HttpHandler httpHandler;
  @Mock private ReactiveServerResponseMapper mapper;

  @InjectMocks private AWSReactiveEventHandler handler;

  @Test
  void resolverIsUsed() {
    callHandler(request, response, awsResponse);
    verify(resolver).resolve(any());
  }

  @Test
  void creatorIsUsed() {
    callHandler(request, response, awsResponse);
    verify(creator).create();
  }

  @Test
  void httpHandlerIsUsed() {
    callHandler(request, response, awsResponse);
    verify(httpHandler).handle(request, response);
  }

  @Test
  void mapperIsUsed() {
    callHandler(request, response, awsResponse);
    verify(mapper).response(response);
  }

  private void callHandler(
      ServerHttpRequest request, ServerHttpResponse response, AWSLambdaCustomResponse awsResponse) {
    given(resolver.resolve(any())).willReturn(request);
    given(creator.create()).willReturn(response);
    given(httpHandler.handle(request, response)).willReturn(Mono.empty());
    given(mapper.response(response)).willReturn(awsResponse);

    handler.handle(new Object());
  }
}
