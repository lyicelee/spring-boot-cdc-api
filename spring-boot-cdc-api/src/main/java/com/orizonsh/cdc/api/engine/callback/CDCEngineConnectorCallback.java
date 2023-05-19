package com.orizonsh.cdc.api.engine.callback;

import java.util.Map;

import org.apache.kafka.connect.source.SourceConnector;
import org.apache.kafka.connect.source.SourceTask;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import io.debezium.engine.DebeziumEngine.ConnectorCallback;

@Component
public class CDCEngineConnectorCallback implements ConnectorCallback {

	/** Log API */
	private final Logger log = LogManager.getLogger(this.getClass());

    /**
     * Called after a connector has been successfully started by the engine; i.e. {@link SourceConnector#start(Map)} has
     * completed successfully
     */
	@Override
	public void connectorStarted() {
		log.info("connectorStarted() started.");
    }

    /**
     * Called after a connector has been successfully stopped by the engine; i.e. {@link SourceConnector#stop()} has
     * completed successfully
     */
	@Override
	public void connectorStopped() {
		log.info("connectorStopped() started.");
    }

    /**
     * Called after a connector task has been successfully started by the engine; i.e. {@link SourceTask#start(Map)} has
     * completed successfully
     */
	@Override
	public void taskStarted() {
		log.info("taskStarted() started.");
    }

    /**
     * Called after a connector task has been successfully stopped by the engine; i.e. {@link SourceTask#stop()} has
     * completed successfully
     */
	@Override
	public void taskStopped() {
		log.info("taskStopped() started.");
    }


}
