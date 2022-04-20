package com.compass.ms.components;

import org.modelmapper.ModelMapper;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class SpringComponents {

    @Bean
    @LoadBalanced
    public RestTemplate getRestTamplate(){ return new RestTemplate(); }

    @Bean
    public ModelMapper mapper(){
        return new ModelMapper();
    }
}
