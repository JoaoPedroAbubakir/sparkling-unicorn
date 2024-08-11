package com.abubakir.timekeeper.service;


import com.abubakir.timekeeper.app.dto.ClockInDTO;
import com.abubakir.timekeeper.app.exception.MaximumRecordsForGivenDayException;
import com.abubakir.timekeeper.app.exception.MinimumLunchTimeException;
import com.abubakir.timekeeper.persistence.repository.TimeSheetRepository;
import com.abubakir.timekeeper.service.repository.TimeSheetSimulatedDatabase;
import org.junit.Before;
import org.junit.Test;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;

public class TimeSheetServiceTest {

    private TimeSheetService timeSheetService;

    @Before
    public void setUp() {
        TimeSheetRepository timeSheetRepository = new TimeSheetSimulatedDatabase();
        KafkaTemplate<String, String> kafkaTemplate = mock(KafkaTemplate.class);
        timeSheetService = new TimeSheetService(timeSheetRepository, kafkaTemplate);
    }

    @Test
    public void shouldThrowMaximumRecordsException() {
        ClockInDTO clockInDTO = ClockInDTO.builder().localDateTime("2024/10/08T00:00:00").build();
        assertThrows(MaximumRecordsForGivenDayException.class, () -> timeSheetService.processTimeStamp(clockInDTO));
    }

    @Test
    public void shouldThrowMinimumLunchTimeException() {
        ClockInDTO clockInDTO = ClockInDTO.builder().localDateTime("2024/10/09T12:50:00").build();
        assertThrows(MinimumLunchTimeException.class, () -> timeSheetService.processTimeStamp(clockInDTO));
    }

    @Test
    public void shouldAllowForCreation() {
        ClockInDTO clockInDTO = ClockInDTO.builder().localDateTime("2024/10/09T13:00:00").build();
        assertThatNoException().isThrownBy(() -> timeSheetService.processTimeStamp(clockInDTO));
    }

    @Test
    public void shouldReturn4() {
        DateTimeFormatter localDateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        int count = timeSheetService.checkHowManyTimesWereLoggedInADay(LocalDate.parse("2024/10/08", localDateFormatter));
        assertThat(count).isEqualTo(4);
    }

    @Test
    public void shouldReturn2() {
        DateTimeFormatter localDateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        int count = timeSheetService.checkHowManyTimesWereLoggedInADay(LocalDate.parse("2024/10/09", localDateFormatter));
        assertThat(count).isEqualTo(2);
    }

    @Test
    public void shouldReturnEmptyAndNotBreak() {
        DateTimeFormatter localDateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        int count = timeSheetService.checkHowManyTimesWereLoggedInADay(LocalDate.parse("2024/10/20", localDateFormatter));
        assertThat(count).isZero();
    }
}