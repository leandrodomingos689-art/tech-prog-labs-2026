package org.misis.tp.lab3.core;

public class UnknownCodeError extends RuntimeException {
    public UnknownCodeError(String what, String code) {
        super(what + " с кодом '" + code + "' не существует");
    }
}
