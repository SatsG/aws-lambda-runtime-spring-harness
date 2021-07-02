package com.github.satsg.aws.lambda.runtime.spring.harness.event.reactive;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.function.Supplier;
import org.reactivestreams.Publisher;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.util.MultiValueMap;
import org.springframework.util.MultiValueMapAdapter;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class ReactiveEventServerHttpResponse implements ServerHttpResponse {

  private final DataBufferFactory factory;
  private HttpStatus status;
  private final MultiValueMap<String, ResponseCookie> cookies;
  private final HttpHeaders headers;
  private byte[] body;

  public ReactiveEventServerHttpResponse(DataBufferFactory factory) {
    this.factory = factory;
    this.status = null;
    this.headers = new HttpHeaders();
    this.cookies = new MultiValueMapAdapter<>(new HashMap<>());
    this.body = null;
  }

  @Override
  public boolean setStatusCode(HttpStatus status) {
    this.status = status;
    return true;
  }

  @Override
  public HttpStatus getStatusCode() {
    return status;
  }

  @Override
  public MultiValueMap<String, ResponseCookie> getCookies() {
    return cookies;
  }

  @Override
  public void addCookie(ResponseCookie cookie) {
    cookies.add(cookie.getName(), cookie);
  }

  @Override
  public DataBufferFactory bufferFactory() {
    return factory;
  }

  @Override
  public void beforeCommit(Supplier<? extends Mono<Void>> action) {
    action.get().block();
  }

  @Override
  public boolean isCommitted() {
    return false;
  }

  @Override
  public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
    ByteArrayOutputStream stream = new ByteArrayOutputStream();
    DataBufferUtils.write(Mono.from(body).map(buffer -> (DataBuffer) buffer), stream).blockLast();
    this.body = stream.toByteArray();
    return Mono.empty();
  }

  @Override
  public Mono<Void> writeAndFlushWith(Publisher<? extends Publisher<? extends DataBuffer>> body) {
    ByteArrayOutputStream stream = new ByteArrayOutputStream();
    DataBufferUtils.write(
            Flux.from(body)
                .flatMap(publisher -> Mono.from(publisher).map(buffer -> (DataBuffer) buffer)),
            stream)
        .blockLast();
    this.body = stream.toByteArray();
    return Mono.empty();
  }

  @Override
  public Mono<Void> setComplete() {
    return Mono.empty();
  }

  @Override
  public HttpHeaders getHeaders() {
    return headers;
  }

  public byte[] getBody() {
    return body;
  }
}
