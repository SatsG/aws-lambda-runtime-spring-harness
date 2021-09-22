package io.github.satsg.aws.lambda.runtime.spring.harness.integration.reactive;

import java.util.Collections;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@SpringBootApplication
public class TestWebApplication {

  public static void main(String... args) {
    SpringApplication.run(TestWebApplication.class, args);
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
