package com.imadcn.framework.canal.listener.db;

import com.alibaba.otter.canal.protocol.CanalEntry;
import com.imadcn.framework.canal.listener.MessageListener;

public interface DclMessageListener extends MessageListener {
	void grant(CanalEntry.Entry entry);
	void createUser(CanalEntry.Entry entry);
}
