package com.orizonsh.cdc.api.engine.config;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.orizonsh.cdc.api.exception.CDCApiCoreException;
import com.orizonsh.cdc.api.exception.CDCEngineException;

import io.debezium.connector.mysql.MySqlConnector;

@Component
public class MySqlEngineConfig extends AbstractEngineConfig {

	private static final long serialVersionUID = 4365194962801871653L;

	/** 監視対象となるテーブルのリスト */
	@Value("${db-config.table.include.list}")
	private String tableIncludeList;

	/**
	 * CDC エンジンの設定情報を取得する。
	 *
	 * @return CDC エンジンの設定情報
	 * @throws CDCApiCoreException
	 */
	@Override
	public Properties getProps() throws CDCEngineException {

		try {
			// 設定情報を初期化する
			final Properties props = initProps(MySqlConnector.class);

			props.setProperty("database.server.id", getEnvProperty("db-config.server.id"));

			props.setProperty("database.hostname", getEnvProperty("db-config.hostname"));
			props.setProperty("database.port", getEnvProperty("db-config.port"));
			props.setProperty("database.dbname", getEnvProperty("db-config.dbname"));
			props.setProperty("database.user", getEnvProperty("db-config.user"));
			props.setProperty("database.password", getEnvProperty("db-config.password"));

			props.setProperty("database.allowPublicKeyRetrieval", "true");

			props.setProperty("database.include.list", "cdc_api_db");

			props.setProperty("table.include.list", "cdc_api_db.orders");

			props.setProperty("decimal.handling.mode", "double");

			logEngineConfigValue(props);

			return props;
		} catch (Exception e) {
			throw new CDCEngineException(e.getMessage(), e);
		}
	}
}
