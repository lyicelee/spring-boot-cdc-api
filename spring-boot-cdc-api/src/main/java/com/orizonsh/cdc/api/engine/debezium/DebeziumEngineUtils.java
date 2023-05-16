package com.orizonsh.cdc.api.engine.debezium;


import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.kafka.connect.data.Field;
import org.apache.kafka.connect.data.Struct;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.databind.json.JsonMapper;

import io.debezium.data.Envelope;
import com.orizonsh.cdc.api.bean.common.DebeziumSourceData;

/**
 * Debeziumのツールクラス
 *
 * @author yuhao.li
 *
 */
public final class DebeziumEngineUtils {

	/** Log API */
	private static final Logger log = LogManager.getLogger(DebeziumEngineUtils.class);

	private static final String FIELD_NAME_SOURCE = "source";

	private static final String FIELD_NAME_BEFORE = "before";

	private static final String FIELD_NAME_AFTER = "after";

	private static final String FIELD_NAME_OPERATION = "op";

	public static DebeziumSourceData getSourceDataFromRecord(Struct record) throws Exception {

		Struct dateStruct = (Struct) record.get(FIELD_NAME_SOURCE);
		// 操作情報データがなければ
		if (null == dateStruct) return null;

		DebeziumSourceData sourceData = convertStructToObject(dateStruct, DebeziumSourceData.class);

		// 操作タイプを取得
		sourceData.setOperationType(Envelope.Operation.forCode((String) record.get(FIELD_NAME_OPERATION)));

		return sourceData;
	}

	public static <T> T getBeforeDataFromRecord(Struct record, Class<T> targetClass) throws Exception {

		Struct dateStruct = (Struct) record.get(FIELD_NAME_BEFORE);
		// 変更前のデータがなければ
		if (null == dateStruct) return null;

		return convertStructToObject(dateStruct, targetClass);
	}

	public static <T> T getAfterDataFromRecord(Struct record, Class<T> targetClass) throws Exception {

		Struct dateStruct = (Struct) record.get(FIELD_NAME_AFTER);
		// 変更後のデータがなければ
		if (null == dateStruct) return null;

		return convertStructToObject(dateStruct, targetClass);
	}

	private static <T> T convertStructToObject(Struct structData, Class<T> targetClass) throws Exception {

		Map<String, Object> dataMap = structData.schema().fields()
			.stream()
			.map(Field::name)
			.filter(fieldName -> structData.get(fieldName) != null)
			.map(fieldName -> Pair.of(fieldName.toString(), structData.get(fieldName)))
			.collect(Collectors.toMap(Pair::getKey, Pair::getValue));

		try {
			JsonMapper mapper = new JsonMapper();
//			log.info(">>>{}<<<", mapper.writeValueAsString(dataMap));

			T targetData = targetClass.cast(mapper.convertValue(dataMap, mapper.getTypeFactory().constructType(targetClass)));

			return targetData;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new Exception(e.getMessage());
		}
	}
}
