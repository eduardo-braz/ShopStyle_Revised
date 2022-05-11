package com.compass.ms;

import com.compass.ms.security.AuthenticationService;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;

@SpringBootTest
class CustomerApplicationTests {

    @Bean
    public AuthenticationService AuthenticationService(){
        return new AuthenticationService();
    }

}
