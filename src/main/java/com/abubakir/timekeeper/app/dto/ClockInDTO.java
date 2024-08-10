package com.abubakir.timekeeper.app.dto;

import com.abubakir.timekeeper.app.validator.constraints.ValidLocalDateTime;
import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
public class ClockInDTO {

    @JsonAlias("dataHora")
    @NotNull
    @ValidLocalDateTime
    private String localDateTime;


}
