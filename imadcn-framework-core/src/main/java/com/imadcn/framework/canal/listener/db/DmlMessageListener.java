package com.imadcn.framework.canal.listener.db;

import com.alibaba.otter.canal.protocol.CanalEntry;
import com.imadcn.framework.canal.listener.MessageListener;

public interface DmlMessageListener extends MessageListener  {
	public void insertTable(CanalEntry.RowData rowData);
	public void updateTable(CanalEntry.RowData rowData);
	public void deleteTable(CanalEntry.RowData rowData);
}
