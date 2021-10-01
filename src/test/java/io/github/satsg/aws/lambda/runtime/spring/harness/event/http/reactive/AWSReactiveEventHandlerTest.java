package io.github.satsg.aws.lambda.runtime.spring.harness.event.http.reactive;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
class AWSReactiveEventHandlerTest {

  @Mock private ReactiveEventMapper mapper1;
  @Mock private ReactiveEventMapper mapper2;
  @Mock private ServerHttpRequest request;
  @Mock private ServerHttpResponse response;

  @Mock private HttpHandler httpHandler;

  private AWSReactiveEventHandler handler;

  @BeforeEach
  void setUpEach() {
    handler = new AWSReactiveEventHandler(Arrays.asList(mapper1, mapper2), httpHandler);
  }

  @Test
  void correctResolverIsUsed() {
    callHandler(request, response);
    verify(mapper1, never()).compose(any(), anyMap());
    verify(mapper2).compose(any(), anyMap());
  }

  @Test
  void correctCreateIsUsed() {
    callHandler(request, response);
    verify(mapper1, never()).create();
    verify(mapper2).create();
  }

  @Test
  void httpHandlerIsUsed() {
    callHandler(request, response);
    verify(httpHandler).handle(request, response);
  }

  @Test
  void throwExceptionWhenNoMatches() {
    given(mapper1.matches(any())).willReturn(false);
    given(mapper2.matches(any())).willReturn(false);

    assertThatThrownBy(() -> handler.handle(new Object(), Collections.emptyMap()))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Event doesn't match any supported mappings.");
  }

  private void callHandler(ServerHttpRequest request, ServerHttpResponse response) {
    given(mapper1.matches(any())).willReturn(false);
    given(mapper2.matches(any())).willReturn(true);
    given(mapper2.compose(any(), anyMap())).willReturn(request);
    given(mapper2.respond(response)).willReturn(new Object());
    given(mapper2.create()).willReturn(response);
    given(httpHandler.handle(request, response)).willReturn(Mono.empty());

    handler.handle(new Object(), Collections.emptyMap());
  }
}
