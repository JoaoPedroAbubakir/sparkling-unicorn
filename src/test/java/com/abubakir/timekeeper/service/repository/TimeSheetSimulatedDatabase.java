package com.abubakir.timekeeper.service.repository;

import com.abubakir.timekeeper.persistence.entity.ClockInEntity;
import com.abubakir.timekeeper.persistence.repository.TimeSheetRepository;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class TimeSheetSimulatedDatabase implements TimeSheetRepository {

    private ArrayList<ClockInEntity> clockInEntities;

    public TimeSheetSimulatedDatabase() {
        this.clockInEntities = new ArrayList<>();
        this.populate();
    }

    private void populate() {
        //This should be a "valid" day
        clockInEntities.add(ClockInEntity.builder().localDateTimeString("2024-10-08T09:00:00").build());
        clockInEntities.add(ClockInEntity.builder().localDateTimeString("2024-10-08T12:00:00").build());
        clockInEntities.add(ClockInEntity.builder().localDateTimeString("2024-10-08T13:00:00").build());
        clockInEntities.add(ClockInEntity.builder().localDateTimeString("2024-10-08T18:00:00").build());

        //These should help with some tests, I really should move this class over
        //By the way, next record for this day should follow the lunch rule
        clockInEntities.add(ClockInEntity.builder().localDateTimeString("2024-10-09T10:00:00").build());
        clockInEntities.add(ClockInEntity.builder().localDateTimeString("2024-10-09T12:00:00").build());
    }

    @Override
    public <S extends ClockInEntity> S save(S entity) {
        this.clockInEntities.add(entity);
        return entity;
    }

    @Override
    public <S extends ClockInEntity> Iterable<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<ClockInEntity> findById(String s) {
        return clockInEntities.stream().filter(clockInEntity -> clockInEntity.getLocalDateTimeString().equals(s)).findFirst();
    }

    @Override
    public boolean existsById(String s) {
        return false;
    }

    @Override
    public Iterable<ClockInEntity> findAll() {
        return this.clockInEntities;
    }

    @Override
    public Iterable<ClockInEntity> findAllById(Iterable<String> localDates) {
        List<ClockInEntity> result = new ArrayList<>();
        for (String localDate : localDates) {
            Optional<ClockInEntity> clockInEntity = findById(localDate);
            clockInEntity.ifPresent(result::add);
        }
        return result;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(String s) {

    }

    @Override
    public void delete(ClockInEntity entity) {
        this.clockInEntities.remove(entity);
    }

    @Override
    public void deleteAllById(Iterable<? extends String> strings) {

    }

    @Override
    public void deleteAll(Iterable<? extends ClockInEntity> entities) {

    }

    @Override
    public void deleteAll() {

    }
}
