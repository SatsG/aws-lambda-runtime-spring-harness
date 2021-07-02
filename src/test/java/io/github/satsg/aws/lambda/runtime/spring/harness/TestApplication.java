package io.github.satsg.aws.lambda.runtime.spring.harness;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@SpringBootApplication
public class TestApplication {

  public static void main(String... args) {
    SpringApplication.run(TestApplication.class, args);
  }
}

@RestController
class LambdaRuntimeController {

  private static final Logger LOGGER = LoggerFactory.getLogger(LambdaRuntimeController.class);
  private static final Lock EVENT_LOCK = new ReentrantLock();
  private static final Condition EVENT_PRESENT = EVENT_LOCK.newCondition();
  private static final Lock RESPONSES_LOCK = new ReentrantLock();
  private static final Queue<Object> EVENTS = new LinkedBlockingQueue<>();
  private static final Queue<Object> RESPONSES = new LinkedBlockingQueue<>();

  private final ObjectMapper mapper;

  LambdaRuntimeController(ObjectMapper mapper) {
    this.mapper = mapper;
  }

  @PostMapping("/push")
  public void push(@RequestBody Object event) throws JsonProcessingException {
    LOGGER.info("Received event: " + mapper.writeValueAsString(event));
    EVENT_LOCK.lock();
    try {
      EVENTS.add(event);
      EVENT_PRESENT.signalAll();
    } catch (Exception e) {
      LOGGER.error("An error occurred trying to push an event.", e);
    } finally {
      EVENT_LOCK.unlock();
    }
  }

  @GetMapping("/2018-06-01/runtime/invocation/next")
  public ResponseEntity<Object> next() {
    LOGGER.info("In next.");
    Object event = null;
    EVENT_LOCK.lock();
    try {
      while (event == null) {
        if (!EVENTS.isEmpty()) {
          event = EVENTS.remove();
        } else {
          EVENT_PRESENT.await();
        }
      }
      EVENT_PRESENT.signalAll();
    } catch (Exception e) {
      LOGGER.error("An error occurred trying to get an event.", e);
    } finally {
      EVENT_LOCK.unlock();
    }
    HttpHeaders headers = new HttpHeaders();
    headers.add("Lambda-Runtime-Aws-Request-Id", UUID.randomUUID().toString());
    return new ResponseEntity<>(event, headers, 200);
  }

  @PostMapping("/2018-06-01/runtime/invocation/{id}/response")
  public void response(@PathVariable String id, @RequestBody Object response)
      throws JsonProcessingException {
    LOGGER.info("Received response (" + id + "): " + mapper.writeValueAsString(response));
    RESPONSES.add(response);
  }

  @PostMapping("/2018-06-01/runtime/invocation/{id}/error")
  public void error(@PathVariable String id, @RequestBody Object error)
      throws JsonProcessingException {
    LOGGER.info("Received error (" + id + "): " + mapper.writeValueAsString(error));
    RESPONSES.add(error);
  }

  @PostMapping("/2018-06-01/runtime/init/error")
  public void response(@RequestBody Object error) throws JsonProcessingException {
    LOGGER.info("Received error: " + mapper.writeValueAsString(error));
    RESPONSES.add(error);
  }

  @GetMapping("/pull")
  public ResponseEntity<Object> pull() {
    ResponseEntity<Object> result = new ResponseEntity<>(HttpStatus.NOT_FOUND);
    RESPONSES_LOCK.lock();
    try {
      if (!RESPONSES.isEmpty()) {
        result = new ResponseEntity<>(RESPONSES.remove(), HttpStatus.OK);
      }
    } catch (Exception e) {
      LOGGER.error("An error occurred trying to get a response.", e);
    } finally {
      RESPONSES_LOCK.unlock();
    }
    return result;
  }

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
    return Mono.just(new ResponseEntity<>("\"Bingus\"", HttpStatus.OK));
  }
}
