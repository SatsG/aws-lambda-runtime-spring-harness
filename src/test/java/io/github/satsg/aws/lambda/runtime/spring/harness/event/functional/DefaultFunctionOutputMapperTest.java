package io.github.satsg.aws.lambda.runtime.spring.harness.event.functional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
class DefaultFunctionOutputMapperTest {

  @Mock private FunctionalContext context;

  @InjectMocks private DefaultFunctionOutputMapper outputMapper;

  @Test
  void consumerGivesDefaultSuccess() {
    given(context.isConsumer()).willReturn(true);
    Object result = outputMapper.output(null, context);
    assertThat(result).isEqualTo("\"OK\"");
  }

  @Test
  void monoIsUnwrapped() {
    given(context.isConsumer()).willReturn(false);
    given(context.getOutputType()).willReturn(Mono.class);

    Object output = new Object();
    Object result = outputMapper.output(Mono.just(output), context);
    assertThat(result).isSameAs(output);
  }

  @Test
  void fluxIsUnwrapped() {
    given(context.isConsumer()).willReturn(false);
    given(context.getOutputType()).willReturn(Flux.class);

    Object output = new Object();
    Object result = outputMapper.output(Flux.just(output), context);
    List<Object> results = (List<Object>) result;

    assertThat(results).containsExactly(output);
  }

  @Test
  void outputIsReturned() {
    given(context.isConsumer()).willReturn(false);
    given(context.getOutputType()).willReturn(Object.class);

    Object output = new Object();
    Object result = outputMapper.output(output, context);

    assertThat(result).isSameAs(output);
  }
}
