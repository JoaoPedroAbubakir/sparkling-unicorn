package com.abubakir.timekeeper.app.exception;

public class MinimumLunchTimeException extends RuntimeException {
    public MinimumLunchTimeException() {
        super("Deve haver no mínimo 1 hora de almoço");
    }
}
