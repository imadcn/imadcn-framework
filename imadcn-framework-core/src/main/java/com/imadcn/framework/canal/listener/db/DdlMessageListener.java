package com.imadcn.framework.canal.listener.db;

import com.alibaba.otter.canal.protocol.CanalEntry;
import com.imadcn.framework.canal.listener.MessageListener;

public interface DdlMessageListener extends MessageListener  {
	
	void createTable(CanalEntry.Entry entry);
	void alterTable(CanalEntry.Entry entry);
	void dropTable(CanalEntry.Entry entry);
	void renameTable(CanalEntry.Entry entry);
	void createIndex(CanalEntry.Entry entry);
	void dropIndex(CanalEntry.Entry entry);
}
