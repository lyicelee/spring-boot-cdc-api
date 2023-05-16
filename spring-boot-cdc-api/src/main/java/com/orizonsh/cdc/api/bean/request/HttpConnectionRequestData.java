package com.orizonsh.cdc.api.bean.request;

import lombok.Data;

/**
 * API送信パラメータ保持用クラス
 */
@Data
public class HttpConnectionRequestData {

	/** 送信対象URL */
	private String url = null;

	/** 送信メソッド */
	private String methodType = null;

	/** コネクションタイムアウト時間 */
	private Integer connectTimeout = null;

	/** レスポンスタイムアウト時間 */
	private Integer readTimeout = null;

	/** コンテンツタイプ */
	private String contentType = null;

	/** 送信ボディ */
	private String reqBody = null;

	/** Bearerトークンによる認証実施フラグ */
	private boolean isAuthBearer = false;

	/** 認証情報 */
	private String authorization = null;

	/** デシリアライズをスキップするフラグ */
	private boolean skipDeserialize = false;
}
