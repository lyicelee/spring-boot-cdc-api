package com.orizonsh.cdc.api.engine;

import org.springframework.stereotype.Component;

import com.orizonsh.cdc.api.exception.CDCApiCoreException;
import com.orizonsh.cdc.api.exception.CDCEngineException;

import io.debezium.engine.ChangeEvent;
import io.debezium.engine.DebeziumEngine;
import io.debezium.engine.format.Json;

@Component
public final class JSONCDCEngine extends AbstractCDCEngine<ChangeEvent<String, String>> {

	/**
	 * CDCエンジンを作成する。
	 * @throws CDCApiCoreException
	 * @throws CDCEngineException
	 */
	void createEngine() throws CDCEngineException {
		this.engine = DebeziumEngine.create(Json.class)
				.using(getEngineConfig())
				.using(getConnectorCallback())
				.using(getCompletCallback())
			    .notifying(this::notify)
			    .build();
	}

	/**
	 * データ通知処理
	 *
	 * @param recordChangeEvents
	 * @param recordCommitter
	 */
	private void notify(ChangeEvent<String, String> event) {

		log.info("handlePayload START");

		try {
			log.info("event:[{}]", event);
			log.info("destination:[{}]", event.destination());
			log.info("key:[{}]", event.key());
			log.info("value:[{}]", event.value());

		} catch (Exception e) {
			e.printStackTrace();
		}

		log.info("handlePayload END");
	}
}
