package io.github.satsg.aws.lambda.runtime.spring.harness.event.functional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cloud.function.context.FunctionCatalog;
import org.springframework.cloud.function.context.catalog.SimpleFunctionRegistry.FunctionInvocationWrapper;

@ExtendWith(MockitoExtension.class)
class AWSFunctionalEventHandlerTest {

  @Mock private FunctionCatalog catalog;
  @Mock private FunctionNameResolver resolver;
  @Mock private FunctionInputMapper inputMapper;
  @Mock private FunctionOutputMapper outputMapper;

  @Mock private FunctionInvocationWrapper function;

  @InjectMocks private AWSFunctionalEventHandler handler;

  @Test
  void outputMapperResultIsReturned() {
    Object event = new Object();
    Map<String, List<String>> headers = Collections.emptyMap();
    Object output = new Object();

    setUpCall(event, headers, output);

    assertThat(handler.handle(event, headers)).isSameAs(output);
  }

  @Test
  void functionLookupIsDoneOnce() {
    Object event = new Object();
    Map<String, List<String>> headers = Collections.emptyMap();
    Object output = new Object();
    setUpCall(event, headers, output);

    handler.handle(event, headers);
    handler.handle(event, headers);

    verify(catalog).lookup(any());
    verify(resolver).getName();
  }

  @Test
  void functionalContextIsReusedForInput() {
    Object event = new Object();
    Map<String, List<String>> headers = Collections.emptyMap();
    Object output = new Object();
    setUpCall(event, headers, output);

    handler.handle(event, headers);
    handler.handle(event, headers);

    ArgumentCaptor<FunctionalContext> captor = ArgumentCaptor.forClass(FunctionalContext.class);
    verify(inputMapper, times(2)).input(eq(event), eq(headers), captor.capture());

    assertThat(captor.getAllValues()).hasSize(2);
    assertThat(captor.getAllValues().get(0)).isSameAs(captor.getAllValues().get(1));
  }

  @Test
  void functionalContextIsReusedForOutput() {
    Object event = new Object();
    Map<String, List<String>> headers = Collections.emptyMap();
    Object output = new Object();
    setUpCall(event, headers, output);

    handler.handle(event, headers);
    handler.handle(event, headers);

    ArgumentCaptor<FunctionalContext> captor = ArgumentCaptor.forClass(FunctionalContext.class);
    verify(outputMapper, times(2)).output(any(), captor.capture());

    assertThat(captor.getAllValues()).hasSize(2);
    assertThat(captor.getAllValues().get(0)).isSameAs(captor.getAllValues().get(1));
  }

  private void setUpCall(Object event, Map<String, List<String>> headers, Object output) {
    String name = UUID.randomUUID().toString();
    Object input = new Object();
    Object result = new Object();

    given(resolver.getName()).willReturn(name);
    given(catalog.lookup(name)).willReturn(function);
    given(inputMapper.input(eq(event), eq(headers), any())).willReturn(input);
    given(function.apply(input)).willReturn(result);
    given(outputMapper.output(eq(result), any())).willReturn(output);
  }
}
