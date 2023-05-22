package com.orizonsh.cdc.api.utils;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.orizonsh.cdc.api.bean.request.HttpConnectionRequestData;
import com.orizonsh.cdc.api.bean.response.HttpConnectionResponseData;
import com.orizonsh.cdc.api.exception.CDCApiCoreException;

/**
 * API送信用ユーティリティクラス.
 */
@Component
public final class HttpConnectionUtils {

	/** Log API */
	private static final Logger log = LogManager.getLogger(HttpConnectionUtils.class);

	/**
	 * HttpUrlConnection を利用して指定のURLにアクセスする。
	 *
	 * @param param API送信パラメータ
	 *
	 * @return 応答情報
	 * @throws Exception
	 */
	public static HttpConnectionResponseData send(HttpConnectionRequestData param) throws Exception {


		try {
			RestTemplate restTemplate = new RestTemplate();

			// レスポンス文字コード設定
			List<HttpMessageConverter<?>> messageConverters = restTemplate.getMessageConverters();
			messageConverters.remove(1);
			HttpMessageConverter<?> converter = new StringHttpMessageConverter(StandardCharsets.UTF_8);
			messageConverters.add(1, converter);
			restTemplate.setMessageConverters(messageConverters);

			// リクエストエンティティ作成
			MediaType type = MediaType.parseMediaType(param.getContentType());

			RequestEntity<String> requestEntity = RequestEntity
					.method(HttpMethod.valueOf(param.getMethodType()), new URI(param.getUrl()))
					.contentType(type)
					.accept(type)
					.body(param.getReqBody());

			ResponseEntity<String> response = restTemplate.exchange(requestEntity, String.class);

			return createResponseBean(param, response.getStatusCode().value(), response.getBody());

		} catch (HttpClientErrorException e) {

			log.error("APIリクエスト実行が失敗しました。", e.getMessage());

			return createResponseBean(param, e.getStatusCode().value(), e.getResponseBodyAsString());

		} catch (Exception e) {

			log.error("APIリクエスト実行が失敗しました。", e);
			throw new CDCApiCoreException("APIリクエスト実行が失敗しました。");

		}
	}

	/**
	 * 応答情報格納用Beanを作成する
	 *
	 * @param param
	 * @param statusCd レスポンスステータスコード
	 * @param body レスポンスボディ
	 *
	 * @return 応答情報格納用Bean
	 * @throws JsonProcessingException
	 * @throws JsonMappingException
	 */
	private static HttpConnectionResponseData createResponseBean(HttpConnectionRequestData param, Integer statusCd, String body) throws Exception {

		log.info("レスポンスステータスコード：{}", statusCd);
		log.info("レスポンスボディ：{}", body);

		// 応答情報格納用Bean
		HttpConnectionResponseData responseInfo = new HttpConnectionResponseData();

		responseInfo.setRepStatus(statusCd);
		if (param.isSkipDeserialize()) {
			responseInfo.setRepBody(body);

		} else {
	        Map<String, Object> httpResMap = new HashMap<String, Object>();
			if (body != null) {
				httpResMap = new ObjectMapper().readValue(body, new TypeReference<HashMap<String, Object>>(){});
			}

			responseInfo.setRepBody(body);
			responseInfo.setResponseMap(httpResMap);
		}

		return responseInfo;
	}

}
