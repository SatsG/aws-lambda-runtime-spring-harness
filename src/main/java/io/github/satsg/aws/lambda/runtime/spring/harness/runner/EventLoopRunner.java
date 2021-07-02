package io.github.satsg.aws.lambda.runtime.spring.harness.runner;

import io.github.satsg.aws.lambda.runtime.spring.harness.event.ServerlessEventLoop;
import org.springframework.boot.CommandLineRunner;

public class EventLoopRunner implements CommandLineRunner {

  private final ServerlessEventLoop eventLoop;

  public EventLoopRunner(ServerlessEventLoop eventLoop) {
    this.eventLoop = eventLoop;
  }

  @Override
  public void run(String... args) throws Exception {
    eventLoop.run();
  }
}
