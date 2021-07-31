package io.github.satsg.aws.lambda.runtime.spring.harness.event.reactive;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import io.github.satsg.aws.lambda.runtime.spring.harness.event.AWSLambdaCustomResponse;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;
import org.assertj.core.data.MapEntry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.util.MultiValueMapAdapter;
import reactor.core.publisher.Flux;

@ExtendWith(MockitoExtension.class)
class DefaultReactiveServerResponseMapperTest {

  private static final DefaultReactiveServerResponseMapper MAPPER =
      new DefaultReactiveServerResponseMapper();

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
    AWSLambdaCustomResponse result = MAPPER.response(response);
    assertThat(result.getStatusCode()).isEqualTo(200);
  }

  @Test
  void notBase64Decoded() {
    AWSLambdaCustomResponse result = MAPPER.response(response);
    assertThat(result.getIsBase64Encoded()).isFalse();
  }

  @Test
  void headersAreCorrect() {
    AWSLambdaCustomResponse result = MAPPER.response(response);
    assertThat(result.getHeaders()).containsExactly(MapEntry.entry("h1", "value1"));
  }

  @Test
  void bodyIsCorrect() {
    given(response.getBody())
        .willReturn(
            Flux.just(
                ByteBuffer.wrap("{\"property\":\"value\"}".getBytes(StandardCharsets.UTF_8))));
    AWSLambdaCustomResponse result = MAPPER.response(response);
    assertThat(result.getBody()).isEqualTo("{\"property\":\"value\"}");
  }

  @Test
  void bodyIsNullWhenNotPresent() {
    AWSLambdaCustomResponse result = MAPPER.response(response);
    assertThat(result.getBody()).isNull();
  }
}
