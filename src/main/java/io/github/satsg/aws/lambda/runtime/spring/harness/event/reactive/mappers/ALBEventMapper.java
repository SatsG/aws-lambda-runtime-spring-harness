package io.github.satsg.aws.lambda.runtime.spring.harness.event.reactive.mappers;

import io.github.satsg.aws.lambda.runtime.spring.harness.event.AWSLambdaRuntime;
import io.github.satsg.aws.lambda.runtime.spring.harness.event.reactive.ReactiveEventServerHttpRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.MultiValueMap;
import org.springframework.util.MultiValueMapAdapter;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriBuilderFactory;

public class ALBEventMapper extends AbstractHttpEventMapper {

  public ALBEventMapper(DataBufferFactory dataBufferFactory, UriBuilderFactory uriBuilderFactory) {
    super(dataBufferFactory, uriBuilderFactory);
  }

  @Override
  public boolean matches(Object event) {
    try {
      Map<String, Object> eventMap = (Map<String, Object>) event;
      Map<String, Object> context = (Map<String, Object>) eventMap.get("requestContext");
      return context.get("elb") != null;
    } catch (Exception e) {
      return false;
    }
  }

  @Override
  public ServerHttpRequest compose(Object event, Map<String, List<String>> headers) {
    try {
      Map<String, Object> eventMap = (Map<String, Object>) event;

      String requestId =
          Optional.ofNullable(headers.get(AWSLambdaRuntime.LAMBDA_RUNTIME_REQUEST_ID_HEADER))
              .flatMap(values -> values.stream().findFirst())
              .orElse(UUID.randomUUID().toString());

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
      throw new IllegalStateException("Unable to convert alb event to a request", e);
    }
  }
}
