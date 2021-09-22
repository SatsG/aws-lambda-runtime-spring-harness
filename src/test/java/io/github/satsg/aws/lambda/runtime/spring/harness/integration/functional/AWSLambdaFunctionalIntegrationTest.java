package io.github.satsg.aws.lambda.runtime.spring.harness.integration.functional;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.satsg.aws.lambda.runtime.spring.harness.integration.IntegrationTest;
import io.github.satsg.aws.lambda.runtime.spring.harness.models.FunctionInput;
import io.github.satsg.aws.lambda.runtime.spring.harness.models.FunctionOutput;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

@TestPropertySource("classpath:application-functional.properties")
class AWSLambdaFunctionalIntegrationTest extends IntegrationTest {

  private static final String DEFAULT_HANDLER_VAR = "DEFAULT_HANDLER";
  private static final String HANDLER_VAR = "_HANDLER";
  private static final String SPRING_FUNC_VAR = "spring.cloud.function.definition";

  @Autowired private GenericApplicationContext context;

  @BeforeEach
  void setUpEach() {
    recreateBeans("handler");
  }

  @ParameterizedTest
  @ValueSource(strings = {DEFAULT_HANDLER_VAR, HANDLER_VAR, SPRING_FUNC_VAR})
  @DirtiesContext
  void invokeFunctionWithEnvironmentSet(String var) throws InterruptedException {
    System.setProperty(var, "basic");
    createEvent(new FunctionInput("woop"));
    FunctionOutput output = getEventLoopResponse(FunctionOutput.class);
    assertThat(output.getSoop()).isEqualTo("woop");
  }

  @Test
  @DirtiesContext
  void invokeInputToMono() throws InterruptedException {
    System.setProperty(SPRING_FUNC_VAR, "inputToMono");
    createEvent(new FunctionInput("woop"));
    FunctionOutput output = getEventLoopResponse(FunctionOutput.class);
    assertThat(output.getSoop()).isEqualTo("woop");
  }

  @Test
  @DirtiesContext
  void invokeInputToFlux() throws InterruptedException {
    System.setProperty(SPRING_FUNC_VAR, "inputToFlux");
    createEvent(new FunctionInput("woop"));
    Object output = getEventLoopResponse(Object.class);
    List<Map<String, Object>> result = (List<Map<String, Object>>) output;
    assertThat(result).hasSize(1);
    assertThat(result.get(0)).containsExactly(Map.entry("soop", "woop"));
  }

  @Test
  @DirtiesContext
  void invokeFluxToFlux() throws InterruptedException {
    System.setProperty(SPRING_FUNC_VAR, "fluxToFlux");
    createEvent(List.of(new FunctionInput("woop"), new FunctionInput("doop")));
    Object output = getEventLoopResponse(Object.class);
    List<Map<String, Object>> result = (List<Map<String, Object>>) output;
    assertThat(result).hasSize(2);
    assertThat(result.get(0)).containsExactly(Map.entry("soop", "woop"));
    assertThat(result.get(1)).containsExactly(Map.entry("soop", "doop"));
  }

  @Test
  @DirtiesContext
  void invokeFluxToFluxForNonCollection() throws InterruptedException {
    System.setProperty(SPRING_FUNC_VAR, "fluxToFlux");
    createEvent(new FunctionInput("woop"));
    Object output = getEventLoopResponse(Object.class);
    List<Map<String, Object>> result = (List<Map<String, Object>>) output;
    assertThat(result).hasSize(1);
    assertThat(result.get(0)).containsExactly(Map.entry("soop", "woop"));
  }

  @Test
  @DirtiesContext
  void invokeMonoToMono() throws InterruptedException {
    System.setProperty(SPRING_FUNC_VAR, "monoToMono");
    createEvent(new FunctionInput("woop"));
    FunctionOutput output = getEventLoopResponse(FunctionOutput.class);
    assertThat(output.getSoop()).isEqualTo("woop");
  }

  @Test
  @DirtiesContext
  void invokeSupplier() throws InterruptedException {
    System.setProperty(SPRING_FUNC_VAR, "supply");
    createEvent(Collections.emptyMap());
    FunctionOutput output = getEventLoopResponse(FunctionOutput.class);
    assertThat(output.getSoop()).isEqualTo("moop");
  }

  @Test
  @DirtiesContext
  void invokeConsumer() throws InterruptedException {
    System.setProperty(SPRING_FUNC_VAR, "consume");
    createEvent(Collections.emptyMap());
    String output = getEventLoopResponse(String.class);
    assertThat(output).isEqualTo("\"OK\"");
  }

  @AfterEach
  void tearDownEach() {
    System.clearProperty(DEFAULT_HANDLER_VAR);
    System.clearProperty(HANDLER_VAR);
    System.clearProperty(SPRING_FUNC_VAR);
  }

  private void recreateBeans(String... beans) {
    Arrays.stream(beans)
        .forEach(
            bean -> {
              BeanDefinition definition = context.getBeanDefinition(bean);
              context.removeBeanDefinition(bean);
              context.registerBeanDefinition(bean, definition);
            });
  }
}
