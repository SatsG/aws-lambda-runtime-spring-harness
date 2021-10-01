package io.github.satsg.aws.lambda.runtime.spring.harness.event.http.reactive.mappers;

import io.github.satsg.aws.lambda.runtime.spring.harness.event.http.reactive.ReactiveEventServerHttpRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.MultiValueMap;
import org.springframework.util.MultiValueMapAdapter;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriBuilderFactory;

public class APIGatewayV1EventMapper extends AbstractHttpEventMapper {

  public APIGatewayV1EventMapper(
      DataBufferFactory dataBufferFactory, UriBuilderFactory uriBuilderFactory) {
    super(dataBufferFactory, uriBuilderFactory);
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
  public ServerHttpRequest compose(Object event, Map<String, List<String>> headers) {
    try {
      Map<String, Object> eventMap = (Map<String, Object>) event;
      Map<String, Object> context = (Map<String, Object>) eventMap.get("requestContext");

      String requestId = (String) context.get("requestId");
      HttpMethod method = HttpMethod.resolve((String) eventMap.get("httpMethod"));
      String path = (String) eventMap.get("path");

      MultiValueMap<String, String> query = new MultiValueMapAdapter<>(new HashMap<>());
      addQueryParams(eventMap, query);

      HttpHeaders requestHeaders = new HttpHeaders();
      addHeaders(eventMap, requestHeaders);

      UriBuilder requestUriBuilder = getUriBuilderFactory().builder().path(path);
      query.forEach(requestUriBuilder::queryParam);

      byte[] body = getBody(eventMap.get("body"), (Boolean) eventMap.get("isBase64Encoded"));

      return new ReactiveEventServerHttpRequest(
          getDataBufferFactory(),
          requestId,
          method,
          requestUriBuilder.build(),
          requestHeaders,
          body);
    } catch (Exception e) {
      throw new IllegalStateException("Unable to convert api gateway v1 event to a request", e);
    }
  }
}
