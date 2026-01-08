package com.tingeso.debt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class DebtApplication {
    public static void main(String[] args) {
        SpringApplication.run(DebtApplication.class, args);
    }
}
