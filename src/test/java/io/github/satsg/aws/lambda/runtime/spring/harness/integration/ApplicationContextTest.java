package io.github.satsg.aws.lambda.runtime.spring.harness.integration;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.satsg.aws.lambda.runtime.spring.harness.config.EventHttpHandlerAutoConfiguration;
import io.github.satsg.aws.lambda.runtime.spring.harness.config.EventRunnerAutoConfiguration;
import io.github.satsg.aws.lambda.runtime.spring.harness.config.LambdaEventLoopAutoConfiguration;
import io.github.satsg.aws.lambda.runtime.spring.harness.config.LambdaReactiveEventAutoConfiguration;
import io.github.satsg.aws.lambda.runtime.spring.harness.event.AWSEventHandler;
import io.github.satsg.aws.lambda.runtime.spring.harness.event.AWSLambdaCustomResponse;
import io.github.satsg.aws.lambda.runtime.spring.harness.event.AWSLambdaRuntime;
import io.github.satsg.aws.lambda.runtime.spring.harness.event.AWSLambdaUriBuilderFactory;
import io.github.satsg.aws.lambda.runtime.spring.harness.event.EventErrorMapper;
import io.github.satsg.aws.lambda.runtime.spring.harness.event.LoopCondition;
import io.github.satsg.aws.lambda.runtime.spring.harness.event.ServerlessEventLoop;
import io.github.satsg.aws.lambda.runtime.spring.harness.event.reactive.ReactiveEventMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.core.ReactiveAdapterRegistry;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.web.reactive.DispatcherHandler;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.accept.RequestedContentTypeResolver;
import org.springframework.web.reactive.function.server.support.HandlerFunctionAdapter;
import org.springframework.web.reactive.function.server.support.RouterFunctionMapping;
import org.springframework.web.reactive.function.server.support.ServerResponseResultHandler;
import org.springframework.web.reactive.resource.ResourceUrlProvider;
import org.springframework.web.reactive.result.SimpleHandlerAdapter;
import org.springframework.web.reactive.result.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.reactive.result.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.reactive.result.method.annotation.ResponseBodyResultHandler;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityResultHandler;
import org.springframework.web.reactive.result.view.ViewResolutionResultHandler;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;
import org.springframework.web.server.WebExceptionHandler;
import org.springframework.web.server.i18n.LocaleContextResolver;
import reactor.core.publisher.Mono;

public class ApplicationContextTest {

  private final ApplicationContextRunner context = new ApplicationContextRunner();

  @Test
  void reactiveDependenciesAreRegistered() {
    context
        .withUserConfiguration(EventHttpHandlerAutoConfiguration.class)
        .withPropertyValues(
            "satsg.enable.aws.lambda.runtime.configuration=true",
            "satsg.enable.aws.lambda.runtime.reactive=true")
        .run(
            ctx -> {
              assertThat(ctx).hasBean("httpHandler");
              assertThat(ctx).getBean("httpHandler").isInstanceOf(HttpHandler.class);

              assertThat(ctx).hasBean("webHandler");
              assertThat(ctx).getBean("webHandler").isInstanceOf(DispatcherHandler.class);

              assertThat(ctx).hasBean("responseStatusExceptionHandler");
              assertThat(ctx)
                  .getBean("responseStatusExceptionHandler")
                  .isInstanceOf(WebExceptionHandler.class);

              assertThat(ctx).hasBean("requestMappingHandlerMapping");
              assertThat(ctx)
                  .getBean("requestMappingHandlerMapping")
                  .isInstanceOf(RequestMappingHandlerMapping.class);

              assertThat(ctx).hasBean("webFluxContentTypeResolver");
              assertThat(ctx)
                  .getBean("webFluxContentTypeResolver")
                  .isInstanceOf(RequestedContentTypeResolver.class);

              assertThat(ctx).hasBean("routerFunctionMapping");
              assertThat(ctx)
                  .getBean("routerFunctionMapping")
                  .isInstanceOf(RouterFunctionMapping.class);

              assertThat(ctx).hasBean("resourceHandlerMapping");
              assertThat(ctx).getBean("resourceHandlerMapping").isInstanceOf(HandlerMapping.class);

              assertThat(ctx).hasBean("resourceUrlProvider");
              assertThat(ctx)
                  .getBean("resourceUrlProvider")
                  .isInstanceOf(ResourceUrlProvider.class);

              assertThat(ctx).hasBean("requestMappingHandlerAdapter");
              assertThat(ctx)
                  .getBean("requestMappingHandlerAdapter")
                  .isInstanceOf(RequestMappingHandlerAdapter.class);

              assertThat(ctx).hasBean("serverCodecConfigurer");
              assertThat(ctx)
                  .getBean("serverCodecConfigurer")
                  .isInstanceOf(ServerCodecConfigurer.class);

              assertThat(ctx).hasBean("localeContextResolver");
              assertThat(ctx)
                  .getBean("localeContextResolver")
                  .isInstanceOf(LocaleContextResolver.class);

              assertThat(ctx).hasBean("webFluxConversionService");
              assertThat(ctx)
                  .getBean("webFluxConversionService")
                  .isInstanceOf(FormattingConversionService.class);

              assertThat(ctx).hasBean("webFluxAdapterRegistry");
              assertThat(ctx)
                  .getBean("webFluxAdapterRegistry")
                  .isInstanceOf(ReactiveAdapterRegistry.class);

              assertThat(ctx).hasBean("handlerFunctionAdapter");
              assertThat(ctx)
                  .getBean("handlerFunctionAdapter")
                  .isInstanceOf(HandlerFunctionAdapter.class);

              assertThat(ctx).hasBean("simpleHandlerAdapter");
              assertThat(ctx)
                  .getBean("simpleHandlerAdapter")
                  .isInstanceOf(SimpleHandlerAdapter.class);

              assertThat(ctx).hasBean("webFluxWebSocketHandlerAdapter");
              assertThat(ctx)
                  .getBean("webFluxWebSocketHandlerAdapter")
                  .isInstanceOf(WebSocketHandlerAdapter.class);

              assertThat(ctx).hasBean("responseEntityResultHandler");
              assertThat(ctx)
                  .getBean("responseEntityResultHandler")
                  .isInstanceOf(ResponseEntityResultHandler.class);

              assertThat(ctx).hasBean("responseBodyResultHandler");
              assertThat(ctx)
                  .getBean("responseBodyResultHandler")
                  .isInstanceOf(ResponseBodyResultHandler.class);

              assertThat(ctx).hasBean("viewResolutionResultHandler");
              assertThat(ctx)
                  .getBean("viewResolutionResultHandler")
                  .isInstanceOf(ViewResolutionResultHandler.class);

              assertThat(ctx).hasBean("serverResponseResultHandler");
              assertThat(ctx)
                  .getBean("serverResponseResultHandler")
                  .isInstanceOf(ServerResponseResultHandler.class);
            });
  }

  @Test
  void eventLoopIsRegistered() {
    context
        .withUserConfiguration(EventRunnerAutoConfiguration.class)
        .withPropertyValues("satsg.enable.aws.lambda.runtime=true")
        .withBean(ServerlessEventLoop.class, () -> () -> {})
        .run(
            ctx -> {
              assertThat(ctx).hasBean("eventLoopRunner");
              assertThat(ctx).getBean("eventLoopRunner").isInstanceOf(CommandLineRunner.class);
            });
  }

  @Test
  void eventLoopRuntimeConfigurationIsRegistered() {
    context
        .withUserConfiguration(LambdaEventLoopAutoConfiguration.class)
        .withPropertyValues(
            "satsg.enable.aws.lambda.runtime.configuration=true",
            "AWS_LAMBDA_RUNTIME_API=localhost:9090",
            "satsg.aws.lambda.runtime.version=2018-06-01")
        .withBean(AWSEventHandler.class, () -> ignored -> new AWSLambdaCustomResponse())
        .withBean(ObjectMapper.class, ObjectMapper::new)
        .run(
            ctx -> {
              assertThat(ctx).hasBean("factory");
              assertThat(ctx).getBean("factory").isInstanceOf(DataBufferFactory.class);

              assertThat(ctx).hasBean("awsLambdaUriBuilderFactory");
              assertThat(ctx)
                  .getBean("awsLambdaUriBuilderFactory")
                  .isInstanceOf(AWSLambdaUriBuilderFactory.class);

              assertThat(ctx).hasBean("runtime");
              assertThat(ctx).getBean("runtime").isInstanceOf(AWSLambdaRuntime.class);

              assertThat(ctx).hasBean("eventErrorMapper");
              assertThat(ctx).getBean("eventErrorMapper").isInstanceOf(EventErrorMapper.class);

              assertThat(ctx).hasBean("condition");
              assertThat(ctx).getBean("condition").isInstanceOf(LoopCondition.class);

              assertThat(ctx).hasBean("eventLoop");
              assertThat(ctx).getBean("eventLoop").isInstanceOf(ServerlessEventLoop.class);
            });
  }

  @Test
  void reactiveEventConfigurationIsRegistered() {
    context
        .withUserConfiguration(LambdaReactiveEventAutoConfiguration.class)
        .withPropertyValues(
            "satsg.enable.aws.lambda.runtime.configuration=true",
            "satsg.enable.aws.lambda.runtime.reactive=true")
        .withBean(DataBufferFactory.class, DefaultDataBufferFactory::new)
        .withBean(HttpHandler.class, () -> (req, res) -> Mono.empty())
        .run(
            ctx -> {
              assertThat(ctx).hasBean("apiGatewayV1EventMapper");
              assertThat(ctx)
                  .getBean("apiGatewayV1EventMapper")
                  .isInstanceOf(ReactiveEventMapper.class);

              assertThat(ctx).hasBean("handler");
              assertThat(ctx).getBean("handler").isInstanceOf(AWSEventHandler.class);
            });
  }
}