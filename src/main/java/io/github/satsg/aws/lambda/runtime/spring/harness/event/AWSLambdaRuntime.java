package io.github.satsg.aws.lambda.runtime.spring.harness.event;

import java.util.Objects;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

public class AWSLambdaRuntime {

  public static final String LAMBDA_RUNTIME_REQUEST_ID_HEADER = "Lambda-Runtime-Aws-Request-Id";
  public static final String LAMBDA_RUNTIME_DEADLINE_HEADER = "Lambda-Runtime-Deadline-Ms";
  public static final String LAMBDA_RUNTIME_FUNCTION_ARN_HEADER =
      "Lambda-Runtime-Invoked-Function-Arn";
  public static final String LAMBDA_RUNTIME_TRACE_ID_HEADER = "Lambda-Runtime-Trace-Id";
  public static final String LAMBDA_RUNTIME_CLIENT_CONTEXT_HEADER = "Lambda-Runtime-Client-Context";
  public static final String LAMBDA_RUNTIME_COGNITO_IDENTITY_HEADER =
      "Lambda-Runtime-Cognito-Identity";

  private final RestTemplate client;
  private final String nextEndpoint;
  private final String responseEndpointPattern;
  private final String initErrorEndpoint;
  private final String responseErrorEndpointPattern;

  public AWSLambdaRuntime(RestTemplate client, AWSLambdaUriBuilderFactory factory) {
    this(
        client,
        factory.builder().path("/runtime/invocation/next").build().toString(),
        factory.builder().build().toString() + "/runtime/invocation/{id}/response",
        factory.builder().path("/runtime/init/error").build().toString(),
        factory.builder().build().toString() + "/runtime/invocation/{id}/error");
  }

  private AWSLambdaRuntime(
      RestTemplate client,
      String nextEndpoint,
      String responseEndpointPattern,
      String initErrorEndpoint,
      String responseErrorEndpointPattern) {
    this.client = Objects.requireNonNull(client);
    this.nextEndpoint = Objects.requireNonNull(nextEndpoint);
    this.responseEndpointPattern = Objects.requireNonNull(responseEndpointPattern);
    this.initErrorEndpoint = Objects.requireNonNull(initErrorEndpoint);
    this.responseErrorEndpointPattern = Objects.requireNonNull(responseErrorEndpointPattern);
  }

  public ResponseEntity<Object> getEvent() {
    return client.getForEntity(nextEndpoint, Object.class);
  }

  public void sendResponse(String requestId, AWSLambdaCustomResponse response) {
    client.postForObject(responseEndpointPattern, response, Object.class, requestId);
  }

  public void sendErrorResponse(String requestId, AWSLambdaErrorResponse response) {
    client.postForObject(
        responseErrorEndpointPattern, createErrorEntity(response), Object.class, requestId);
  }

  public void sendErrorInit(AWSLambdaErrorResponse response) {
    client.postForObject(initErrorEndpoint, createErrorEntity(response), Object.class);
  }

  private HttpEntity<Object> createErrorEntity(AWSLambdaErrorResponse response) {
    HttpHeaders headers = new HttpHeaders();
    if (StringUtils.hasText(response.getErrorType())) {
      headers.add("Lambda-Runtime-Function-Error-Type", response.getErrorType());
    }
    return new HttpEntity<>(response, headers);
  }
}
