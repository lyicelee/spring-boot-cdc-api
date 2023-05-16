package com.orizonsh.cdc.api.stub.controller;

import java.net.HttpURLConnection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CDCApiStubController {

	/** Log API */
	private final Logger log = LogManager.getLogger(this.getClass());

	/**
	 * データ通知
	 *
	 * @param requestDTO
	 * @return
	 * @throws Exception
	 */
	@PutMapping(produces = "application/json; charset=UTF-8", value = { "/notify1", "/notify2", "/notify3", "/notify4" })
	@ResponseBody
	public ResponseEntity<Object> notify(@RequestBody String jsonBody, BindingResult bindingResult) {

		log.info("JSON Body：[{}]", jsonBody);

		return ResponseEntity.status(HttpURLConnection.HTTP_OK).body("");
	}
}
