package io.github.satsg.aws.lambda.runtime.spring.harness.event.http.reactive;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.function.Supplier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

class ReactiveEventServerHttpResponseTest {

  private static final DataBufferFactory FACTORY = new DefaultDataBufferFactory();

  private ReactiveEventServerHttpResponse response;

  @BeforeEach
  void setUpEach() {
    response = new ReactiveEventServerHttpResponse(FACTORY);
  }

  @Test
  void ableToSetStatusCode() {
    response.setStatusCode(HttpStatus.OK);
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
  }

  @Test
  void cookiesAreNotNull() {
    assertThat(response.getCookies()).isNotNull();
  }

  @Test
  void headersAreNotNull() {
    assertThat(response.getHeaders()).isNotNull();
  }

  @Test
  void actionsAreNotNull() {
    assertThat(response.getActions()).isNotNull();
  }

  @Test
  void ableToAddHeader() {
    response.getHeaders().add("h1", "value1");
    assertThat(response.getHeaders()).containsKey("h1");
    assertThat(response.getHeaders().get("h1")).containsExactly("value1");
  }

  @Test
  void factoryIsCorrect() {
    assertThat(response.bufferFactory()).isSameAs(FACTORY);
  }

  @Test
  void ableToAddCookie() {
    ResponseCookie cookie = ResponseCookie.from("cookie1", "value1").build();
    response.addCookie(cookie);
    assertThat(response.getCookies().get("cookie1")).containsExactly(cookie);
  }

  @Test
  void ableToAddActions() {
    Supplier<? extends Mono<Void>> supplier = Mono::empty;
    response.beforeCommit(supplier);
    assertThat(response.getActions()).containsExactly(supplier);
  }

  @Test
  void ableToWriteBody() {
    response.writeWith(
        Flux.just(
            FACTORY.wrap(
                ByteBuffer.wrap("{\"property\":\"value\"}".getBytes(StandardCharsets.UTF_8)))));
    assertThat(new String(response.getBody().blockLast().array(), StandardCharsets.UTF_8))
        .isEqualTo("{\"property\":\"value\"}");
  }
}
