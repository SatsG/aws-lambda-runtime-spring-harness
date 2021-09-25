package io.github.satsg.aws.lambda.runtime.spring.harness.event.reactive.mappers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import io.github.satsg.aws.lambda.runtime.spring.harness.event.AWSLambdaCustomResponse;
import io.github.satsg.aws.lambda.runtime.spring.harness.event.reactive.ReactiveEventMapper;
import io.github.satsg.aws.lambda.runtime.spring.harness.event.reactive.ReactiveEventServerHttpRequest;
import io.github.satsg.aws.lambda.runtime.spring.harness.event.reactive.ReactiveEventServerHttpResponse;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.assertj.core.data.MapEntry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.RequestPath;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.MultiValueMapAdapter;
import reactor.core.publisher.Flux;

@ExtendWith(MockitoExtension.class)
abstract class BaseHttpEventMapperTest {

  @Nested
  class ComposeTest {

    @Test
    void requestIsDefaultReactiveType() {
      ServerHttpRequest request = getMapper().compose(minimalEvent(), Map.of());
      assertThat(request).isInstanceOf(ReactiveEventServerHttpRequest.class);
    }

    @Test
    void methodIsSetCorrectly() {
      Map<String, Object> event = minimalEvent();
      event.put("httpMethod", "POST");
      ServerHttpRequest request = getMapper().compose(event, Map.of());
      assertThat(request.getMethod()).isEqualTo(HttpMethod.POST);
    }

    @Test
    void pathIsSetCorrectly() {
      Map<String, Object> event = minimalEvent();
      event.put("path", "/path");
      ServerHttpRequest request = getMapper().compose(event, Map.of());
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

      ServerHttpRequest request = getMapper().compose(event, Map.of());

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

      ServerHttpRequest request = getMapper().compose(event, Map.of());

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
    void bodyIsAddedCorrectly() {
      Map<String, Object> event = minimalEvent();
      event.put("body", "{\"property\":\"value\"}");
      event.put("isBase64Encoded", false);
      ReactiveEventServerHttpRequest request =
          (ReactiveEventServerHttpRequest) getMapper().compose(event, Map.of());
      assertThat(
              new String(
                  request.getBody().blockLast().asByteBuffer().array(), StandardCharsets.UTF_8))
          .isEqualTo("{\"property\":\"value\"}");
    }

    @Test
    void bodyIsAddedCorrectlyWhenBase64() {
      Map<String, Object> event = minimalEvent();
      event.put(
          "body",
          new String(
              Base64.getEncoder()
                  .encode("{\"property\":\"value\"}".getBytes(StandardCharsets.UTF_8)),
              StandardCharsets.UTF_8));
      event.put("isBase64Encoded", true);
      ReactiveEventServerHttpRequest request =
          (ReactiveEventServerHttpRequest) getMapper().compose(event, Map.of());
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
      assertThat(getMapper().create()).isInstanceOf(ReactiveEventServerHttpResponse.class);
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
      AWSLambdaCustomResponse result = (AWSLambdaCustomResponse) getMapper().respond(response);
      assertThat(result.getStatusCode()).isEqualTo(200);
    }

    @Test
    void notBase64Decoded() {
      AWSLambdaCustomResponse result = (AWSLambdaCustomResponse) getMapper().respond(response);
      assertThat(result.getIsBase64Encoded()).isFalse();
    }

    @Test
    void headersAreCorrect() {
      AWSLambdaCustomResponse result = (AWSLambdaCustomResponse) getMapper().respond(response);
      assertThat(result.getHeaders()).containsExactly(MapEntry.entry("h1", "value1"));
    }

    @Test
    void multiHeadersAreCorrect() {
      AWSLambdaCustomResponse result = (AWSLambdaCustomResponse) getMapper().respond(response);
      assertThat(result.getMultiValueHeaders())
          .containsExactly(MapEntry.entry("h1", List.of("value1")));
    }

    @Test
    void bodyIsCorrect() {
      given(response.getBody())
          .willReturn(
              Flux.just(
                  ByteBuffer.wrap("{\"property\":\"value\"}".getBytes(StandardCharsets.UTF_8))));
      AWSLambdaCustomResponse result = (AWSLambdaCustomResponse) getMapper().respond(response);
      assertThat(result.getBody()).isEqualTo("{\"property\":\"value\"}");
    }

    @Test
    void bodyIsEmptyWhenNotPresent() {
      AWSLambdaCustomResponse result = (AWSLambdaCustomResponse) getMapper().respond(response);
      assertThat(result.getBody()).isEmpty();
    }
  }

  protected abstract Map<String, Object> minimalEvent();

  protected abstract ReactiveEventMapper getMapper();
}
