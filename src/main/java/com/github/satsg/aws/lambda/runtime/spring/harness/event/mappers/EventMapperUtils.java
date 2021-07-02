package com.github.satsg.aws.lambda.runtime.spring.harness.event.mappers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

class EventMapperUtils {

  static byte[] getBody(ObjectMapper mapper, Object body, Boolean isBase64Encoded)
      throws JsonProcessingException {
    if (body != null) {
      if (isBase64Encoded) {
        body =
            mapper.readValue(
                new String(Base64.getDecoder().decode((String) body), StandardCharsets.UTF_8),
                Object.class);
      }
      return body instanceof String
          ? String.valueOf(body).getBytes(StandardCharsets.UTF_8)
          : mapper.writeValueAsBytes(body);
    }
    return new byte[0];
  }
}
