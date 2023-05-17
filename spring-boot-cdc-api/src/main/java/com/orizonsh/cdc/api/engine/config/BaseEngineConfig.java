package com.orizonsh.cdc.api.engine.config;

import java.io.Serializable;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.orizonsh.cdc.api.exception.CDCApiException;

import io.debezium.config.Configuration;
import io.debezium.connector.postgresql.PostgresConnector;

/**
 * CDC エンジン Configのベースクラス
 */
@Component
public abstract class BaseEngineConfig implements Serializable {

	private static final long serialVersionUID = 8617742754241543904L;

	/** Log API */
	final Logger log = LogManager.getLogger(this.getClass());

	@Value("${cdc-engine-config.engine.name}")
	private String engineName;

	@Value("${cdc-engine-config.slot.name}")
	private String slotName;

	@Value("${cdc-engine-config.topic.prefix}")
	private String topicPrefix;

	@Value("${cdc-engine-config.offset.storage}")
	private String offsetStorage;

//	@Value("${cdc-engine-config.offset.storage.file.filename}")
//	private String offsetStorageFileName;

	@Value("${cdc-engine-config.offset.flush.interval.ms}")
	private Long offsetFlushInterval;

	@Value("${cdc-engine-config.offset.flush.timeout.ms}")
	private Long offsetFlushTimeout;

//	@Value("${cdc-engine-config.schema.history.internal}")
//	private String schemaHistoryInternal;
//
//	@Value("${cdc-engine-config.schema.history.internal.file.filename}")
//	private String schemaHistoryInternalFileName;

	/**
	 * CDC エンジンの設定情報を取得する。
	 *
	 * @return CDC エンジンの設定情報
	 * @throws CDCApiException
	 */
	public Properties getProps() throws CDCApiException {

		final Properties props = Configuration.create().build().asProperties();

		props.setProperty("name", engineName);
		props.setProperty("slot.name", slotName);

		props.setProperty("connector.class", PostgresConnector.class.getName());

		props.setProperty("converter", "org.apache.kafka.connect.json.JsonConverter");
		props.setProperty("converter.schemas.enable", "false");

		props.setProperty("offset.storage", offsetStorage);
//		props.setProperty("offset.storage.file.filename", offsetStorageFileName);
		props.setProperty("offset.flush.interval.ms", offsetFlushInterval.toString());
		props.setProperty("offset.flush.timeout.ms", offsetFlushTimeout.toString());

		props.setProperty("include.schema.changes", "false");

		props.setProperty("plugin.name", "decoderbufs");

		props.setProperty("tasks.max", "1");

		props.setProperty("snapshot.mode", "never");

		props.setProperty("topic.prefix", topicPrefix);

//		props.setProperty("schema.history.internal", schemaHistoryInternal);
//		props.setProperty("schema.history.internal.file.filename", schemaHistoryInternalFileName);

		return props;
	}

	/**
	 * Debeziumの設定情報をログに出力する。
	 */
	final void logEngineConfigValue(Properties props) {
		try {
			Map<String, Object> configMap = props.stringPropertyNames()
					.stream()
					.map(key -> {
						return Pair.of(key, props.getProperty(key));
					})
					.collect(Collectors.toMap(Pair::getKey, Pair::getValue));

			log.debug("cdc engine config:[{}]", new JsonMapper().writeValueAsString(configMap));

		} catch (Exception e) {
			log.debug(e.getMessage());
		}
	}
}
