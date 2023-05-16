package com.orizonsh.cdc.api.service;

import com.orizonsh.cdc.api.exception.CDCApiException;

public interface CDCApiService {

	/**
	 * データ通知先設定
	 *
	 * @param notifyURL データ通知先URL
	 * @throws CDCApiException
	 */
	void setNotifyUrl(String notifyURL) throws CDCApiException;

}
