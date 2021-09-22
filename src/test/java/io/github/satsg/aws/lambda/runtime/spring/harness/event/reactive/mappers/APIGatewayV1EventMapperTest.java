package io.github.satsg.aws.lambda.runtime.spring.harness.event.reactive.mappers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.satsg.aws.lambda.runtime.spring.harness.event.AWSLambdaCustomResponse;
import io.github.satsg.aws.lambda.runtime.spring.harness.event.reactive.ReactiveEventServerHttpRequest;
import io.github.satsg.aws.lambda.runtime.spring.harness.event.reactive.ReactiveEventServerHttpResponse;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;
import org.assertj.core.data.MapEntry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.RequestPath;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.MultiValueMapAdapter;
import org.springframework.web.util.DefaultUriBuilderFactory;
import reactor.core.publisher.Flux;

@ExtendWith(MockitoExtension.class)
class APIGatewayV1EventMapperTest {

  private static final APIGatewayV1EventMapper EVENT_MAPPER =
      new APIGatewayV1EventMapper(new DefaultDataBufferFactory(), new DefaultUriBuilderFactory());

  @Nested
  class MatchesTest {

    @Test
    void doesNotMatchIfEventIsNotMapType() {
      assertThat(EVENT_MAPPER.matches(new ArrayList<>())).isFalse();
    }

    @Test
    void matchesCorrectly() {
      Map<String, Object> event = minimalMatch();
      assertThat(EVENT_MAPPER.matches(event)).isTrue();
    }

    @Test
    void matchesWithoutVersionCorrectly() {
      Map<String, Object> event = minimalMatch();
      event.remove("version");
      assertThat(EVENT_MAPPER.matches(event)).isTrue();
    }

    @ParameterizedTest
    @ArgumentsSource(InvalidMatches.class)
    void incorrectMatchesFails(Map<String, Object> event) {
      assertThat(EVENT_MAPPER.matches(event)).isFalse();
    }
  }

  @Nested
  class ComposeTest {

    @Test
    void requestIsDefaultReactiveType() {
      ServerHttpRequest request = EVENT_MAPPER.compose(minimalEvent(), Map.of());
      assertThat(request).isInstanceOf(ReactiveEventServerHttpRequest.class);
    }

    @Test
    void methodIsSetCorrectly() {
      Map<String, Object> event = minimalEvent();
      event.put("httpMethod", "POST");
      ServerHttpRequest request = EVENT_MAPPER.compose(event, Map.of());
      assertThat(request.getMethod()).isEqualTo(HttpMethod.POST);
    }

    @Test
    void pathIsSetCorrectly() {
      Map<String, Object> event = minimalEvent();
      event.put("path", "/path");
      ServerHttpRequest request = EVENT_MAPPER.compose(event, Map.of());
      assertThat(request.getPath()).isEqualTo(RequestPath.parse("/path", ""));
    }

    @Test
    void queriesAreAddedCorrectly() {
      Map<String, Object> event = minimalEvent();
      event.put(
          "multiValueQueryStringParameters",
          new HashMap<>(
              Map.of(
                  "q1",
                  Collections.singletonList("value1"),
                  "q2",
                  Collections.singletonList(""),
                  "q3",
                  Collections.singletonList(null),
                  "q4",
                  Arrays.asList("value1", "value2"))));

      ServerHttpRequest request = EVENT_MAPPER.compose(event, Map.of());

      assertThat(request.getQueryParams()).containsKey("q1");
      assertThat(request.getQueryParams().get("q1")).containsExactly("value1");
      assertThat(request.getQueryParams()).containsKey("q2");
      assertThat(request.getQueryParams().get("q2")).containsExactly("");
      assertThat(request.getQueryParams()).containsKey("q3");
      assertThat(request.getQueryParams().get("q3")).containsExactly("");
      assertThat(request.getQueryParams()).containsKey("q4");
      assertThat(request.getQueryParams().get("q4")).containsExactly("value1", "value2");
    }

    @Test
    void headersAreAddedCorrectly() {
      Map<String, Object> event = minimalEvent();
      event.put(
          "multiValueHeaders",
          new HashMap<>(
              Map.of(
                  "h1",
                  Collections.singletonList("value1"),
                  "h2",
                  Collections.singletonList(""),
                  "h3",
                  Collections.singletonList(null),
                  "h4",
                  Arrays.asList("value1", "value2"))));

      ServerHttpRequest request = EVENT_MAPPER.compose(event, Map.of());

      assertThat(request.getHeaders()).containsKey("h1");
      assertThat(request.getHeaders().get("h1")).containsExactly("value1");
      assertThat(request.getHeaders()).containsKey("h2");
      assertThat(request.getHeaders().get("h2")).containsExactly("");
      assertThat(request.getHeaders()).containsKey("h3");
      assertThat(request.getHeaders().get("h3")).containsExactly((String) null);
      assertThat(request.getHeaders()).containsKey("h4");
      assertThat(request.getHeaders().get("h4")).containsExactly("value1", "value2");
    }

    @Test
    void bodyIsAddedCorrectly() throws JsonProcessingException {
      Map<String, Object> event = minimalEvent();
      event.put("body", "{\"property\":\"value\"}");
      event.put("isBase64Encoded", false);
      ReactiveEventServerHttpRequest request =
          (ReactiveEventServerHttpRequest) EVENT_MAPPER.compose(event, Map.of());
      assertThat(
              new String(
                  request.getBody().blockLast().asByteBuffer().array(), StandardCharsets.UTF_8))
          .isEqualTo("{\"property\":\"value\"}");
    }

    @Test
    void bodyIsAddedCorrectlyWhenBase64() throws JsonProcessingException {
      Map<String, Object> event = minimalEvent();
      event.put(
          "body",
          new String(
              Base64.getEncoder()
                  .encode("{\"property\":\"value\"}".getBytes(StandardCharsets.UTF_8)),
              StandardCharsets.UTF_8));
      event.put("isBase64Encoded", true);
      ReactiveEventServerHttpRequest request =
          (ReactiveEventServerHttpRequest) EVENT_MAPPER.compose(event, Map.of());
      assertThat(
              new String(
                  request.getBody().blockLast().asByteBuffer().array(), StandardCharsets.UTF_8))
          .isEqualTo("{\"property\":\"value\"}");
    }
  }

  @Nested
  class CreateTest {

    @Test
    void isReactiveEventServerHttpResponse() {
      assertThat(EVENT_MAPPER.create()).isInstanceOf(ReactiveEventServerHttpResponse.class);
    }
  }

  @Nested
  class ResponseTest {

    @Mock private ReactiveEventServerHttpResponse response;

    @BeforeEach
    void setUpEach() {
      given(response.getStatusCode()).willReturn(HttpStatus.OK);
      given(response.getHeaders())
          .willReturn(
              new HttpHeaders(
                  new MultiValueMapAdapter<>(Map.of("h1", Collections.singletonList("value1")))));
    }

    @Test
    void statusIsCorrect() {
      AWSLambdaCustomResponse result = (AWSLambdaCustomResponse) EVENT_MAPPER.respond(response);
      assertThat(result.getStatusCode()).isEqualTo(200);
    }

    @Test
    void notBase64Decoded() {
      AWSLambdaCustomResponse result = (AWSLambdaCustomResponse) EVENT_MAPPER.respond(response);
      assertThat(result.getIsBase64Encoded()).isFalse();
    }

    @Test
    void headersAreCorrect() {
      AWSLambdaCustomResponse result = (AWSLambdaCustomResponse) EVENT_MAPPER.respond(response);
      assertThat(result.getHeaders()).containsExactly(MapEntry.entry("h1", "value1"));
    }

    @Test
    void bodyIsCorrect() {
      given(response.getBody())
          .willReturn(
              Flux.just(
                  ByteBuffer.wrap("{\"property\":\"value\"}".getBytes(StandardCharsets.UTF_8))));
      AWSLambdaCustomResponse result = (AWSLambdaCustomResponse) EVENT_MAPPER.respond(response);
      assertThat(result.getBody()).isEqualTo("{\"property\":\"value\"}");
    }

    @Test
    void bodyIsNullWhenNotPresent() {
      AWSLambdaCustomResponse result = (AWSLambdaCustomResponse) EVENT_MAPPER.respond(response);
      assertThat(result.getBody()).isNull();
    }
  }

  private static Map<String, Object> minimalMatch() {
    return new HashMap<>(
        Map.of(
            "version",
            "1.0",
            "httpMethod",
            "GET",
            "path",
            "/path",
            "requestContext",
            new HashMap<>(Map.of("httpMethod", "GET"))));
  }

  private static Map<String, Object> minimalEvent() {
    return new HashMap<>(
        Map.of(
            "version",
            "1.0",
            "body",
            "{\"property\":\"value\"}",
            "isBase64Encoded",
            false,
            "httpMethod",
            "GET",
            "path",
            "/path",
            "multiValueQueryStringParameters",
            new HashMap<>(
                Map.of(
                    "q1",
                    Collections.singletonList("value1"),
                    "q2",
                    Collections.singletonList(""),
                    "q3",
                    Collections.singletonList(null))),
            "multiValueHeaders",
            new HashMap<>(
                Map.of(
                    "h1",
                    Collections.singletonList("value1"),
                    "h2",
                    Collections.singletonList(""),
                    "h3",
                    Collections.singletonList(null))),
            "requestContext",
            new HashMap<>(Map.of("httpMethod", "GET", "requestId", "test-request-id"))));
  }

  static class InvalidMatches implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext)
        throws Exception {
      Map<String, Object> withoutHttpMethod = minimalMatch();
      withoutHttpMethod.remove("httpMethod");

      Map<String, Object> withoutPath = minimalMatch();
      withoutPath.remove("path");

      Map<String, Object> withoutContext = minimalMatch();
      withoutContext.remove("requestContext");

      Map<String, Object> withoutContextHttpMethod = minimalMatch();
      ((Map<String, Object>) withoutContextHttpMethod.get("requestContext")).remove("httpMethod");

      return Stream.of(withoutHttpMethod, withoutPath, withoutContext, withoutContextHttpMethod)
          .map(Arguments::of);
    }
  }
}
