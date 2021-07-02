package io.github.satsg.aws.lambda.runtime.spring.harness.event.mappers;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.satsg.aws.lambda.runtime.spring.harness.event.reactive.ReactiveEventMapper;
import io.github.satsg.aws.lambda.runtime.spring.harness.event.reactive.ReactiveEventServerHttpRequest;
import java.util.Map;
import java.util.stream.Stream;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriBuilderFactory;

public class APIGatewayV2EventMapper implements ReactiveEventMapper {

  private final DataBufferFactory dataBufferFactory;
  private final UriBuilderFactory uriBuilderFactory;
  private final ObjectMapper mapper;

  public APIGatewayV2EventMapper(
      DataBufferFactory dataBufferFactory,
      UriBuilderFactory uriBuilderFactory,
      ObjectMapper mapper) {
    this.dataBufferFactory = dataBufferFactory;
    this.uriBuilderFactory = uriBuilderFactory;
    this.mapper = mapper;
  }

  @Override
  public boolean matches(Object event) {
    try {
      Map<String, Object> eventMap = (Map<String, Object>) event;
      Map<String, Object> requestContext = (Map<String, Object>) eventMap.get("requestContext");
      return eventMap.get("version") != null
          && eventMap.get("version").equals("2.0")
          && requestContext != null
          && requestContext.get("http") != null;
    } catch (Exception e) {
      return false;
    }
  }

  @Override
  public ServerHttpRequest compose(Object event) {
    try {
      Map<String, Object> eventMap = (Map<String, Object>) event;
      Map<String, Object> requestContext = (Map<String, Object>) eventMap.get("requestContext");
      Map<String, Object> http = (Map<String, Object>) requestContext.get("http");

      String requestId = (String) requestContext.get("requestId");
      HttpMethod method = HttpMethod.resolve((String) http.get("method"));

      String path = (String) http.get("path");
      String rawQueryString = (String) eventMap.get("rawQueryString");

      UriBuilder builder = uriBuilderFactory.builder().path(path);
      if (rawQueryString != null) {
        Stream.of(rawQueryString.split("&")).forEach(builder::query);
      }

      HttpHeaders headers = new HttpHeaders();
      ((Map<String, String>) eventMap.get("headers")).forEach(headers::add);

      byte[] body =
          EventMapperUtils.getBody(
              mapper, eventMap.get("body"), (Boolean) eventMap.get("isBase64Encoded"));

      return new ReactiveEventServerHttpRequest(
          dataBufferFactory, requestId, method, builder.build(), headers, body);
    } catch (Exception e) {
      throw new IllegalStateException("Unable to convert event to a request", e);
    }
  }
}
