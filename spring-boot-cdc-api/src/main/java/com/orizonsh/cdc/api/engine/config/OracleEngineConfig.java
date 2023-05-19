package com.orizonsh.cdc.api.engine.config;

import java.util.Optional;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.orizonsh.cdc.api.exception.CDCApiException;

import io.debezium.connector.oracle.OracleConnector;

@Component
public class OracleEngineConfig extends AbstractEngineConfig {

	private static final long serialVersionUID = -8597000815977519575L;

	@Value("${db-config.server.name}")
	private String dbServerName;

	@Value("${db-config.hostname}")
	private String dbHostname;

	@Value("${db-config.port}")
	private String dbPort;

	@Value("${db-config.dbname}")
	private String dbname;

	@Value("${db-config.user}")
	private String dbUser;

	@Value("${db-config.password}")
	private String dbPassword;

	/** 監視対象となるテーブルのリスト */
	@Value("${db-config.table.include.list}")
	private String tableIncludeList;

	/**
	 * CDC エンジンの設定情報を取得する。
	 *
	 * @return CDC エンジンの設定情報
	 * @throws CDCApiException
	 */
	@Override
	public Properties getProps() throws CDCApiException {

		try {
			// 設定情報を初期化する
			final Properties props = initProps(OracleConnector.class);

			props.setProperty("snapshot.mode", "schema_only");

			props.setProperty("database.server.name", dbServerName);
			props.setProperty("database.hostname", dbHostname);
			props.setProperty("database.port", dbPort);
			props.setProperty("database.dbname", dbname);
			props.setProperty("database.user", dbUser);
			props.setProperty("database.password", dbPassword);

			props.setProperty("table.include.list", tableIncludeList);

			props.setProperty("decimal.handling.mode", "double");

			logEngineConfigValue(props);

			return props;
		} catch (Exception e) {
			throw new CDCApiException(e.getMessage(), e);
		}
	}

}
