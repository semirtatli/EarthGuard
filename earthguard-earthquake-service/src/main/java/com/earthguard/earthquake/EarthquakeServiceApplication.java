package com.earthguard.earthquake;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EntityScan(basePackages = "com.earthguard.common.entity")
@EnableScheduling
public class EarthquakeServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(EarthquakeServiceApplication.class, args);
    }
}