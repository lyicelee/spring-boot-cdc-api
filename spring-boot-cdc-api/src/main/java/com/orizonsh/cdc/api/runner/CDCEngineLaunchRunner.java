package com.orizonsh.cdc.api.runner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import com.orizonsh.cdc.api.engine.CDCEngine;

@Service
public class CDCEngineLaunchRunner implements CommandLineRunner {

	/** Log API */
	private final Logger log = LogManager.getLogger(this.getClass());

	@Autowired
	private CDCEngine engine;

	@Override
	public void run(String... args) throws Exception {

		log.info("CDC Engine 起動!!!");

		engine.start();

		log.info("CDC Engine 起動済!!!");
	}

}
