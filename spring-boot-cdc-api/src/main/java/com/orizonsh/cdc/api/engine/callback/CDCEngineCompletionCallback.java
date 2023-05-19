package com.orizonsh.cdc.api.engine.callback;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import io.debezium.engine.DebeziumEngine.CompletionCallback;

@Component
public class CDCEngineCompletionCallback implements CompletionCallback {

	/** Log API */
	private final Logger log = LogManager.getLogger(this.getClass());

	@Override
	public void handle(boolean success, String message, Throwable error) {
		// TODO 自動生成されたメソッド・スタブ

	}

}
