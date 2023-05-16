package com.orizonsh.cdc.api.runner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.orizonsh.cdc.api.engine.CDCEngine;

@Component
public class DebeziumEngineRunner implements CommandLineRunner {

	/** Log API */
	private final Logger log = LogManager.getLogger(this.getClass());

	@Autowired
	private CDCEngine engine;

	public void run(String... args) throws Exception {

		log.info("開始 {} {}", "1", "2");

		engine.initEngine();

		log.info("終了 {} {}", "1", "2");

	}
}