package com.innovez.core.search;

@SuppressWarnings("serial")
public class SearchOperationException extends RuntimeException {
	public SearchOperationException() {
		super();
	}

	public SearchOperationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public SearchOperationException(String message, Throwable cause) {
		super(message, cause);
	}

	public SearchOperationException(String message) {
		super(message);
	}

	public SearchOperationException(Throwable cause) {
		super(cause);
	}
}
