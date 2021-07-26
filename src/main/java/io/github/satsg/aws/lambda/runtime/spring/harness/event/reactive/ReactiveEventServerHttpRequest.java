package io.github.satsg.aws.lambda.runtime.spring.harness.event.reactive;

import java.net.URI;
import java.util.HashMap;
import java.util.Objects;
import java.util.stream.Stream;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.RequestPath;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.MultiValueMap;
import org.springframework.util.MultiValueMapAdapter;
import reactor.core.publisher.Flux;

public class ReactiveEventServerHttpRequest implements ServerHttpRequest {
  private final DataBufferFactory factory;
  private final String id;
  private final URI uri;
  private final RequestPath path;
  private final MultiValueMap<String, String> query;
  private final MultiValueMap<String, HttpCookie> cookies;
  private final String method;
  private final HttpHeaders headers;
  private final byte[] body;

  public ReactiveEventServerHttpRequest(
      DataBufferFactory factory,
      String requestId,
      HttpMethod method,
      URI uri,
      HttpHeaders headers,
      byte[] body) {
    this.factory = Objects.requireNonNull(factory);
    this.id = Objects.requireNonNull(requestId);
    this.uri = Objects.requireNonNull(uri);
    this.path = RequestPath.parse(uri, null);
    this.query = createQueryMap(uri);
    this.cookies = new MultiValueMapAdapter<>(new HashMap<>());
    this.method = Objects.requireNonNull(method).toString();
    this.headers = Objects.requireNonNull(headers);
    this.body = Objects.requireNonNull(body);
  }

  @Override
  public String getId() {
    return id;
  }

  @Override
  public RequestPath getPath() {
    return path;
  }

  @Override
  public MultiValueMap<String, String> getQueryParams() {
    return query;
  }

  @Override
  public MultiValueMap<String, HttpCookie> getCookies() {
    return cookies;
  }

  @Override
  public String getMethodValue() {
    return method;
  }

  @Override
  public URI getURI() {
    return uri;
  }

  @Override
  public Flux<DataBuffer> getBody() {
    return Flux.just(factory.wrap(body));
  }

  @Override
  public HttpHeaders getHeaders() {
    return headers;
  }

  private MultiValueMap<String, String> createQueryMap(URI uri) {
    MultiValueMap<String, String> result = new MultiValueMapAdapter<>(new HashMap<>());
    if (uri.getQuery() != null) {
      Stream.of(uri.getQuery().split("&"))
          .map(params -> params.split("="))
          .forEach(pair -> result.add(pair[0], pair.length == 2 ? pair[1] : ""));
    }
    return result;
  }
}
