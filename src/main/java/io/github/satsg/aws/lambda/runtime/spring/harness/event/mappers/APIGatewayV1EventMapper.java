package io.github.satsg.aws.lambda.runtime.spring.harness.event.mappers;

import io.github.satsg.aws.lambda.runtime.spring.harness.event.reactive.ReactiveEventMapper;
import io.github.satsg.aws.lambda.runtime.spring.harness.event.reactive.ReactiveEventServerHttpRequest;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.MultiValueMap;
import org.springframework.util.MultiValueMapAdapter;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriBuilderFactory;

public class APIGatewayV1EventMapper implements ReactiveEventMapper {

  private final DataBufferFactory dataBufferFactory;
  private final UriBuilderFactory uriBuilderFactory;

  public APIGatewayV1EventMapper(
      DataBufferFactory dataBufferFactory, UriBuilderFactory uriBuilderFactory) {
    this.dataBufferFactory = Objects.requireNonNull(dataBufferFactory);
    this.uriBuilderFactory = Objects.requireNonNull(uriBuilderFactory);
  }

  @Override
  public boolean matches(Object event) {
    try {
      Map<String, Object> eventMap = (Map<String, Object>) event;
      Map<String, Object> context = (Map<String, Object>) eventMap.get("requestContext");

      return (eventMap.get("version") == null || eventMap.get("version").equals("1.0"))
          && eventMap.get("httpMethod") != null
          && eventMap.get("path") != null
          && context != null
          && context.get("httpMethod") != null;
    } catch (Exception e) {
      return false;
    }
  }

  @Override
  public ServerHttpRequest compose(Object event) {
    try {
      Map<String, Object> eventMap = (Map<String, Object>) event;
      Map<String, Object> context = (Map<String, Object>) eventMap.get("requestContext");

      String requestId = (String) context.get("requestId");
      HttpMethod method = HttpMethod.resolve((String) eventMap.get("httpMethod"));
      String path = (String) eventMap.get("path");

      MultiValueMap<String, String> query = new MultiValueMapAdapter<>(new HashMap<>());
      addQueryParams(eventMap, query);

      HttpHeaders headers = new HttpHeaders();
      addHeaders(eventMap, headers);

      UriBuilder requestUriBuilder = uriBuilderFactory.builder().path(path);
      query.forEach(requestUriBuilder::queryParam);

      byte[] body = getBody(eventMap.get("body"), (Boolean) eventMap.get("isBase64Encoded"));

      return new ReactiveEventServerHttpRequest(
          dataBufferFactory, requestId, method, requestUriBuilder.build(), headers, body);
    } catch (Exception e) {
      throw new IllegalStateException("Unable to convert api gateway v1 event to a request", e);
    }
  }

  private void addQueryParams(Map<String, Object> event, MultiValueMap<String, String> query) {
    addMultiValueSources(event.get("multiValueQueryStringParameters"), query);
  }

  private void addHeaders(Map<String, Object> event, MultiValueMap<String, String> headers) {
    addMultiValueSources(event.get("multiValueHeaders"), headers);
  }

  private void addMultiValueSources(
      Object multipleSource, MultiValueMap<String, String> destination) {
    if (multipleSource != null) {
      Map<String, List<String>> multiSourceValues = (Map<String, List<String>>) multipleSource;
      multiSourceValues.forEach(destination::addAll);
    }
  }

  private byte[] getBody(Object body, Boolean isBase64Encoded) {
    if (body != null) {
      return isBase64Encoded
          ? Base64.getDecoder().decode(String.valueOf(body))
          : String.valueOf(body).getBytes(StandardCharsets.UTF_8);
    }
    return new byte[0];
  }
}
