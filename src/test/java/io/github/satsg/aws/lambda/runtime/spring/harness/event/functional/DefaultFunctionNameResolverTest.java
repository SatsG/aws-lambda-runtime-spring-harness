package io.github.satsg.aws.lambda.runtime.spring.harness.event.functional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;

@ExtendWith(MockitoExtension.class)
class DefaultFunctionNameResolverTest {

  @Mock private Environment environment;

  @InjectMocks private DefaultFunctionNameResolver resolver;

  @ParameterizedTest
  @ValueSource(strings = {"DEFAULT_HANDLER", "_HANDLER", "spring.cloud.function.definition"})
  void setVariablesResolvesName(String var) {
    given(environment.containsProperty(anyString())).willReturn(false);
    given(environment.containsProperty(var)).willReturn(true);
    given(environment.getProperty(var)).willReturn("name");

    assertThat(resolver.getName()).isEqualTo("name");
  }

  @Test
  void nullIfNoEnvironmentIsSet() {
    given(environment.containsProperty(anyString())).willReturn(false);

    assertThat(resolver.getName()).isNull();
  }
}
