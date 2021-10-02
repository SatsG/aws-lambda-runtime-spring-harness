package io.github.satsg.aws.lambda.runtime.spring.harness.event.reactive.mappers;

import io.github.satsg.aws.lambda.runtime.spring.harness.event.reactive.ReactiveEventMapper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.web.util.DefaultUriBuilderFactory;

@ExtendWith(MockitoExtension.class)
class ALBEventMapperTest extends BaseHttpEventMapperTest {

  @Nested
  class MatchesTest {

    @Test
    void doesNotMatchIfEventIsNotMapType() {
      Assertions.assertThat(getMapper().matches(new ArrayList<>())).isFalse();
    }

    @Test
    void matchesCorrectly() {
      Map<String, Object> event = minimalMatch();
      Assertions.assertThat(getMapper().matches(event)).isTrue();
    }

    @Test
    void incorrectMatchesFails() {
      Map<String, Object> event = minimalMatch();
      ((Map<String, Object>) event.get("requestContext")).remove("elb");
      Assertions.assertThat(getMapper().matches(event)).isFalse();
    }
  }

  private static Map<String, Object> minimalMatch() {
    return new HashMap<>(Map.of("requestContext", new HashMap<>(Map.of("elb", Map.of()))));
  }

  @Override
  protected Map<String, Object> minimalEvent() {
    return new HashMap<>(
        Map.of(
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
            new HashMap<>(Map.of("elb", Map.of()))));
  }

  @Override
  protected ReactiveEventMapper getMapper() {
    return new ALBEventMapper(new DefaultDataBufferFactory(), new DefaultUriBuilderFactory());
  }
}
