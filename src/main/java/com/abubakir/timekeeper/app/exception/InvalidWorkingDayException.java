package com.abubakir.timekeeper.app.exception;

public class InvalidWorkingDayException extends RuntimeException{
    public InvalidWorkingDayException() {
        super("Sábado e domingo não são permitidos como dia de trabalho");
    }
}
