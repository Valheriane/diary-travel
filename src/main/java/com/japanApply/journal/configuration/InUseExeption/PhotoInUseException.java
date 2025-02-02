package com.japanApply.journal.configuration.InUseExeption;


public class PhotoInUseException extends RuntimeException {
    public PhotoInUseException(String message) {
        super(message);
    }
}
