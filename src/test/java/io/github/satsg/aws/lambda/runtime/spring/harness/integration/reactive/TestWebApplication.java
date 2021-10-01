package io.github.satsg.aws.lambda.runtime.spring.harness.integration.reactive;

import java.util.Collections;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.WebFilter;
import reactor.core.publisher.Mono;

@SpringBootApplication
public class TestWebApplication {

  public static void main(String... args) {
    SpringApplication.run(TestWebApplication.class, args);
  }

  @Bean
  public WebFilter filter1() {
    return (exchange, chain) -> {
      exchange.getResponse().addCookie(ResponseCookie.from("me", "you").build());
      return chain.filter(exchange);
    };
  }

  @Bean
  public WebFilter filter2() {
    return (exchange, chain) -> {
      exchange
          .getResponse()
          .beforeCommit(
              () ->
                  Mono.just(exchange.getResponse())
                      .map(
                          r -> {
                            r.addCookie(ResponseCookie.from("here", "there").build());
                            return r;
                          })
                      .then());
      return chain.filter(exchange);
    };
  }
}

@RestController
class TestController {

  @RequestMapping(
      value = "/test",
      method = {
        RequestMethod.GET,
        RequestMethod.POST,
        RequestMethod.PATCH,
        RequestMethod.PUT,
        RequestMethod.DELETE,
        RequestMethod.HEAD,
        RequestMethod.OPTIONS,
        RequestMethod.TRACE
      })
  public Mono<ResponseEntity<Object>> test(@RequestBody String body) {
    return Mono.just(
        new ResponseEntity<>(Collections.singletonMap("property", "value"), HttpStatus.OK));
  }
}
