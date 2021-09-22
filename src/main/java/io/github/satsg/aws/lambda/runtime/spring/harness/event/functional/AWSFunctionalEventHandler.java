package io.github.satsg.aws.lambda.runtime.spring.harness.event.functional;

import io.github.satsg.aws.lambda.runtime.spring.harness.event.AWSEventHandler;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.springframework.cloud.function.context.FunctionCatalog;
import org.springframework.cloud.function.context.catalog.SimpleFunctionRegistry.FunctionInvocationWrapper;
import org.springframework.util.StringUtils;

public class AWSFunctionalEventHandler implements AWSEventHandler {

  private final FunctionCatalog catalog;
  private final FunctionNameResolver resolver;
  private final FunctionInputMapper inputMapper;
  private final FunctionOutputMapper outputMapper;

  private FunctionInvocationWrapper function;
  private FunctionalContext context;

  public AWSFunctionalEventHandler(
      FunctionCatalog catalog,
      FunctionNameResolver resolver,
      FunctionInputMapper inputMapper,
      FunctionOutputMapper outputMapper) {
    this.catalog = Objects.requireNonNull(catalog);
    this.resolver = Objects.requireNonNull(resolver);
    this.inputMapper = Objects.requireNonNull(inputMapper);
    this.outputMapper = Objects.requireNonNull(outputMapper);
  }

  @Override
  public Object handle(Object event, Map<String, List<String>> headers) {
    FunctionInvocationWrapper function = lookup();
    Object input = inputMapper.input(event, headers, context);
    Object result = function.apply(input);
    return outputMapper.output(result, context);
  }

  private FunctionInvocationWrapper lookup() {
    if (function == null) {
      String name = resolveFunctionName();
      function = catalog.lookup(name);
      if (function == null) {
        throw new RuntimeException("Unable to locate function with name: " + name);
      }
      context = new FunctionalContext(function);
    }
    return function;
  }

  private String resolveFunctionName() {
    String name = resolver.getName();
    if (!StringUtils.hasText(name)) {
      throw new RuntimeException("Unable to resolve function name.");
    }
    return name;
  }
}
