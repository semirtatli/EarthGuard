package com.earthguard.earthquake;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "com.earthguard.common.entity")
public class EarthquakeServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(EarthquakeServiceApplication.class, args);
    }
}