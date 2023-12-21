package com.icwd.user.service.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class MyConfig {

    @Bean
    @LoadBalanced // is used to do load balancing and use full call service with name and without port
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
