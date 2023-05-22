package com.orizonsh.cdc.api.engine.config;

import java.io.Serializable;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.orizonsh.cdc.api.exception.CDCApiCoreException;
import com.orizonsh.cdc.api.exception.CDCEngineException;

import io.debezium.config.Configuration;
import io.debezium.connector.common.RelationalBaseSourceConnector;

/**
 * CDC エンジン Configのベースクラス
 */
public abstract class AbstractEngineConfig implements Serializable {

	private static final long serialVersionUID = 8617742754241543904L;

	/** Log API */
	final Logger log = LogManager.getLogger(this.getClass());

	/** 環境設定 */
	@Autowired
	private Environment environment;

	/**
	 * CDC エンジンの設定情報を取得する。
	 *
	 * @return CDC エンジンの設定情報
	 * @throws CDCApiCoreException
	 */
	public Properties getProps() throws CDCEngineException {
		//
		throw new NotImplementedException("getProps()をオーバーライドしてください。");
	}

	/**
	 * CDC エンジンの設定情報を初期化する。
	 *
	 * @param <T>
	 * @param connectorClass connectorクラス
	 * @return CDC エンジンの設定情報
	 * @throws CDCApiCoreException
	 */
	final <T extends RelationalBaseSourceConnector> Properties initProps(Class<T> connectorClass) throws CDCEngineException {

		final Properties props = Configuration.create().build().asProperties();

		props.setProperty("connector.class", connectorClass.getName());

		props.setProperty("name", getEnvProperty("cdc-engine-config.engine.name"));
		props.setProperty("slot.name", getEnvProperty("cdc-engine-config.slot.name"));

		props.setProperty("converter", "org.apache.kafka.connect.json.JsonConverter");
		props.setProperty("converter.schemas.enable", "false");

		props.setProperty("offset.storage", getEnvProperty("cdc-engine-config.offset.storage"));
		props.setProperty("offset.storage.file.filename", getEnvProperty("cdc-engine-config..file.filename"));
		props.setProperty("offset.flush.interval.ms", getEnvProperty("cdc-engine-config.flush.offset.interval.ms"));
		props.setProperty("offset.flush.timeout.ms", getEnvProperty("cdc-engine-config.offset.flush.timeout.ms"));

		props.setProperty("schema.history.internal", getEnvProperty("cdc-engine-config.schema.history.internal"));
		props.setProperty("schema.history.internal.file.filename", getEnvProperty("cdc-engine-config.schema.history.internal.file.filename"));

		props.setProperty("tasks.max", getEnvProperty("cdc-engine-config.tasks.max"));

		props.setProperty("topic.prefix", getEnvProperty("cdc-engine-config.topic.prefix"));

		props.setProperty("snapshot.mode", getEnvProperty("cdc-engine-config.snapshot.mode"));

		return props;
	}

	final String getEnvProperty(String key) throws CDCEngineException {
		try {
			return getEnvProperty(key);
		} catch (Exception e) {
			throw new CDCEngineException(String.format("設定情報から[%s]データが取得できません。", key));
		}
	}

	/**
	 * Debeziumの設定情報をログに出力する。
	 */
	final void logEngineConfigValue(Properties props) {
		try {
			Map<String, Object> configMap = props.stringPropertyNames().stream().map(key -> {
				return Pair.of(key, props.getProperty(key));
			}).collect(Collectors.toMap(Pair::getKey, Pair::getValue));

			log.debug("cdc engine config:[{}]", new JsonMapper().writeValueAsString(configMap));

		} catch (Exception e) {
			log.debug(e.getMessage());
		}
	}
}
