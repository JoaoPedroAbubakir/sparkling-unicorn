package com.abubakir.timekeeper.persistence.repository;

import com.abubakir.timekeeper.persistence.entity.ClockInEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

public interface TimeSheetRepository extends CrudRepository<ClockInEntity, String> {
}
