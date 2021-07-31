package io.github.satsg.aws.lambda.runtime.spring.harness.event;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

import java.util.Collections;
import org.assertj.core.data.MapEntry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;

@ExtendWith(MockitoExtension.class)
class AWSLambdaRuntimeTest {

  @Mock private RestTemplate client;
  private AWSLambdaRuntime runtime;

  @BeforeEach
  void setUpEach() {
    runtime = new AWSLambdaRuntime(client, new AWSLambdaUriBuilderFactory("host", "version"));
  }

  @Test
  void getEventCorrectly() {
    runtime.getEvent();
    verify(client).getForEntity("http://host/version/runtime/invocation/next", Object.class);
  }

  @Test
  void sendResponseCorrectly() {
    AWSLambdaCustomResponse response = new AWSLambdaCustomResponse();
    runtime.sendResponse("request-id", response);
    verify(client)
        .postForObject(
            "http://host/version/runtime/invocation/{id}/response",
            response,
            Object.class,
            "request-id");
  }

  @Test
  void sendErrorCorrectly() {
    AWSLambdaErrorResponse response =
        new AWSLambdaErrorResponse("", "error-type", Collections.emptyList());
    runtime.sendErrorResponse("request-id", response);
    ArgumentCaptor<HttpEntity<?>> entityArgumentCaptor = ArgumentCaptor.forClass(HttpEntity.class);

    verify(client)
        .postForObject(
            eq("http://host/version/runtime/invocation/{id}/error"),
            entityArgumentCaptor.capture(),
            eq(Object.class),
            eq("request-id"));

    assertThat(entityArgumentCaptor.getValue().getHeaders())
        .containsExactly(
            MapEntry.entry(
                "Lambda-Runtime-Function-Error-Type", Collections.singletonList("error-type")));
  }
}
