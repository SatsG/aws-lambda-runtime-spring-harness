package io.github.satsg.aws.lambda.runtime.spring.harness.event.functional;

public interface FunctionOutputMapper {
  Object output(Object result, FunctionalContext context);
}
