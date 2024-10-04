package com.github.uber_eat_cloneapi1.config;


import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class InfobipConfig {

    @Value("${infobip.api.key}")
    private String apiKey;

    @Value("${infobip.api.base-url}")
    private String baseURL;


}

