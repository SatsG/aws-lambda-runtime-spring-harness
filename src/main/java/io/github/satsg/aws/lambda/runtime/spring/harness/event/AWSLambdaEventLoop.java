package io.github.satsg.aws.lambda.runtime.spring.harness.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;

public class AWSLambdaEventLoop implements ServerlessEventLoop {

  private static final Logger LOGGER = LoggerFactory.getLogger(AWSLambdaEventLoop.class);

  private final AWSLambdaRuntime runtime;
  private final AWSEventHandler handler;
  private final EventErrorMapper eventErrorMapper;
  private final LoopCondition condition;
  private final ObjectMapper mapper;

  public AWSLambdaEventLoop(
      AWSLambdaRuntime runtime,
      AWSEventHandler handler,
      EventErrorMapper eventErrorMapper,
      LoopCondition condition,
      ObjectMapper mapper) {
    this.runtime = Objects.requireNonNull(runtime);
    this.handler = Objects.requireNonNull(handler);
    this.eventErrorMapper = Objects.requireNonNull(eventErrorMapper);
    this.condition = Objects.requireNonNull(condition);
    this.mapper = mapper;
  }

  @Override
  public void run() {
    while (condition.shouldContinue()) {
      try {
        ResponseEntity<Object> event = getEvent();
        String requestId =
            event.getHeaders().getFirst(AWSLambdaRuntime.LAMBDA_RUNTIME_REQUEST_ID_HEADER);
        LOGGER.info("Processing event: " + requestId);
        processResponse(requestId, event.getBody());
      } catch (Exception e) {
        LOGGER.error("There was an issue retrieving the event.", e);
      }
    }
  }

  private ResponseEntity<Object> getEvent() throws JsonProcessingException {
    ResponseEntity<Object> event = runtime.getEvent();
    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug("Received event: " + mapper.writeValueAsString(event.getBody()));
    }
    return event;
  }

  private void processResponse(String requestId, Object event) {
    try {
      createResponse(requestId, event);
    } catch (Exception e) {
      LOGGER.error("There was an issue responding to the event.", e);
      executeErrorProcedure(() -> runtime.sendErrorResponse(requestId, eventErrorMapper.error(e)));
    }
  }

  private void createResponse(String requestId, Object event) throws JsonProcessingException {
    AWSLambdaCustomResponse response = handler.handle(event);
    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug("Attempting to respond with: " + mapper.writeValueAsString(response));
    }
    runtime.sendResponse(requestId, response);
    LOGGER.info("Responded to event: " + requestId);
  }

  private void executeErrorProcedure(Runnable proc) {
    try {
      proc.run();
    } catch (Exception e) {
      LOGGER.error("Failed to send error message to the runtime.", e);
    }
  }
}
