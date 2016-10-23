package com.fenghun.openglesdroid.jni.bean20;

public interface ErrorHandler {
	public enum ErrorType {
		BUFFER_CREATION_ERROR
	}
	
	public void handleError(ErrorType errorType, String cause);
}