package in.artamonov.utilitybill.config;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestBeanConfig {

  @Bean
  public ObjectMapper jsonObjectMapper() {
    ObjectMapper objectMapper = new ObjectMapper()
        .enable(SerializationFeature.INDENT_OUTPUT);
    objectMapper.configure(SerializationFeature.WRITE_DATES_WITH_ZONE_ID, true);
    objectMapper.configure(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE, false);
    objectMapper.setConfig(objectMapper.getSerializationConfig()
        .with(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY));
    JavaTimeModule javaTimeModule = new JavaTimeModule();
    javaTimeModule.addSerializer(ZonedDateTime.class, new ZonedDateTimeSerializer());
    javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ISO_DATE_TIME));
    objectMapper.registerModule(javaTimeModule);
    return objectMapper;
  }

  @Bean
  public XmlMapper xmlObjectMapper() {
    XmlMapper xmlMapper = new XmlMapper();
    xmlMapper.setSerializationInclusion(Include.ALWAYS);
    xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);
    xmlMapper.registerModule(new ParameterNamesModule());
    xmlMapper.registerModule(new Jdk8Module());
    xmlMapper.registerModule(new JavaTimeModule());
    return xmlMapper;
  }

}
