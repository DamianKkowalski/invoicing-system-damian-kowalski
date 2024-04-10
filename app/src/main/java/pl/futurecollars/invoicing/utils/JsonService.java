package pl.futurecollars.invoicing.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.stereotype.Service;

@Service
public class JsonService {

  private final ObjectMapper objectMapper =
      new ObjectMapper().registerModule(new JavaTimeModule())
          .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

  public String toJson(Object object) {
    try {
      return objectMapper.writeValueAsString(object);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  public <T> T toObject(String json, Class<T> clazz) {
    try {
      return objectMapper.readValue(json, clazz);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }
}
