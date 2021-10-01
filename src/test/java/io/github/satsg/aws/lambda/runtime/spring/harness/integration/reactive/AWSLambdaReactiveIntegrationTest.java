package io.github.satsg.aws.lambda.runtime.spring.harness.integration.reactive;

import io.github.satsg.aws.lambda.runtime.spring.harness.event.AWSLambdaCustomResponse;
import io.github.satsg.aws.lambda.runtime.spring.harness.integration.IntegrationTest;
import io.github.satsg.aws.lambda.runtime.spring.harness.models.alb.ALBRequest;
import io.github.satsg.aws.lambda.runtime.spring.harness.models.apigateway.v1.APIGatewayRequestV1;
import java.util.List;
import java.util.Map;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

@TestPropertySource("classpath:application-reactive.properties")
class AWSLambdaReactiveIntegrationTest extends IntegrationTest {

  @Test
  @DirtiesContext
  void apiGatewayEventV1() throws InterruptedException {
    createEvent(createAPIGatewayV1Event());
    AWSLambdaCustomResponse response = getEventLoopResponse(AWSLambdaCustomResponse.class);
    assertResponse(response);
  }

  @Test
  @DirtiesContext
  void albEvent() throws InterruptedException {
    createEvent(createALBEvent());
    AWSLambdaCustomResponse response = getEventLoopResponse(AWSLambdaCustomResponse.class);
    assertResponse(response);
  }

  private APIGatewayRequestV1 createAPIGatewayV1Event() {
    return loadEventFile("api-gateway-v1.json", APIGatewayRequestV1.class);
  }

  private ALBRequest createALBEvent() {
    return loadEventFile("alb-event.json", ALBRequest.class);
  }

  private void assertResponse(AWSLambdaCustomResponse response) {
    Assertions.assertThat(response.getStatusCode()).isEqualTo(200);
    Assertions.assertThat(response.getIsBase64Encoded()).isFalse();
    Assertions.assertThat(response.getBody()).isEqualTo("{\"property\":\"value\"}");
    Assertions.assertThat(response.getHeaders())
        .containsExactlyInAnyOrderEntriesOf(
            Map.of(
                "Content-Length",
                "20",
                "Content-Type",
                "application/json",
                "Set-Cookie",
                "here=there"));
    Assertions.assertThat(response.getMultiValueHeaders())
        .containsExactlyInAnyOrderEntriesOf(
            Map.of(
                "Content-Length",
                List.of("20"),
                "Content-Type",
                List.of("application/json"),
                "Set-Cookie",
                List.of("here=there", "me=you")));
  }
}
