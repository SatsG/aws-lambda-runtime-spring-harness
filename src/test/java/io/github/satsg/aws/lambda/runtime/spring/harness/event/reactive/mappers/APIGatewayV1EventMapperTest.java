package io.github.satsg.aws.lambda.runtime.spring.harness.event.reactive.mappers;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.satsg.aws.lambda.runtime.spring.harness.event.reactive.ReactiveEventMapper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.web.util.DefaultUriBuilderFactory;

class APIGatewayV1EventMapperTest extends BaseHttpEventMapperTest {

  @Nested
  class MatchesTest {

    @Test
    void doesNotMatchIfEventIsNotMapType() {
      assertThat(getMapper().matches(new ArrayList<>())).isFalse();
    }

    @Test
    void matchesCorrectly() {
      Map<String, Object> event = minimalMatch();
      assertThat(getMapper().matches(event)).isTrue();
    }

    @Test
    void matchesWithoutVersionCorrectly() {
      Map<String, Object> event = minimalMatch();
      event.remove("version");
      assertThat(getMapper().matches(event)).isTrue();
    }

    @ParameterizedTest
    @ArgumentsSource(InvalidMatches.class)
    void incorrectMatchesFails(Map<String, Object> event) {
      assertThat(getMapper().matches(event)).isFalse();
    }
  }

  private static Map<String, Object> minimalMatch() {
    return new HashMap<>(
        Map.of(
            "version",
            "1.0",
            "httpMethod",
            "GET",
            "path",
            "/path",
            "requestContext",
            new HashMap<>(Map.of("httpMethod", "GET"))));
  }

  @Override
  protected Map<String, Object> minimalEvent() {
    return new HashMap<>(
        Map.of(
            "version",
            "1.0",
            "body",
            "{\"property\":\"value\"}",
            "isBase64Encoded",
            false,
            "httpMethod",
            "GET",
            "path",
            "/path",
            "multiValueQueryStringParameters",
            new HashMap<>(
                Map.of(
                    "q1",
                    Collections.singletonList("value1"),
                    "q2",
                    Collections.singletonList(""),
                    "q3",
                    Collections.singletonList(null))),
            "multiValueHeaders",
            new HashMap<>(
                Map.of(
                    "h1",
                    Collections.singletonList("value1"),
                    "h2",
                    Collections.singletonList(""),
                    "h3",
                    Collections.singletonList(null))),
            "requestContext",
            new HashMap<>(Map.of("httpMethod", "GET", "requestId", "test-request-id"))));
  }

  @Override
  protected ReactiveEventMapper getMapper() {
    return new APIGatewayV1EventMapper(
        new DefaultDataBufferFactory(), new DefaultUriBuilderFactory());
  }

  static class InvalidMatches implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext)
        throws Exception {
      Map<String, Object> withoutHttpMethod = minimalMatch();
      withoutHttpMethod.remove("httpMethod");

      Map<String, Object> withoutPath = minimalMatch();
      withoutPath.remove("path");

      Map<String, Object> withoutContext = minimalMatch();
      withoutContext.remove("requestContext");

      Map<String, Object> withoutContextHttpMethod = minimalMatch();
      ((Map<String, Object>) withoutContextHttpMethod.get("requestContext")).remove("httpMethod");

      return Stream.of(withoutHttpMethod, withoutPath, withoutContext, withoutContextHttpMethod)
          .map(Arguments::of);
    }
  }
}
