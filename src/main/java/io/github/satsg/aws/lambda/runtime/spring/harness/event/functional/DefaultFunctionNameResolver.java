package io.github.satsg.aws.lambda.runtime.spring.harness.event.functional;

import java.util.List;
import java.util.Objects;
import org.springframework.core.env.Environment;

public class DefaultFunctionNameResolver implements FunctionNameResolver {

  private static final List<String> NAME_LOCATIONS =
      List.of("DEFAULT_HANDLER", "_HANDLER", "spring.cloud.function.definition");

  private final Environment environment;

  public DefaultFunctionNameResolver(Environment environment) {
    this.environment = Objects.requireNonNull(environment);
  }

  @Override
  public String getName() {
    return NAME_LOCATIONS.stream()
        .filter(environment::containsProperty)
        .map(environment::getProperty)
        .findFirst()
        .orElse(null);
  }
}
