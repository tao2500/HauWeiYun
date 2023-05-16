package com.bdc.reservationService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class reservationServiceApplication {
    public static void main(String[] args){
        SpringApplication.run(reservationServiceApplication.class, args);
    }
}
