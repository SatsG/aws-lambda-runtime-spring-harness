package io.github.satsg.aws.lambda.runtime.spring.harness.event.functional;

import java.lang.reflect.Type;
import org.springframework.cloud.function.context.catalog.FunctionTypeUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class DefaultFunctionOutputMapper implements FunctionOutputMapper {
  @Override
  public Object output(Object result, FunctionalContext context) {
    if (context.isConsumer()) {
      return success();
    }

    Type outputType = context.getOutputType();

    if (FunctionTypeUtils.isMono(outputType)) {
      return ((Mono<?>) result).block();
    }

    if (FunctionTypeUtils.isFlux(outputType)) {
      return ((Flux<?>) result).collectList().block();
    }

    return result;
  }

  private String success() {
    return "\"OK\"";
  }
}
