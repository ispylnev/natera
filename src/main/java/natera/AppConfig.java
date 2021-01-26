package natera;

import lombok.extern.java.Log;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.InterceptingClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Collections;

import static java.util.Arrays.asList;

@SpringBootApplication
@Log
public class AppConfig {

    public static void main(String[] args) {
        SpringApplication.run(AppConfig.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate rt =  new RestTemplate();
        rt.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        rt.setRequestFactory(new HttpComponentsClientHttpRequestFactory());

        // Custom error handling
        rt.setErrorHandler(new DefaultResponseErrorHandler() {
            @Override
            protected boolean hasError(HttpStatus statusCode) {
                return false;
            }
        });
        return rt;
    }



}
