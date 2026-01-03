package com.earthguard.earthquake.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    public static final String EARTHQUAKE_EVENTS_TOPIC = "earthquake-events";
    public static final String CRITICAL_ALERTS_TOPIC = "critical-alerts";

    @Bean
    public NewTopic earthquakeEventsTopic() {
        return TopicBuilder.name(EARTHQUAKE_EVENTS_TOPIC)
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic criticalAlertsTopic() {
        return TopicBuilder.name(CRITICAL_ALERTS_TOPIC)
                .partitions(1)
                .replicas(1)
                .build();
    }
}