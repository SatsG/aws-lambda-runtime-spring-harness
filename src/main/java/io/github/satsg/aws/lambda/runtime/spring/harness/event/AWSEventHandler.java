package io.github.satsg.aws.lambda.runtime.spring.harness.event;

import java.util.List;
import java.util.Map;

public interface AWSEventHandler {
  Object handle(Object event, Map<String, List<String>> headers);
}
