package com.mitchellbosecke.pebble.error;

public class ClassAccessException extends PebbleException {

    private static final long serialVersionUID = 5109892021088141417L;

    public ClassAccessException(Integer lineNumber, String filename) {
        super(null, "Access to the specified method is denied.", lineNumber, filename);
    }
}
