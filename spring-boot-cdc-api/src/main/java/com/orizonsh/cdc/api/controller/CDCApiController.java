package com.orizonsh.cdc.api.controller;

import java.net.HttpURLConnection;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.orizonsh.cdc.api.bean.request.NotifyRequestData;
import com.orizonsh.cdc.api.bean.response.NotifyResponseData;
import com.orizonsh.cdc.api.service.CDCApiService;

/**
 * PostgreSQL CDC APIのControllerクラス
 *
 *
 * @author yuhao.li
 * @version 1.0.0
 *
 */
@RestController
public final class CDCApiController {

	/** Log API */
	private final Logger log = LogManager.getLogger(this.getClass());

	@Autowired
	private CDCApiService service;

	/**
	 * データ通知先設定
	 *
	 * @param requestDTO
	 * @return
	 * @throws Exception
	 */
	@PostMapping(produces = "application/json; charset=UTF-8", value = { "/notify" })
	@ResponseBody
	public ResponseEntity<Object> notify(@Validated @RequestBody NotifyRequestData requestData, BindingResult bindingResult) {

		// 処理開始日時
		long startDateTime = System.currentTimeMillis();

		// リクエストパラメータログに出力
		log.info("データ通知先設定　開始します。");

		try {

			// パラメータチェックエラー発生した場合
			if (bindingResult.hasErrors()) {
				// パラメータチェックエラーを出力
				outputValidateCheckErrorLog(bindingResult.getAllErrors());
				return createResponseEntity(HttpURLConnection.HTTP_BAD_REQUEST, "400");
			}

			service.setNotifyUrl(requestData.getNotifyURL());

			return createResponseEntity(HttpURLConnection.HTTP_OK, "0");

		} catch (Exception ex) {
			// 予期せぬエラー発生した場合
			log.error(ex.getMessage(), ex);
			return createResponseEntity(HttpURLConnection.HTTP_INTERNAL_ERROR, "500");

		} finally {
			log.info("データ通知先設定　終了しました。[処理時間：{}秒]",
					String.format("%.03f", (System.currentTimeMillis() - startDateTime) / 1000f));
		}
	}

	/**
	 * パラメータチェックエラーを出力
	 *
	 * @param list パラメータチェックエラー
	 */
	private void outputValidateCheckErrorLog(List<ObjectError> errList) {
		log.error("引数チェックエラーが発生しました。");
		for (ObjectError error : errList) {
			log.error(error.toString());
		}
	}

	/**
	 * レスポンスのHTTPヘッダー情報を取得
	 *
	 * @return HTTPヘッダー情報
	 */
	private HttpHeaders getResponseHeaders() {
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.set("Content-Type", "application/json; charset=UTF-8");
		return responseHeaders;
	}

	/**
	 * ResponseEntityを取得
	 *
	 * @param statusCode  応答コード
	 * @param responseDTO 応答情報
	 * @return ResponseEntity
	 */
	private ResponseEntity<Object> createResponseEntity(int statusCode, String resultCode) {
		NotifyResponseData responseData = new NotifyResponseData();
		responseData.setResultCode(resultCode);
		return ResponseEntity.status(statusCode).headers(getResponseHeaders()).body(responseData);
	}

}
