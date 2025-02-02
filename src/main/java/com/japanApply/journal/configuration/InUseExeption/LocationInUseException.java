package com.japanApply.journal.configuration.InUseExeption;


public class LocationInUseException extends RuntimeException {
    public LocationInUseException(String message) {
        super(message);
    }
}
