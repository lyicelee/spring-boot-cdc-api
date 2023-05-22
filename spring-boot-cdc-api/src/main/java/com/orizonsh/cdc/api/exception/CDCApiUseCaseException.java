package com.orizonsh.cdc.api.exception;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * CDC エンジンのExceptionクラス
 */
public final class CDCApiUseCaseException extends Exception {

	/** Log API */
	private final Logger log = LogManager.getLogger(this.getMessage());

	private static final long serialVersionUID = 826197122304263725L;

    public CDCApiUseCaseException(String message) {
        super(String.format("[CDC-API]%s", message));
    }

    public CDCApiUseCaseException(String message, Throwable cause) {
        super(String.format("[CDC-API]%s", message), cause);
    }

    public CDCApiUseCaseException(Throwable cause) {
        super(cause);
    	log.error("", cause);
    }
}
