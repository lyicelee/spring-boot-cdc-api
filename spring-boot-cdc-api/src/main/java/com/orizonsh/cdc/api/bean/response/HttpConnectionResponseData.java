package com.orizonsh.cdc.api.bean.response;

import java.util.Map;

import lombok.Data;

/**
 * API送信結果保持用クラス
 */
@Data
public class HttpConnectionResponseData {

	/** 応答Status */
	private Integer repStatus = 0;

	/** 応答Body */
	private String repBody = null;

    /** 受信したJSONオブジェクトのデシリアライズ結果. */
    private Map<String, Object> responseMap = null;
}
