package io.github.satsg.aws.lambda.runtime.spring.harness.event;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.satsg.aws.lambda.runtime.spring.harness.config.TestConfig;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class AWSLambdaEventLoopTest {

  private static final ResponseEntity<Object> EVENT_ENTITY =
      ResponseEntity.ok()
          .header("Lambda-Runtime-Aws-Request-Id", "request-id")
          .body(Collections.emptyMap());

  @Mock private AWSLambdaRuntime runtime;
  @Mock private AWSEventHandler handler;
  @Mock private EventErrorMapper eventErrorMapper;
  @Mock private ObjectMapper mapper;

  private AWSLambdaEventLoop eventLoop;

  @BeforeEach
  void setUpEach() {
    given(runtime.getEvent()).willReturn(EVENT_ENTITY);
  }

  @Nested
  class SingleLoopTest {

    private final LoopCondition CONDITION = new TestConfig.BoundedLoop(1);

    @BeforeEach
    void setUpEach() {
      eventLoop = new AWSLambdaEventLoop(runtime, handler, eventErrorMapper, CONDITION, mapper);
    }

    @Test
    void successfulHandlingSendsResponse() {
      AWSLambdaCustomResponse response = new AWSLambdaCustomResponse();
      given(handler.handle(EVENT_ENTITY.getBody())).willReturn(response);
      eventLoop.run();
      verify(runtime).sendResponse("request-id", response);
    }

    @Test
    void errorSendsResponse() {
      RuntimeException exception = new RuntimeException("Whoops!");
      AWSLambdaErrorResponse errorResponse = new AWSLambdaErrorResponse();

      given(handler.handle(EVENT_ENTITY.getBody())).willThrow(exception);
      given(eventErrorMapper.error(exception)).willReturn(errorResponse);

      eventLoop.run();
      verify(runtime).sendErrorResponse("request-id", errorResponse);
    }
  }

  @Nested
  class MultipleLoopTest {

    private final LoopCondition CONDITION = new TestConfig.BoundedLoop(2);

    @BeforeEach
    void setUpEach() {
      eventLoop = new AWSLambdaEventLoop(runtime, handler, eventErrorMapper, CONDITION, mapper);
    }

    @Test
    void successfulHandlingSendsResponseTheCorrectAmountOfTimes() {
      AWSLambdaCustomResponse response = new AWSLambdaCustomResponse();
      given(handler.handle(EVENT_ENTITY.getBody())).willReturn(response);
      eventLoop.run();
      verify(runtime, times(2)).sendResponse("request-id", response);
    }
  }
}
