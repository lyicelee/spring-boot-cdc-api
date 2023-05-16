package com.orizonsh.cdc.api.handler;

import java.util.List;

import org.apache.kafka.common.protocol.types.Struct;
import org.apache.kafka.connect.source.SourceRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import io.debezium.engine.DebeziumEngine;
import io.debezium.engine.RecordChangeEvent;

@Service
public class DataChangeEventHandler {

	/** Log API */
	private static final Logger log = LogManager.getLogger(DataChangeEventHandler.class);

	public static void handlePayload(
				List<RecordChangeEvent<SourceRecord>> recordChangeEvents,
				DebeziumEngine.RecordCommitter<RecordChangeEvent<SourceRecord>> recordCommitter
			) {

		log.info("handlePayload START");

		for (RecordChangeEvent<SourceRecord> r : recordChangeEvents) {

			SourceRecord sourceRecord = r.record();

			Struct sourceRecordChangeValue = (Struct) sourceRecord.value();

			if (sourceRecordChangeValue == null) {
				continue;
			}

//			// 获取变更表数据
//			Map<String, Object> changeMap = getChangeTableInfo(sourceRecordChangeValue);
//			if (CollectionUtils.isEmpty(changeMap)) {
//				continue;
//			}
//
//			ChangeListenerModel changeListenerModel = getChangeDataInfo(sourceRecordChangeValue, changeMap);
//			if (changeListenerModel == null) {
//				continue;
//			}
//
//
//			String jsonString = JSON.toJSONString(changeListenerModel);
//			log.info("发送变更数据：{}", jsonString);
		}

		try {
			recordCommitter.markBatchFinished();

		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		log.info("handlePayload END");
	}

}
