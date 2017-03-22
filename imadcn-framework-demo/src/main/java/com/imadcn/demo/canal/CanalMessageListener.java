package com.imadcn.demo.canal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.otter.canal.protocol.CanalEntry.RowData;
import com.imadcn.framework.canal.listener.db.DmlMessageListener;

public class CanalMessageListener implements DmlMessageListener {
	
	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public void insertTable(RowData rowData) {
		logger.info("insert detected");
	}

	@Override
	public void updateTable(RowData rowData) {
		logger.info("update detected");
	}

	@Override
	public void deleteTable(RowData rowData) {
		logger.info("delete detected");
	}

}
