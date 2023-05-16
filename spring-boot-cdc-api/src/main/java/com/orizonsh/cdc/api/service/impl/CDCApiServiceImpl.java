package com.orizonsh.cdc.api.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.orizonsh.cdc.api.engine.CDCEngine;
import com.orizonsh.cdc.api.exception.CDCApiException;
import com.orizonsh.cdc.api.service.CDCApiService;

@Service
public class CDCApiServiceImpl implements CDCApiService {

	@Autowired
	private CDCEngine engine;

	@Override
	public void setNotifyUrl(String notifyURL) throws CDCApiException {

		engine.stop();
		engine.addNotifyURL(notifyURL);
		engine.start();
	}

}
