package io.github.satsg.aws.lambda.runtime.spring.harness.event.functional;

import java.util.List;
import java.util.Map;

public interface FunctionInputMapper {
  Object input(Object event, Map<String, List<String>> headers, FunctionalContext context);
}
