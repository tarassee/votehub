package com.tarasiuk.votehub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class PollingStationApplication {
    public static void main(String[] args) {
        SpringApplication.run(PollingStationApplication.class, args);
    }
}
