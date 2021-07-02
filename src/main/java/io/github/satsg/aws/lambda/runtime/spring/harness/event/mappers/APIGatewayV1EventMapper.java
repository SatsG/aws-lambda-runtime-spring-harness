package io.github.satsg.aws.lambda.runtime.spring.harness.event.mappers;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.satsg.aws.lambda.runtime.spring.harness.event.reactive.ReactiveEventMapper;
import io.github.satsg.aws.lambda.runtime.spring.harness.event.reactive.ReactiveEventServerHttpRequest;
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
      HttpMethod method = HttpMethod.resolve((String) context.get("httpMethod"));
      String path = (String) context.get("resourcePath");

      MultiValueMap<String, String> query = new MultiValueMapAdapter<>(new HashMap<>());
      addQueryParams(eventMap, query);

      HttpHeaders headers = new HttpHeaders();
      addHeaders(eventMap, headers);

      UriBuilder requestUriBuilder = uriBuilderFactory.builder().path(path);
      query.forEach(requestUriBuilder::queryParam);

      byte[] body =
          EventMapperUtils.getBody(
              mapper, eventMap.get("body"), (Boolean) eventMap.get("isBase64Encoded"));

      return new ReactiveEventServerHttpRequest(
          dataBufferFactory, requestId, method, requestUriBuilder.build(), headers, body);
    } catch (Exception e) {
      throw new IllegalStateException("Unable to convert api gateway v1 event to a request", e);
    }
  }

  private void addQueryParams(Map<String, Object> event, MultiValueMap<String, String> query) {
    addMultiValueSources(
        event.get("queryStringParameters"), event.get("multiValueQueryStringParameters"), query);
  }

  private void addHeaders(Map<String, Object> event, MultiValueMap<String, String> headers) {
    addMultiValueSources(event.get("headers"), event.get("multiValueHeaders"), headers);
  }

  private void addMultiValueSources(
      Object singleSource, Object multipleSource, MultiValueMap<String, String> destination) {
    if (singleSource != null) {
      Map<String, String> singleSourceValues = (Map<String, String>) singleSource;
      singleSourceValues.forEach(destination::add);
    }

    if (multipleSource != null) {
      Map<String, List<String>> multiSourceValues = (Map<String, List<String>>) multipleSource;
      multiSourceValues.forEach(
          (source, values) ->
              values.forEach(
                  value -> {
                    List<String> current = destination.get(source);
                    if (current == null || !current.contains(value)) {
                      destination.add(source, value);
                    }
                  }));
    }
  }
}
