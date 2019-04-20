package com.mightywidgets.error;

public class WidgetNotFoundException extends RuntimeException {

    public WidgetNotFoundException(Long id) {
        super("Could not find widget " + id);
    }

}
