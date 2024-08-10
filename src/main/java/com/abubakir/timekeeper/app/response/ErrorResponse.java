package com.abubakir.timekeeper.app.response;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.io.Serializable;

@Data
@Builder
@Jacksonized
public class ErrorResponse implements Serializable {

    @JsonProperty("mensagem")
    private String message;

}
