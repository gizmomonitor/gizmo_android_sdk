package com.gizmo.backend;

@SuppressWarnings("serial")
public class BackendAPIException extends Exception {

    public BackendAPIException(Exception e) {
        super(e);
    }

    public BackendAPIException(String msg) {
        super(msg);
    }
}