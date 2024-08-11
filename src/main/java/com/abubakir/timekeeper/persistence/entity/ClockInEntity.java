package com.abubakir.timekeeper.persistence.entity;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash("clockIn")
@Builder
@Getter
public class ClockInEntity {

    @Id
    private String localDateTimeString;

}
