package io.github.satsg.aws.lambda.runtime.spring.harness.integration.functional;

import io.github.satsg.aws.lambda.runtime.spring.harness.models.FunctionInput;
import io.github.satsg.aws.lambda.runtime.spring.harness.models.FunctionOutput;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.function.context.FunctionalSpringApplication;
import org.springframework.context.annotation.Bean;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@SpringBootApplication
public class TestFunctionalApplication {

  public static void main(String... args) {
    FunctionalSpringApplication.run(TestFunctionalApplication.class, args);
  }

  @Bean
  public Function<FunctionInput, FunctionOutput> basic() {
    return input -> new FunctionOutput(input.getBoop());
  }

  @Bean
  public Function<FunctionInput, Mono<FunctionOutput>> inputToMono() {
    return input -> Mono.just(new FunctionOutput(input.getBoop()));
  }

  @Bean
  public Function<Mono<FunctionInput>, Mono<FunctionOutput>> monoToMono() {
    return mono -> mono.map(input -> new FunctionOutput(input.getBoop()));
  }

  @Bean
  public Function<FunctionInput, Flux<FunctionOutput>> inputToFlux() {
    return input -> Flux.just(new FunctionOutput(input.getBoop()));
  }

  @Bean
  public Function<Flux<FunctionInput>, Flux<FunctionOutput>> fluxToFlux() {
    return flux -> flux.map(input -> new FunctionOutput(input.getBoop()));
  }

  @Bean
  public Supplier<FunctionOutput> supply() {
    return () -> new FunctionOutput("moop");
  }

  @Bean
  public Consumer<FunctionInput> consume() {
    return input -> {};
  }
}
