package com.abubakir.timekeeper.app.exception;

public class RequiredFieldNotPopulatedException extends RuntimeException {
    public RequiredFieldNotPopulatedException() {
        super("Campo obrigatório não informado");
    }

}
