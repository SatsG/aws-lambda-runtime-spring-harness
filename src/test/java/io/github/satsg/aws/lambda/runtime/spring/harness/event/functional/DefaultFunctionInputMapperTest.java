package io.github.satsg.aws.lambda.runtime.spring.harness.event.functional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.SimpleType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import java.lang.reflect.Type;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
class DefaultFunctionInputMapperTest {

  @Mock private ObjectMapper mapper;
  @Mock private FunctionalContext context;
  @Mock private TypeFactory typeFactory;

  @InjectMocks private DefaultFunctionInputMapper inputMapper;

  @Test
  void inputMapperForSupplierIsNull() {
    given(context.isSupplier()).willReturn(true);
    Object result = inputMapper.input(null, null, context);
    assertThat(result).isNull();
  }

  @Test
  void inputMapperForMonoConvertsCorrectly() {
    setUpMapper();
    given(context.isSupplier()).willReturn(false);
    given(context.getInputType()).willReturn(Mono.class);
    given(context.getInternalInputType()).willReturn(Object.class);

    Object result = inputMapper.input(new Object(), null, context);

    assertThat(result).isInstanceOf(Mono.class);
  }

  @Test
  void inputMapperForFluxConvertsCorrectly() {
    setUpMapper();
    given(context.isSupplier()).willReturn(false);
    given(context.getInputType()).willReturn(Flux.class);
    given(context.getInternalInputType()).willReturn(Object.class);

    Object result = inputMapper.input(new Object(), null, context);

    assertThat(result).isInstanceOf(Flux.class);
  }

  private void setUpMapper() {
    given(mapper.getTypeFactory()).willReturn(typeFactory);
    given(typeFactory.constructType(any(Type.class)))
        .willReturn(SimpleType.constructUnsafe(Object.class));
    given(mapper.convertValue(any(), any(JavaType.class))).willReturn(new Object());
  }
}
