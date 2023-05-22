package com.orizonsh.cdc.api.exception;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * CDC エンジンのExceptionクラス
 */
public final class CDCEngineException extends Exception {

	/** Log API */
	private final Logger log = LogManager.getLogger(this.getMessage());

	private static final long serialVersionUID = 826197122304263725L;

    public CDCEngineException(String message) {
        super(String.format("[CDC-ENGINE]%s", message));
    }

    public CDCEngineException(String message, Throwable cause) {
        super(String.format("[CDC-ENGINE]%s", message), cause);
    }

    public CDCEngineException(Throwable cause) {
        super(cause);
    	log.error("", cause);
    }
}
