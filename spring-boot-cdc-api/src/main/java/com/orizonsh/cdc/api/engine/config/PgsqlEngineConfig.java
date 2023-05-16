package com.orizonsh.cdc.api.engine.config;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.orizonsh.cdc.api.exception.CDCApiException;

@Component("PgsqlEngineConfig")
public final class PgsqlEngineConfig extends BaseEngineConfig {

	@Value("${cdc-engine-config.database.server.name}")
	private String dbServerName;

	@Value("${cdc-engine-config.database.hostname}")
	private String dbHostname;

	@Value("${cdc-engine-config.database.port}")
	private String dbPort;

	@Value("${cdc-engine-config.database.dbname}")
	private String dbname;

	@Value("${cdc-engine-config.database.user}")
	private String dbUser;

	@Value("${cdc-engine-config.database.password}")
	private String dbPassword;

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

			logEngineConfigValue(props);

			return props;
		} catch (Exception e) {
			throw new CDCApiException(e.getMessage(), e);
		}
	}
}
