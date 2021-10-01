package io.github.satsg.aws.lambda.runtime.spring.harness.event.http.reactive;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.RequestPath;
import org.springframework.util.MultiValueMapAdapter;
import org.springframework.web.util.DefaultUriBuilderFactory;

class ReactiveEventServerHttpRequestTest {

  private ReactiveEventServerHttpRequest request;

  @BeforeEach
  void setUpEach() {
    request =
        new ReactiveEventServerHttpRequest(
            new DefaultDataBufferFactory(),
            "request-id",
            HttpMethod.GET,
            new DefaultUriBuilderFactory()
                .builder()
                .path("/path")
                .queryParam("q1", "value1")
                .queryParam("q1", "value2")
                .queryParam("q2", (String) null)
                .queryParam("q3", "")
                .build(),
            new HttpHeaders(
                new MultiValueMapAdapter<>(
                    Map.of(
                        "h1",
                        Collections.singletonList("value1"),
                        "h2",
                        Collections.singletonList(""),
                        "h3",
                        Collections.singletonList(null),
                        "h4",
                        Arrays.asList("value1", "value2"),
                        "Cookie",
                        Arrays.asList(
                            null,
                            "",
                            "name1=value1",
                            "name2=value2; name3=; name4=value4",
                            "name5=")))),
            new byte[0]);
  }

  @Test
  void requestIdIsCorrect() {
    assertThat(request.getId()).isEqualTo("request-id");
  }

  @Test
  void methodIsCorrect() {
    assertThat(request.getMethodValue()).isEqualTo("GET");
  }

  @Test
  void pathIsCorrect() {
    assertThat(request.getPath()).isEqualTo(RequestPath.parse("/path", ""));
  }

  @Test
  void queryIsCorrect() {
    assertThat(request.getQueryParams()).containsKey("q1");
    assertThat(request.getQueryParams().get("q1")).containsExactly("value1", "value2");
    assertThat(request.getQueryParams()).containsKey("q2");
    assertThat(request.getQueryParams().get("q2")).containsExactly("");
    assertThat(request.getQueryParams()).containsKey("q3");
    assertThat(request.getQueryParams().get("q3")).containsExactly("");
  }

  @Test
  void headersIsCorrect() {
    assertThat(request.getHeaders()).containsKey("h1");
    assertThat(request.getHeaders().get("h1")).containsExactly("value1");
    assertThat(request.getHeaders()).containsKey("h2");
    assertThat(request.getHeaders().get("h2")).containsExactly("");
    assertThat(request.getHeaders()).containsKey("h3");
    assertThat(request.getHeaders().get("h3")).containsExactly((String) null);
    assertThat(request.getHeaders()).containsKey("h4");
    assertThat(request.getHeaders().get("h4")).containsExactly("value1", "value2");
  }

  @Test
  void bodyIsCorrect() {
    assertThat(request.getBody().blockLast().asByteBuffer().array()).isEmpty();
  }

  @Test
  void cookiesAreCorrect() {
    assertThat(request.getCookies()).containsKey("name1");
    assertThat(request.getCookies().get("name1"))
        .containsExactly(new HttpCookie("name1", "value1"));
    assertThat(request.getCookies()).containsKey("name2");
    assertThat(request.getCookies().get("name2"))
        .containsExactly(new HttpCookie("name2", "value2"));
    assertThat(request.getCookies()).containsKey("name3");
    assertThat(request.getCookies().get("name3")).containsExactly(new HttpCookie("name3", ""));
    assertThat(request.getCookies()).containsKey("name4");
    assertThat(request.getCookies().get("name4"))
        .containsExactly(new HttpCookie("name4", "value4"));
    assertThat(request.getCookies()).containsKey("name5");
    assertThat(request.getCookies().get("name5")).containsExactly(new HttpCookie("name5", ""));
  }
}
