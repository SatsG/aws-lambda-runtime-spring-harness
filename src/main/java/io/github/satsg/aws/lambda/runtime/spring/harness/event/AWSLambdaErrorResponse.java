package io.github.satsg.aws.lambda.runtime.spring.harness.event;

import java.util.List;

public class AWSLambdaErrorResponse {
  private String errorMessage;
  private String errorType;
  private List<String> stackTrace;

  public AWSLambdaErrorResponse() {}

  public AWSLambdaErrorResponse(String errorMessage, String errorType, List<String> stackTrace) {
    this.errorMessage = errorMessage;
    this.errorType = errorType;
    this.stackTrace = stackTrace;
  }

  public String getErrorMessage() {
    return errorMessage;
  }

  public void setErrorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
  }

  public String getErrorType() {
    return errorType;
  }

  public void setErrorType(String errorType) {
    this.errorType = errorType;
  }

  public List<String> getStackTrace() {
    return stackTrace;
  }

  public void setStackTrace(List<String> stackTrace) {
    this.stackTrace = stackTrace;
  }
}
