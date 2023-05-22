package com.orizonsh.cdc.api.service;

import com.orizonsh.cdc.api.exception.CDCApiCoreException;

public interface CDCApiService {

	/**
	 * データ通知先設定
	 *
	 * @param notifyURL データ通知先URL
	 * @throws CDCApiCoreException
	 */
	void setNotifyUrl(String notifyURL) throws CDCApiCoreException;

}
