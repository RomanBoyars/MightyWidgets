package com.mightywidgets.error;

public class EntityNotFoundException extends RuntimeException {

    public <ID> EntityNotFoundException(ID id) {
        super("Could not find entity " + id);
    }
}
