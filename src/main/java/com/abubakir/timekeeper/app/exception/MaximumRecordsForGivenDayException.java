package com.abubakir.timekeeper.app.exception;

public class MaximumRecordsForGivenDayException extends RuntimeException {
    public MaximumRecordsForGivenDayException() {
        super("Apenas 4 horários podem ser registrados por dia");
    }
}
