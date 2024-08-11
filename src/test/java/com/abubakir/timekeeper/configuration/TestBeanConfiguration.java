package com.abubakir.timekeeper.configuration;

import com.abubakir.timekeeper.app.controller.TimeSheetController;
import com.abubakir.timekeeper.persistence.repository.TimeSheetRepository;
import com.abubakir.timekeeper.service.TimeSheetService;
import com.abubakir.timekeeper.service.repository.TimeSheetSimulatedDatabase;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.KafkaTemplate;

@TestConfiguration
@Profile("test")
public class TestBeanConfiguration {

    @Bean("simulatedDatabase")
    @Primary
    public TimeSheetRepository timeSheetRepository() {
        return new TimeSheetSimulatedDatabase();
    }

    @Bean
    public TimeSheetService timeSheetService(TimeSheetRepository timeSheetRepository, KafkaTemplate<String, String> kafkaTemplate) {
        return new TimeSheetService(timeSheetRepository, kafkaTemplate);
    }

    @Bean
    public TimeSheetController timeSheetController(TimeSheetService timeSheetService) {
        return new TimeSheetController(timeSheetService);
    }

}
