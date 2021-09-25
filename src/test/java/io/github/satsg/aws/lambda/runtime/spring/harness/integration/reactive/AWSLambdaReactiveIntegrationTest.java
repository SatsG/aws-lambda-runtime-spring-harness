package io.github.satsg.aws.lambda.runtime.spring.harness.integration.reactive;

import io.github.satsg.aws.lambda.runtime.spring.harness.event.AWSLambdaCustomResponse;
import io.github.satsg.aws.lambda.runtime.spring.harness.integration.IntegrationTest;
import io.github.satsg.aws.lambda.runtime.spring.harness.models.alb.ALBRequest;
import io.github.satsg.aws.lambda.runtime.spring.harness.models.apigateway.v1.APIGatewayRequestV1;
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
    Assertions.assertThat(response.getBody()).isEqualTo("{\"property\":\"value\"}");
  }

  @Test
  @DirtiesContext
  void albEvent() throws InterruptedException {
    createEvent(createALBEvent());
    AWSLambdaCustomResponse response = getEventLoopResponse(AWSLambdaCustomResponse.class);
    Assertions.assertThat(response.getBody()).isEqualTo("{\"property\":\"value\"}");
  }

  private APIGatewayRequestV1 createAPIGatewayV1Event() {
    return loadEventFile("api-gateway-v1.json", APIGatewayRequestV1.class);
  }

  private ALBRequest createALBEvent() {
    return loadEventFile("alb-event.json", ALBRequest.class);
  }
}
