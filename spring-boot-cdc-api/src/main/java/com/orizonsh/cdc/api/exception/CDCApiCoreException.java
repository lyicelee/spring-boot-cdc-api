package com.orizonsh.cdc.api.exception;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * CDC エンジンのExceptionクラス
 */
public final class CDCApiCoreException extends Exception {

	/** Log API */
	private final Logger log = LogManager.getLogger(this.getMessage());

	private static final long serialVersionUID = 826197122304263725L;

    public CDCApiCoreException(String message) {
        super(String.format("[CDC-API]%s", message));
    }

    public CDCApiCoreException(String message, Throwable cause) {
        super(String.format("[CDC-API]%s", message), cause);
    }

    public CDCApiCoreException(Throwable cause) {
        super(cause);
    	log.error("", cause);
    }
}
