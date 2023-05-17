package com.orizonsh.cdc.api.engine;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.source.SourceRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.orizonsh.cdc.api.bean.common.DebeziumSourceData;
import com.orizonsh.cdc.api.bean.request.HttpConnectionRequestData;
import com.orizonsh.cdc.api.bean.table.CategoryData;
import com.orizonsh.cdc.api.bean.table.OrdersData;
import com.orizonsh.cdc.api.bean.table.ProductData;
import com.orizonsh.cdc.api.bean.table.TableDataBase;
import com.orizonsh.cdc.api.engine.config.PgsqlEngineConfig;
import com.orizonsh.cdc.api.engine.debezium.DebeziumEngineUtils;
import com.orizonsh.cdc.api.exception.CDCApiException;
import com.orizonsh.cdc.api.utils.ApplicationContextUtils;
import com.orizonsh.cdc.api.utils.HttpConnectionUtils;

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

	/** Executor Service */
	private ExecutorService executor = null;

	@Autowired
	private ApplicationContextUtils contextUtils;

	@Value("${cdc-engine-config.db.type}")
	private String dbType;

	/** データ通知先URL */
	private HashSet<String> notifyURLSet = new HashSet<String>();

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

	public void start() throws CDCApiException {

		log.info("CDC Engineを起動します。");

		if (null == engine) {
			this.initEngine();
		}

		executor = Executors.newSingleThreadExecutor();
		executor.execute(engine);

		log.info("CDC Engineが起動できました。");
	}

	/**
	 * データ通知先URLを追加
	 *
	 * @param notifyURL
	 */
	public void addNotifyURL(String notifyURL) {
		notifyURLSet.add(notifyURL);
	}

	public void stop() throws CDCApiException {
		try {
			if (null != engine) {
				log.info("CDC Engineを停止します。");
				engine.close();
				executor.shutdown();
				engine = null;
				log.info("CDC Engineが停止できました。");
			}
		} catch (Exception e) {
			throw new CDCApiException(e.getMessage(), e);
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

				DebeziumSourceData sourceData = DebeziumEngineUtils.getSourceDataFromRecord(sourceRecordChangeValue);
				if (null == sourceData) {
					continue;
				}
				log.info(mapper.writeValueAsString(sourceData));

				if (Envelope.Operation.CREATE == sourceData.getOperationType() ||
						Envelope.Operation.UPDATE == sourceData.getOperationType() ||
						Envelope.Operation.DELETE == sourceData.getOperationType()) {

					TableDataBase beforeData = null;
					TableDataBase afterData = null;

					switch (sourceData.getTableName()) {
					case "orders": {
						beforeData = DebeziumEngineUtils.getBeforeDataFromRecord(sourceRecordChangeValue, OrdersData.class);
						afterData = DebeziumEngineUtils.getAfterDataFromRecord(sourceRecordChangeValue, OrdersData.class);
						break;
					}
					case "category": {
						beforeData = DebeziumEngineUtils.getBeforeDataFromRecord(sourceRecordChangeValue, CategoryData.class);
						afterData = DebeziumEngineUtils.getAfterDataFromRecord(sourceRecordChangeValue, CategoryData.class);
						break;
					}
					case "product": {
						beforeData = DebeziumEngineUtils.getBeforeDataFromRecord(sourceRecordChangeValue, ProductData.class);
						afterData = DebeziumEngineUtils.getAfterDataFromRecord(sourceRecordChangeValue, ProductData.class);
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

					sendData(sourceData.getTableName(), sourceData.getOperationType(), beforeData, afterData);
				}

//				recordCommitter.markProcessed(r);

				log.info("----------------------------------------------------------------------------------");
			}

//			recordCommitter.markBatchFinished();

		} catch (Exception e) {
			e.printStackTrace();
		}

		log.info("handlePayload END");
	}

	private void sendData(String tableName, Envelope.Operation operation, TableDataBase beforeData, TableDataBase afterData) throws Exception {


		HttpConnectionRequestData requestData = new HttpConnectionRequestData();

		// リクエスト情報を設定
		requestData.setMethodType("PUT");
		requestData.setContentType("application/json; charset=UTF-8");
		requestData.setConnectTimeout(5000);
		requestData.setReadTimeout(5000);

		HashMap<String, Object> requestBodyMap = new HashMap<String, Object>();
		requestBodyMap.put("tableName",  tableName);
		requestBodyMap.put("operation",  operation);
		requestBodyMap.put("beforeData", beforeData);
		requestBodyMap.put("afterData",  afterData);

		// リクエストボディ
		requestData.setReqBody(new ObjectMapper().writeValueAsString(requestBodyMap));

		for (String notifyURL : notifyURLSet) {
			requestData.setUrl(notifyURL);
//			HttpConnectionUtils.send(requestData);
		}
	}
}
