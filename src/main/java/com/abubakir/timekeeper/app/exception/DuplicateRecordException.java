package com.abubakir.timekeeper.app.exception;

public class DuplicateRecordException extends RuntimeException{
    public DuplicateRecordException(){
        super("Horários já registrado");
    }
}
