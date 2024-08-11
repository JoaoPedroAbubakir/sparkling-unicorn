package com.abubakir.timekeeper.configuration;


import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfiguration {

    @Value(value = "${spring.kafka.bootstrap-servers.host}")
    private String bootstrapAddressHost;

    @Value(value = "${spring.kafka.bootstrap-servers.port}")
    private String bootstrapAddressPort;

    @Value(value = "${spring.kafka.custom.topic}")
    private String topicName;

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddressHost + ":" + bootstrapAddressPort);
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic batidasTopic() {
        return new NewTopic(topicName, 1, (short) 1);
    }
}
