package io.github.satsg.aws.lambda.runtime.spring.harness.event.mappers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.satsg.aws.lambda.runtime.spring.harness.event.reactive.ReactiveEventMapper;
import io.github.satsg.aws.lambda.runtime.spring.harness.event.reactive.ReactiveEventServerHttpRequest;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
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
  private final ObjectMapper mapper;

  public APIGatewayV1EventMapper(
      DataBufferFactory dataBufferFactory,
      UriBuilderFactory uriBuilderFactory,
      ObjectMapper mapper) {
    this.dataBufferFactory = Objects.requireNonNull(dataBufferFactory);
    this.uriBuilderFactory = Objects.requireNonNull(uriBuilderFactory);
    this.mapper = Objects.requireNonNull(mapper);
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

      byte[] body =
          getBody(mapper, eventMap.get("body"), (Boolean) eventMap.get("isBase64Encoded"));

      return new ReactiveEventServerHttpRequest(
          dataBufferFactory, requestId, method, requestUriBuilder.build(), headers, body);
    } catch (Exception e) {
      throw new IllegalStateException("Unable to convert api gateway v1 event to a request", e);
    }
  }

  private void addQueryParams(Map<String, Object> event, MultiValueMap<String, String> query) {
    addSingleValueSources(event.get("queryStringParameters"), query);
  }

  private void addHeaders(Map<String, Object> event, MultiValueMap<String, String> headers) {
    addSingleValueSources(event.get("headers"), headers);
  }

  private void addSingleValueSources(
      Object singleSource, MultiValueMap<String, String> destination) {
    if (singleSource != null) {
      Map<String, String> singleSourceValues = (Map<String, String>) singleSource;
      singleSourceValues.forEach(destination::add);
    }
  }

  private byte[] getBody(ObjectMapper mapper, Object body, Boolean isBase64Encoded)
      throws JsonProcessingException {
    if (body != null) {
      if (isBase64Encoded) {
        body =
            mapper.readValue(
                new String(Base64.getDecoder().decode((String) body), StandardCharsets.UTF_8),
                Object.class);
      }
      return body instanceof String
          ? String.valueOf(body).getBytes(StandardCharsets.UTF_8)
          : mapper.writeValueAsBytes(body);
    }
    return new byte[0];
  }
}
