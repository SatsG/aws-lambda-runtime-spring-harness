package io.github.satsg.aws.lambda.runtime.spring.harness.event.reactive;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.server.reactive.ServerHttpRequest;

@ExtendWith(MockitoExtension.class)
class AWSReactiveEventResolverTest {

  @Mock private ReactiveEventMapper mapper1;
  @Mock private ReactiveEventMapper mapper2;

  private AWSReactiveEventResolver resolver;

  @BeforeEach
  void setUpEach() {
    given(mapper1.matches(any())).willReturn(false);
    resolver = new AWSReactiveEventResolver(Arrays.asList(mapper1, mapper2));
  }

  @Test
  void correctRequestIsReturned(@Mock ServerHttpRequest request) {
    given(mapper2.matches(any())).willReturn(true);
    given(mapper2.compose(any())).willReturn(request);
    assertThat(resolver.resolve(new Object())).isSameAs(request);
  }

  @Test
  void exceptionOnNoMatch() {
    given(mapper2.matches(any())).willReturn(false);
    assertThatThrownBy(() -> resolver.resolve(new Object()))
        .hasMessage("Event doesn't match any supported mappings.");
  }
}
