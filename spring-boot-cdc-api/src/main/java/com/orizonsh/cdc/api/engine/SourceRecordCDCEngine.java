package com.orizonsh.cdc.api.engine;

import java.util.List;

import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.source.SourceRecord;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.orizonsh.cdc.api.bean.common.DebeziumSourceData;
import com.orizonsh.cdc.api.engine.debezium.DebeziumEngineUtils;
import com.orizonsh.cdc.api.exception.CDCEngineException;

import io.debezium.data.Envelope;
import io.debezium.embedded.Connect;
import io.debezium.engine.DebeziumEngine;
import io.debezium.engine.RecordChangeEvent;
import io.debezium.engine.format.ChangeEventFormat;

@Component
public final class SourceRecordCDCEngine extends AbstractCDCEngine<RecordChangeEvent<SourceRecord>> {

	/**
	 * CDCエンジンを作成する。
	 * @throws CDCEngineException
	 */
	void createEngine() throws CDCEngineException {
		this.engine = DebeziumEngine.create(ChangeEventFormat.of(Connect.class))
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
	private void notify(
			List<RecordChangeEvent<SourceRecord>> recordChangeEvents,
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

					log.info("before data:[{}]", DebeziumEngineUtils.getBeforeDataWithJsonString(sourceRecordChangeValue));
					log.info("after data:[{}]", DebeziumEngineUtils.getAfterDataWithJsonString(sourceRecordChangeValue));

//					TableDataBase beforeData = null;
//					TableDataBase afterData = null;
//
//					switch (sourceData.getTableName()) {
//					case "orders": {
//						beforeData = DebeziumEngineUtils.getBeforeDataFromRecord(sourceRecordChangeValue, OrdersData.class);
//						afterData = DebeziumEngineUtils.getAfterDataFromRecord(sourceRecordChangeValue, OrdersData.class);
//						break;
//					}
//					case "category": {
//						beforeData = DebeziumEngineUtils.getBeforeDataFromRecord(sourceRecordChangeValue, CategoryData.class);
//						afterData = DebeziumEngineUtils.getAfterDataFromRecord(sourceRecordChangeValue, CategoryData.class);
//						break;
//					}
//					case "product": {
//						beforeData = DebeziumEngineUtils.getBeforeDataFromRecord(sourceRecordChangeValue, ProductData.class);
//						afterData = DebeziumEngineUtils.getAfterDataFromRecord(sourceRecordChangeValue, ProductData.class);
//						break;
//					}
//					default:
//						throw new IllegalArgumentException("Unexpected value: " + sourceData.getTableName());
//					}
//
//					if (beforeData != null) {
//						log.info(mapper.writeValueAsString(beforeData));
//					} else {
//						log.warn("変更前のデータがありません。");
//					}
//
//					if (afterData != null) {
//						log.info(mapper.writeValueAsString(afterData));
//					} else {
//						log.warn("変更後のデータがありません。");
//					}

//					sendData(sourceData.getTableName(), sourceData.getOperationType(), beforeData, afterData);
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

}
