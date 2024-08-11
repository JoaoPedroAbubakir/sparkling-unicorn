package com.abubakir.timekeeper.service;

import com.abubakir.timekeeper.app.dto.ClockInDTO;
import com.abubakir.timekeeper.app.exception.DuplicateRecordException;
import com.abubakir.timekeeper.app.exception.InvalidWorkingDayException;
import com.abubakir.timekeeper.app.exception.MaximumRecordsForGivenDayException;
import com.abubakir.timekeeper.app.exception.MinimumLunchTimeException;
import com.abubakir.timekeeper.persistence.entity.ClockInEntity;
import com.abubakir.timekeeper.persistence.repository.TimeSheetRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@AllArgsConstructor
public class TimeSheetService {

    TimeSheetRepository timeSheetRepository;

    public void processTimeStamp(ClockInDTO clockInDTO) {
        DateTimeFormatter localDateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss");
        LocalDateTime localDateTime = LocalDateTime.parse(clockInDTO.getLocalDateTime(), localDateTimeFormatter);
        validateDayOfTheWeek(localDateTime.toLocalDate());
        validateDateNotInserted(localDateTime);
        validateNumberOfRecordsForGivenDate(localDateTime.toLocalDate());
        validateIfLunch(localDateTime);
        save(clockInDTO);

    }

    private void save(ClockInDTO clockInDTO) {
        ClockInEntity entity = ClockInEntity.builder().localDateTimeString(clockInDTO.getLocalDateTime()).build();
        timeSheetRepository.save(entity);
    }

    private void validateDateNotInserted(LocalDateTime localDateTime) {
        DateTimeFormatter localDateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss");
        timeSheetRepository.findById(localDateTime.format(localDateTimeFormatter)).ifPresent(clockInEntity -> {throw new DuplicateRecordException();});
    }

    private void validateNewRecordIsAtLeastOneHourAfterLast(LocalDateTime localDateTime) {
        List<ClockInEntity> timesForGivenDay = getTimesForGivenDay(localDateTime.toLocalDate());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss");
        LocalTime nextTime = localDateTime.toLocalTime();
        ClockInEntity lastClockIn = timesForGivenDay.get(timesForGivenDay.size() - 1);
        LocalTime lastTime = LocalTime.parse(lastClockIn.getLocalDateTimeString(), formatter);
        if (nextTime.isBefore(lastTime.plusHours(1))) {
            throw new MinimumLunchTimeException();
        }
    }

    public List<ClockInEntity> getTimesForGivenDay(LocalDate localDate) {
        Iterable<ClockInEntity> allById = timeSheetRepository.findAll();
        Map<LocalDate, List<ClockInEntity>> groupedByDate = splitByDay(allById);
        return groupedByDate.getOrDefault(localDate, Collections.emptyList());
    }

    public int checkHowManyTimesWereLoggedInADay(LocalDate localDate) {
        List<ClockInEntity> groupedByDate = getTimesForGivenDay(localDate);
        return groupedByDate.size();
    }

    private static Map<LocalDate, List<ClockInEntity>> splitByDay(Iterable<ClockInEntity> clockInEntities) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd'T'HH:mm:ss");
        Map<LocalDate, List<ClockInEntity>> groupedByDate = new HashMap<>();
        for (ClockInEntity clockInEntity : clockInEntities) {
            LocalDateTime localDateTime = LocalDateTime.parse(clockInEntity.getLocalDateTimeString(), formatter);
            LocalDate localDate = localDateTime.toLocalDate();
            groupedByDate.computeIfAbsent(localDate, k -> new ArrayList<>()).add(clockInEntity);
        }
        return groupedByDate;
    }

    private void validateDayOfTheWeek(LocalDate localDate) {
        DayOfWeek dayOfWeek = localDate.getDayOfWeek();
        if (dayOfWeek.equals(DayOfWeek.SATURDAY) || dayOfWeek.equals(DayOfWeek.SUNDAY)) {
            throw new InvalidWorkingDayException();
        }
    }

    private void validateNumberOfRecordsForGivenDate(LocalDate localDate) {
        if (checkHowManyTimesWereLoggedInADay(localDate) == 4) {
            throw new MaximumRecordsForGivenDayException();
        }
    }

    private void validateIfLunch(LocalDateTime localDateTime) {
        if (checkHowManyTimesWereLoggedInADay(localDateTime.toLocalDate()) == 2) {
            validateNewRecordIsAtLeastOneHourAfterLast(localDateTime);
        }
    }

}
