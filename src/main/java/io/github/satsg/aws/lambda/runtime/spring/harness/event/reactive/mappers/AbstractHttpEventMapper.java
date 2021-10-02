package io.github.satsg.aws.lambda.runtime.spring.harness.event.reactive.mappers;

import io.github.satsg.aws.lambda.runtime.spring.harness.event.AWSLambdaCustomResponse;
import io.github.satsg.aws.lambda.runtime.spring.harness.event.reactive.ReactiveEventMapper;
import io.github.satsg.aws.lambda.runtime.spring.harness.event.reactive.ReactiveEventServerHttpResponse;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriBuilderFactory;

public abstract class AbstractHttpEventMapper implements ReactiveEventMapper {

  private final DataBufferFactory dataBufferFactory;
  private final UriBuilderFactory uriBuilderFactory;

  protected AbstractHttpEventMapper(
      DataBufferFactory dataBufferFactory, UriBuilderFactory uriBuilderFactory) {
    this.dataBufferFactory = dataBufferFactory;
    this.uriBuilderFactory = uriBuilderFactory;
  }

  @Override
  public ServerHttpResponse create() {
    return new ReactiveEventServerHttpResponse(getDataBufferFactory());
  }

  @Override
  public Object respond(ServerHttpResponse response) {
    try {
      ReactiveEventServerHttpResponse result = (ReactiveEventServerHttpResponse) response;
      runActions(result);
      appendCookiesToHeaders(result);
      AWSLambdaCustomResponse awsResponse = new AWSLambdaCustomResponse();
      awsResponse.setStatusCode(result.getStatusCode().value());
      setResponseBody(result, awsResponse);
      awsResponse.setHeaders(result.getHeaders().toSingleValueMap());
      awsResponse.setMultiValueHeaders(response.getHeaders());
      awsResponse.setIsBase64Encoded(false);
      return awsResponse;
    } catch (Exception e) {
      throw new IllegalStateException("Unable to convert server response to lambda response.", e);
    }
  }

  protected DataBufferFactory getDataBufferFactory() {
    return dataBufferFactory;
  }

  protected UriBuilderFactory getUriBuilderFactory() {
    return uriBuilderFactory;
  }

  protected void addQueryParams(Map<String, Object> event, MultiValueMap<String, String> query) {
    addMultiValueSources(event.get("multiValueQueryStringParameters"), query);
  }

  protected void addHeaders(Map<String, Object> event, MultiValueMap<String, String> headers) {
    addMultiValueSources(event.get("multiValueHeaders"), headers);
  }

  protected void addMultiValueSources(
      Object multipleSource, MultiValueMap<String, String> destination) {
    if (multipleSource != null) {
      Map<String, List<String>> multiSourceValues = (Map<String, List<String>>) multipleSource;
      multiSourceValues.forEach(destination::addAll);
    }
  }

  protected byte[] getBody(Object body, Boolean isBase64Encoded) {
    if (body != null) {
      return isBase64Encoded
          ? Base64.getDecoder().decode(String.valueOf(body))
          : String.valueOf(body).getBytes(StandardCharsets.UTF_8);
    }
    return new byte[0];
  }

  private void runActions(ReactiveEventServerHttpResponse response) {
    response.getActions().forEach(action -> action.get().block());
  }

  private void appendCookiesToHeaders(ReactiveEventServerHttpResponse response) {
    for (List<ResponseCookie> cookies : response.getCookies().values()) {
      for (ResponseCookie cookie : cookies) {
        response.getHeaders().add(HttpHeaders.SET_COOKIE, cookie.toString());
      }
    }
  }

  private void setResponseBody(
      ReactiveEventServerHttpResponse reactive, AWSLambdaCustomResponse aws) {
    if (reactive.getBody() != null) {
      ByteBuffer body = reactive.getBody().blockLast();
      if (body != null) {
        aws.setBody(new String(body.array(), StandardCharsets.UTF_8));
      }
    }
  }
}
