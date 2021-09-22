package io.github.satsg.aws.lambda.runtime.spring.harness.event.functional;

import java.lang.reflect.Type;
import java.util.Objects;
import org.springframework.cloud.function.context.catalog.SimpleFunctionRegistry.FunctionInvocationWrapper;

public class FunctionalContext {
  private final FunctionInvocationWrapper function;

  public FunctionalContext(FunctionInvocationWrapper function) {
    this.function = Objects.requireNonNull(function);
  }

  public String getFunctionName() {
    return function.getFunctionDefinition();
  }

  public Type getInputType() {
    return function.getInputType();
  }

  public Type getInternalInputType() {
    return function.getItemType(getInputType());
  }

  public Type getOutputType() {
    return function.getOutputType();
  }

  public Type getInternalOutputType() {
    return function.getItemType(getOutputType());
  }

  public boolean isFunction() {
    return function.isFunction();
  }

  public boolean isSupplier() {
    return function.isSupplier();
  }

  public boolean isConsumer() {
    return function.isConsumer();
  }

  public boolean isRoutingFunction() {
    return function.isRoutingFunction();
  }
}
