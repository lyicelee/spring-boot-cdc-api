package com.orizonsh.cdc.api.bean.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.debezium.data.Envelope;
import lombok.Data;

/**
 *
 * @author yuhao.li
 *
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DebeziumSourceDataBean {

	/** Debeziumバージョン */
	@JsonProperty(value = "version")
	private String versionName;

	/** コネクタ種類  */
	@JsonProperty(value = "connector")
	private String connectorType;

	/** Topic Prefix  */
	@JsonProperty(value = "name")
	private String topicPrefix;

	@JsonProperty(value = "ts_ms")
	private String tsms;

	/** スナップショットの種類 */
	@JsonProperty(value = "snapshot")
	private String snapshotType;

	/** DB名称 */
	@JsonProperty(value = "db")
	private String dbName;

	/** スキーマ名称 */
	@JsonProperty(value = "schema")
	private String schemaName;

	/** テーブル */
	@JsonProperty(value = "table")
	private String tableName;

	/** 操作タイプ */
	private Envelope.Operation operationType;
}
