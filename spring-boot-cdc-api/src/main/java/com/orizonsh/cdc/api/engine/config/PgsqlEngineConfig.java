package com.orizonsh.cdc.api.engine.config;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.orizonsh.cdc.api.exception.CDCApiException;

@Component
public final class PgsqlEngineConfig extends BaseEngineConfig {

	private static final long serialVersionUID = 2121930554641157595L;

	@Value("${pgsql-db-config.server.name}")
	private String dbServerName;

	@Value("${pgsql-db-config.hostname}")
	private String dbHostname;

	@Value("${pgsql-db-config.port}")
	private String dbPort;

	@Value("${pgsql-db-config.dbname}")
	private String dbname;

	@Value("${pgsql-db-config.user}")
	private String dbUser;

	@Value("${pgsql-db-config.password}")
	private String dbPassword;

	/** 監視対象となるテーブルのリスト */
	@Value("${pgsql-db-config.table.include.list}")
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
			final Properties props = super.getProps();

			props.setProperty("database.server.name", dbServerName);
			props.setProperty("database.hostname", dbHostname);
			props.setProperty("database.port", dbPort);
			props.setProperty("database.dbname", dbname);
			props.setProperty("database.user", dbUser);
			props.setProperty("database.password", dbPassword);

			props.setProperty("decimal.handling.mode", "double");

			props.setProperty("table.include.list", tableIncludeList);

			logEngineConfigValue(props);

			return props;
		} catch (Exception e) {
			throw new CDCApiException(e.getMessage(), e);
		}
	}
}
