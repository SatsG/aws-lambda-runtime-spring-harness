package io.github.satsg.aws.lambda.runtime.spring.harness.event.functional;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.cloud.function.context.catalog.FunctionTypeUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class DefaultFunctionInputMapper implements FunctionInputMapper {
  private final ObjectMapper mapper;

  public DefaultFunctionInputMapper(ObjectMapper mapper) {
    this.mapper = Objects.requireNonNull(mapper);
  }

  @Override
  public Object input(Object event, Map<String, List<String>> headers, FunctionalContext context) {
    if (context.isSupplier()) {
      return null;
    }

    Type inputType = context.getInputType();

    if (FunctionTypeUtils.isMono(inputType)) {
      return Mono.just(convertEventToType(event, context.getInternalInputType()));
    }

    if (FunctionTypeUtils.isFlux(inputType)) {
      if (event instanceof Collection<?>) {
        return Flux.fromIterable(
            ((Collection<?>) event)
                .stream()
                    .map(e -> convertEventToType(e, context.getInternalInputType()))
                    .collect(Collectors.toList()));
      } else {
        return Flux.just(convertEventToType(event, context.getInternalInputType()));
      }
    }

    return convertEventToType(event, context.getInternalInputType());
  }

  private Object convertEventToType(Object event, Type type) {
    return mapper.convertValue(event, mapper.getTypeFactory().constructType(type));
  }
}
