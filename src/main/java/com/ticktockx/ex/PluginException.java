package com.ticktockx.ex;

public class PluginException extends RuntimeException {

    public PluginException(String message, Throwable cause) {
        super(message, cause);
    }

    public PluginException(String message) {
        super(message);
    }
}
