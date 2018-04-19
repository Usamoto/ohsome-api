package org.heigit.bigspatialdata.ohsome.ohsomeApi.config;

import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**Used to apply auto-intend onto the json response.*/
@Configuration
public class JacksonConfig {

  @Autowired
  private ObjectMapper objectMapper;

  @PostConstruct
  public void setup() {
    objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
  }

}
