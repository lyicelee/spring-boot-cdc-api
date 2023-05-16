package com.orizonsh.cdc.api.engine;

import java.util.List;
import java.util.Properties;

import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.source.SourceRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.orizonsh.cdc.api.bean.common.DebeziumSourceDataBean;
import com.orizonsh.cdc.api.bean.table.CategoryDataBean;
import com.orizonsh.cdc.api.bean.table.OrdersDataBean;
import com.orizonsh.cdc.api.bean.table.ProductDataBean;
import com.orizonsh.cdc.api.bean.table.TableDataBeanBase;
import com.orizonsh.cdc.api.engine.config.PgsqlEngineConfig;
import com.orizonsh.cdc.api.engine.debezium.DebeziumEngineUtils;
import com.orizonsh.cdc.api.exception.CDCApiException;
import com.orizonsh.cdc.api.utils.ApplicationContextUtils;

import io.debezium.data.Envelope;
import io.debezium.embedded.Connect;
import io.debezium.engine.DebeziumEngine;
import io.debezium.engine.RecordChangeEvent;
import io.debezium.engine.format.ChangeEventFormat;

@Component
public class CDCEngine {

	/** Log API */
	private static final Logger log = LogManager.getLogger(CDCEngine.class);

	/** Debezium Engine */
	private DebeziumEngine<RecordChangeEvent<SourceRecord>> engine = null;

	@Autowired
	private ApplicationContextUtils contextUtils;

	@Value("${cdc-engine-config.db.type}")
	private String dbType;

	public void initEngine() throws CDCApiException  {

		if (null == engine) {
			log.info("CDC Engineを初期化します。");
			this.engine = DebeziumEngine
					.create(ChangeEventFormat.of(Connect.class))
					.using(getEngineConfig())
					.notifying(this::payload)
					.build();
		} else {
			log.warn("CDC Engineが既に初期化しました。");
		}
	}

	/**
	 *
	 * @return
	 * @throws CDCApiException
	 */
	private Properties getEngineConfig() throws CDCApiException {

		log.debug("[cdc-engine]DB type>>>>>{}", dbType);
		switch(dbType) {
			case CDCEngineConstant.DB_TYPE_PGSQL:
				return contextUtils.getBean(PgsqlEngineConfig.class).getProps();

			default:
				throw new CDCApiException("対象外DB種類：" + dbType);
		}
	}

	/**
	 *
	 * @param recordChangeEvents
	 * @param recordCommitter
	 */
	private void payload(List<RecordChangeEvent<SourceRecord>> recordChangeEvents,
			DebeziumEngine.RecordCommitter<RecordChangeEvent<SourceRecord>> recordCommitter) {

		log.info("handlePayload START");

		try {
			JsonMapper mapper = new JsonMapper();

			for (RecordChangeEvent<SourceRecord> r : recordChangeEvents) {

				SourceRecord sourceRecord = r.record();

				Struct sourceRecordChangeValue = (Struct) sourceRecord.value();

				if (sourceRecordChangeValue == null) {
					continue;
				}

				DebeziumSourceDataBean sourceData = DebeziumEngineUtils.getSourceDataFromRecord(sourceRecordChangeValue);
				if (null == sourceData) {
					continue;
				}
				log.info(mapper.writeValueAsString(sourceData));

				if (Envelope.Operation.CREATE == sourceData.getOperationType() ||
						Envelope.Operation.UPDATE == sourceData.getOperationType() ||
						Envelope.Operation.DELETE == sourceData.getOperationType()) {

					TableDataBeanBase beforeData = null;
					TableDataBeanBase afterData = null;

					switch (sourceData.getTableName()) {
					case "orders": {
						beforeData = DebeziumEngineUtils.getBeforeDataFromRecord(sourceRecordChangeValue, OrdersDataBean.class);
						afterData = DebeziumEngineUtils.getAfterDataFromRecord(sourceRecordChangeValue, OrdersDataBean.class);
						break;
					}
					case "category": {
						beforeData = DebeziumEngineUtils.getBeforeDataFromRecord(sourceRecordChangeValue, CategoryDataBean.class);
						afterData = DebeziumEngineUtils.getAfterDataFromRecord(sourceRecordChangeValue, CategoryDataBean.class);
						break;
					}
					case "product": {
						beforeData = DebeziumEngineUtils.getBeforeDataFromRecord(sourceRecordChangeValue, ProductDataBean.class);
						afterData = DebeziumEngineUtils.getAfterDataFromRecord(sourceRecordChangeValue, ProductDataBean.class);
						break;
					}
					default:
						throw new IllegalArgumentException("Unexpected value: " + sourceData.getTableName());
					}

					if (beforeData != null) {
						log.info(mapper.writeValueAsString(beforeData));
					} else {
						log.warn("変更前のデータがありません。");
					}

					if (afterData != null) {
						log.info(mapper.writeValueAsString(afterData));
					} else {
						log.warn("変更後のデータがありません。");
					}
				}

				recordCommitter.markProcessed(r);

				log.info("----------------------------------------------------------------------------------");
			}

			recordCommitter.markBatchFinished();

		} catch (Exception e) {
			e.printStackTrace();
		}

		log.info("handlePayload END");
	}
}
