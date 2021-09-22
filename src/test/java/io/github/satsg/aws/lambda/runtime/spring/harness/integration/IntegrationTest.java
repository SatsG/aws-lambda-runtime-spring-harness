package io.github.satsg.aws.lambda.runtime.spring.harness.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.satsg.aws.lambda.runtime.spring.harness.event.ServerlessEventLoop;
import io.github.satsg.aws.lambda.runtime.spring.harness.integration.config.TestConfig;
import io.github.satsg.aws.lambda.runtime.spring.harness.runner.EventLoopRunner;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
    properties = {
      "server.port=8080",
      "AWS_LAMBDA_RUNTIME_API=localhost:${server.port}",
      "satsg.aws.lambda.runtime.version=2018-06-01",
      "satsg.enable.aws.lambda.runtime.configuration=true"
    })
@ExtendWith({SpringExtension.class, MockitoExtension.class})
@Import(TestConfig.class)
@DirtiesContext
public class IntegrationTest {

  @Autowired private ObjectMapper mapper;
  @Autowired private TestRestTemplate restTemplate;

  private Future<?> eventRunner;

  @BeforeEach
  void setUpEach(@Autowired ServerlessEventLoop eventLoop) {
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

  @AfterEach
  void tearDownEach() {
    eventRunner.cancel(true);
  }

  protected <T> T loadEventFile(String filename, Class<T> clazz) {
    try (InputStream eventStream = getClass().getClassLoader().getResourceAsStream(filename)) {
      return mapper.readValue(eventStream.readAllBytes(), clazz);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  protected void createEvent(Object event) {
    restTemplate.postForEntity("/push", new HttpEntity<>(event), Void.class);
  }

  protected <T> T getEventLoopResponse(Class<T> type) throws InterruptedException {
    int timeoutMs = 1000;
    int retry = 5;
    int multiplier = 2;

    ResponseEntity<T> response = null;
    while (retry > 0 && response == null) {
      response = restTemplate.exchange("/pull", HttpMethod.GET, null, type);
      if (response.getStatusCode().is4xxClientError()) {
        response = null;
        --retry;
        Thread.sleep(timeoutMs);
        timeoutMs *= multiplier;
      }
    }

    return response != null ? response.getBody() : Assertions.fail("Unable to pull event");
  }
}
