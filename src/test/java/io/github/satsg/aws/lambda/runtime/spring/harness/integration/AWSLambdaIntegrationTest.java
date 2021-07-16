package io.github.satsg.aws.lambda.runtime.spring.harness.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.satsg.aws.lambda.runtime.spring.harness.config.TestConfig;
import io.github.satsg.aws.lambda.runtime.spring.harness.event.AWSLambdaCustomResponse;
import io.github.satsg.aws.lambda.runtime.spring.harness.event.ServerlessEventLoop;
import io.github.satsg.aws.lambda.runtime.spring.harness.models.apigateway.v1.APIGatewayRequestV1;
import io.github.satsg.aws.lambda.runtime.spring.harness.runner.EventLoopRunner;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
    properties = {
      "server.port=8080",
      "AWS_LAMBDA_RUNTIME_API=localhost:${server.port}",
      "satsg.aws.lambda.runtime.version=2018-06-01",
      "satsg.enable.aws.lambda.runtime.configuration=true",
      "satsg.enable.aws.lambda.runtime.reactive=true"
    })
@ExtendWith({SpringExtension.class, MockitoExtension.class})
@Import(TestConfig.class)
class AWSLambdaIntegrationTest {

  @Autowired private ObjectMapper mapper;
  @Autowired private TestRestTemplate restTemplate;

  private Future<?> eventRunner;

  @BeforeEach
  void setUpEach(@Autowired ServerlessEventLoop eventLoop) {
    start(eventLoop);
  }

  @Test
  void apiGatewayEventV1() throws InterruptedException {
    stopNextLoop();
    createEvent(createAPIGatewayV1Event());
    AWSLambdaCustomResponse response = getEventLoopResponse();
    Assertions.assertThat(response.getBody()).isEqualTo("{\"property\":\"value\"}");
  }

  @AfterEach
  void tearDownEach() {
    eventRunner.cancel(true);
  }

  private APIGatewayRequestV1 createAPIGatewayV1Event() {
    return loadEventFile("api-gateway-v1.json", APIGatewayRequestV1.class);
  }

  private <T> T loadEventFile(String filename, Class<T> clazz) {
    try (InputStream eventStream = getClass().getClassLoader().getResourceAsStream(filename)) {
      return mapper.readValue(eventStream.readAllBytes(), clazz);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private void createEvent(Object event) {
    restTemplate.postForEntity("/push", new HttpEntity<>(event), Void.class);
  }

  private AWSLambdaCustomResponse getEventLoopResponse() throws InterruptedException {
    int timeoutMs = 1000;
    int retry = 5;
    int multiplier = 2;

    ResponseEntity<AWSLambdaCustomResponse> response = null;
    while (retry > 0 && response == null) {
      response = restTemplate.getForEntity("/pull", AWSLambdaCustomResponse.class);
      if (response.getStatusCode().is4xxClientError()) {
        response = null;
        --retry;
        Thread.sleep(timeoutMs);
        timeoutMs *= multiplier;
      }
    }

    return response != null ? response.getBody() : Assertions.fail("Unable to pull event");
  }

  private void start(ServerlessEventLoop eventLoop) {
    System.setProperty(TestConfig.EVENT_LOOP_CONDITION_PROP, "true");
    eventRunner =
        CompletableFuture.runAsync(
            () -> {
              try {
                new EventLoopRunner(eventLoop).run();
              } catch (Exception e) {
                e.printStackTrace();
              }
            });
  }

  private void stopNextLoop() {
    System.setProperty(TestConfig.EVENT_LOOP_CONDITION_PROP, "false");
  }
}
