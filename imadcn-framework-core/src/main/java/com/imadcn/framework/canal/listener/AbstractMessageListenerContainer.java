package com.imadcn.framework.canal.listener;

import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.SystemUtils;
import org.slf4j.MDC;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.SmartLifecycle;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.CollectionUtils;

import com.alibaba.otter.canal.protocol.Message;
import com.alibaba.otter.canal.protocol.CanalEntry.Column;
import com.alibaba.otter.canal.protocol.CanalEntry.Entry;
import com.alibaba.otter.canal.protocol.CanalEntry.EntryType;
import com.alibaba.otter.canal.protocol.CanalEntry.EventType;
import com.alibaba.otter.canal.protocol.CanalEntry.RowChange;
import com.alibaba.otter.canal.protocol.CanalEntry.RowData;
import com.alibaba.otter.canal.protocol.CanalEntry.TransactionBegin;
import com.alibaba.otter.canal.protocol.CanalEntry.TransactionEnd;
import com.google.protobuf.InvalidProtocolBufferException;
import com.imadcn.framework.canal.connection.CanalAccessor;
import com.imadcn.framework.canal.connection.ConnectionFactory;
import com.imadcn.framework.canal.connection.SimpleConnectionFactory;
import com.imadcn.framework.canal.connection.SocketClusterConnectionFactory;
import com.imadcn.framework.canal.connection.ZkClusterConnetionFactory;
import com.imadcn.framework.canal.listener.db.DclMessageListener;
import com.imadcn.framework.canal.listener.db.DdlMessageListener;
import com.imadcn.framework.canal.listener.db.DmlMessageListener;
import com.rabbitmq.client.Channel;

public abstract class AbstractMessageListenerContainer extends CanalAccessor
		implements MessageListenerContainer, ApplicationContextAware, BeanNameAware, DisposableBean, SmartLifecycle {
	
    protected static final String SEP = SystemUtils.LINE_SEPARATOR;

	private volatile Object messageListener;

	private volatile String beanName;

	private volatile boolean autoStartup = true;

	private int phase = Integer.MAX_VALUE;

	private volatile boolean active = false;

	private volatile boolean running = false;

	private final Object lifecycleMonitor = new Object();

	private volatile boolean initialized;

	private volatile ApplicationContext applicationContext;

	/**
	 * Set the message listener implementation to register. This can be either a
	 * Spring {@link MessageListener} object or a Spring
	 * {@link DclMessageListener} object or a Spring {@link DdlMessageListener}
	 * object or a Spring {@link DmlMessageListener} object.
	 * 
	 * @param messageListener
	 */
	public void setMessageListener(Object messageListener) {
		checkMessageListener(messageListener);
		this.messageListener = messageListener;
	}

	/**
	 * Check the given message listener, throwing an exception if it does not
	 * correspond to a supported listener type.
	 */
	protected void checkMessageListener(Object messageListener) {
		if (!(messageListener instanceof MessageListener || messageListener instanceof DclMessageListener
				|| messageListener instanceof DdlMessageListener || messageListener instanceof DmlMessageListener)) {
			throw new IllegalArgumentException("Message listener needs to be of type " + "["
					+ MessageListener.class.getName() + "] or [" + DclMessageListener.class.getName() + "]" + " or ["
					+ DdlMessageListener.class.getName() + "] or [" + DmlMessageListener.class.getName() + "]");
		}
	}

	/**
	 * @return The message listener object to register.
	 */
	public Object getMessageListener() {
		return this.messageListener;
	}

	/**
	 * Set whether to automatically start the container after initialization.
	 * <p>
	 * Default is "true"; set this to "false" to allow for manual startup
	 * through the {@link #start()} method.
	 *
	 * @param autoStartup
	 *            true for auto startup.
	 */
	public void setAutoStartup(boolean autoStartup) {
		this.autoStartup = autoStartup;
	}

	@Override
	public boolean isAutoStartup() {
		return this.autoStartup;
	}

	/**
	 * Specify the phase in which this container should be started and stopped.
	 * The startup order proceeds from lowest to highest, and the shutdown order
	 * is the reverse of that. By default this value is Integer.MAX_VALUE
	 * meaning that this container starts as late as possible and stops as soon
	 * as possible.
	 *
	 * @param phase
	 *            The phase.
	 */
	public void setPhase(int phase) {
		this.phase = phase;
	}

	/**
	 * @return The phase in which this container will be started and stopped.
	 */
	@Override
	public int getPhase() {
		return this.phase;
	}

	@Override
	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}

	/**
	 * @return The bean name that this listener container has been assigned in
	 *         its containing bean factory, if any.
	 */
	protected final String getBeanName() {
		return this.beanName;
	}

	protected final ApplicationContext getApplicationContext() {
		return this.applicationContext;
	}

	@Override
	public final void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	/**
	 * The ConnectionFactory that uses for obtaining canal Connections.
	 * 
	 * @return
	 */
	public ConnectionFactory getConnectionFactory() {
		ConnectionFactory connectionFactory = super.getConnectionFactory();
		if (connectionFactory instanceof SimpleConnectionFactory) {
			ConnectionFactory targetConnectionFactory = ((SimpleConnectionFactory) connectionFactory);
			if (targetConnectionFactory != null) {
				return targetConnectionFactory;
			}
		} else if (connectionFactory instanceof SimpleConnectionFactory) {
			ConnectionFactory targetConnectionFactory = ((SocketClusterConnectionFactory) connectionFactory);
			if (targetConnectionFactory != null) {
				return targetConnectionFactory;
			}
		} else if (connectionFactory instanceof SimpleConnectionFactory) {
			ConnectionFactory targetConnectionFactory = ((ZkClusterConnetionFactory) connectionFactory);
			if (targetConnectionFactory != null) {
				return targetConnectionFactory;
			}
		}
		return connectionFactory;
	}

	/**
	 * Delegates to {@link #validateConfiguration()} and {@link #initialize()}.
	 */
	@Override
	public final void afterPropertiesSet() {
		super.afterPropertiesSet();
		validateConfiguration();
		initialize();
	}

	@Override
	public void setupMessageListener(Object messageListener) {
		setMessageListener(messageListener);
	}

	/**
	 * Validate the configuration of this container.
	 * <p>
	 * The default implementation is empty. To be overridden in subclasses.
	 */
	protected void validateConfiguration() {
	}

	/**
	 * Calls {@link #shutdown()} when the BeanFactory destroys the container
	 * instance.
	 * 
	 * @see #shutdown()
	 */
	@Override
	public void destroy() {
		shutdown();
	}

	// -------------------------------------------------------------------------
	// Lifecycle methods for starting and stopping the container
	// -------------------------------------------------------------------------

	/**
	 * Initialize this container.
	 * <p>
	 * Creates a Rabbit Connection and calls {@link #doInitialize()}.
	 */
	public void initialize() {
		try {
			synchronized (this.lifecycleMonitor) {
				this.lifecycleMonitor.notifyAll();
			}
			doInitialize();
		} catch (Exception ex) {
			throw new RuntimeException(ex); // TODO
		}
	}

	/**
	 * Stop the shared Connection, call {@link #doShutdown()}, and close this
	 * container.
	 */
	public void shutdown() {
		logger.debug("Shutting down Rabbit listener container");
		synchronized (this.lifecycleMonitor) {
			this.active = false;
			this.lifecycleMonitor.notifyAll();
		}

		// Shut down the invokers.
		try {
			doShutdown();
		} catch (Exception ex) {
			throw new RuntimeException(ex); // TODO
		} finally {
			synchronized (this.lifecycleMonitor) {
				this.running = false;
				this.lifecycleMonitor.notifyAll();
			}
		}
	}

	/**
	 * Register any invokers within this container.
	 * <p>
	 * Subclasses need to implement this method for their specific invoker
	 * management process.
	 *
	 * @throws Exception
	 *             Any Exception.
	 */
	protected abstract void doInitialize() throws Exception;

	/**
	 * Close the registered invokers.
	 * <p>
	 * Subclasses need to implement this method for their specific invoker
	 * management process.
	 * <p>
	 * A shared Rabbit Connection, if any, will automatically be closed
	 * <i>afterwards</i>.
	 * 
	 * @see #shutdown()
	 */
	protected abstract void doShutdown();

	/**
	 * @return Whether this container is currently active, that is, whether it
	 *         has been set up but not shut down yet.
	 */
	public final boolean isActive() {
		synchronized (this.lifecycleMonitor) {
			return this.active;
		}
	}

	/**
	 * Start this container.
	 * 
	 * @see #doStart
	 */
	@Override
	public void start() {
		if (!this.initialized) {
			synchronized (this.lifecycleMonitor) {
				if (!this.initialized) {
					afterPropertiesSet();
					this.initialized = true;
				}
			}
		}
		try {
			if (logger.isDebugEnabled()) {
				logger.debug("Starting Rabbit listener container.");
			}
			doStart();
		} catch (Exception ex) {
			throw new RuntimeException(ex); // TODO
		}
	}

	/**
	 * Start this container, and notify all invoker tasks.
	 * 
	 * @throws Exception
	 *             if thrown by Rabbit API methods
	 */
	protected void doStart() throws Exception {
		// Reschedule paused tasks, if any.
		synchronized (this.lifecycleMonitor) {
			this.active = true;
			this.running = true;
			this.lifecycleMonitor.notifyAll();
		}
	}

	/**
	 * Stop this container.
	 * 
	 * @see #doStop
	 * @see #doStop
	 */
	@Override
	public void stop() {
		try {
			doStop();
		} catch (Exception ex) {
			throw new RuntimeException(ex); // TODO
		} finally {
			synchronized (this.lifecycleMonitor) {
				this.running = false;
				this.lifecycleMonitor.notifyAll();
			}
		}
	}

	@Override
	public void stop(Runnable callback) {
		this.stop();
		callback.run();
	}

	/**
	 * This method is invoked when the container is stopping. The default
	 * implementation does nothing, but subclasses may override.
	 */
	protected void doStop() {
	}

	/**
	 * Determine whether this container is currently running, that is, whether
	 * it has been started and not stopped yet.
	 * 
	 * @see #start()
	 * @see #stop()
	 */
	@Override
	public final boolean isRunning() {
		synchronized (this.lifecycleMonitor) {
			return (this.running);
		}
	}
	
	protected void process() {
        int batchSize = 5 * 1024;
        while (running) {
            try {
                MDC.put("destination", destination);
                connector.connect();
                connector.subscribe();
                while (running) {
                    Message message = connector.getWithoutAck(batchSize); // 获取指定数量的数据
                    long batchId = message.getId();
                    int size = message.getEntries().size();
                    if (batchId == -1 || size == 0) {
                        // try {
                        // Thread.sleep(1000);
                        // } catch (InterruptedException e) {
                        // }
                    } else {
                        printSummary(message, batchId, size);
                        printEntry(message.getEntries());
                    }

                    connector.ack(batchId); // 提交确认
                    // connector.rollback(batchId); // 处理失败, 回滚数据
                }
            } catch (Exception e) {
                logger.error("process error!", e);
            } finally {
                connector.disconnect();
                MDC.remove("destination");
            }
        }
    }

    private void printSummary(Message message, long batchId, int size) {
        long memsize = 0;
        for (Entry entry : message.getEntries()) {
            memsize += entry.getHeader().getEventLength();
        }

        String startPosition = null;
        String endPosition = null;
        if (!CollectionUtils.isEmpty(message.getEntries())) {
            startPosition = buildPositionForDump(message.getEntries().get(0));
            endPosition = buildPositionForDump(message.getEntries().get(message.getEntries().size() - 1));
        }

        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
        logger.info(context_format, new Object[] { batchId, size, memsize, format.format(new Date()), startPosition,
                endPosition });
    }

    protected String buildPositionForDump(Entry entry) {
        long time = entry.getHeader().getExecuteTime();
        Date date = new Date(time);
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
        return entry.getHeader().getLogfileName() + ":" + entry.getHeader().getLogfileOffset() + ":"
               + entry.getHeader().getExecuteTime() + "(" + format.format(date) + ")";
    }

    protected void printEntry(List<Entry> entrys) {
        for (Entry entry : entrys) {
            long executeTime = entry.getHeader().getExecuteTime();
            long delayTime = new Date().getTime() - executeTime;

            if (entry.getEntryType() == EntryType.TRANSACTIONBEGIN || entry.getEntryType() == EntryType.TRANSACTIONEND) {
                if (entry.getEntryType() == EntryType.TRANSACTIONBEGIN) {
                    TransactionBegin begin = null;
                    try {
                        begin = TransactionBegin.parseFrom(entry.getStoreValue());
                    } catch (InvalidProtocolBufferException e) {
                        throw new RuntimeException("parse event has an error , data:" + entry.toString(), e);
                    }
                    // 打印事务头信息，执行的线程id，事务耗时
                    logger.info(transaction_format,
                        new Object[] { entry.getHeader().getLogfileName(),
                                String.valueOf(entry.getHeader().getLogfileOffset()),
                                String.valueOf(entry.getHeader().getExecuteTime()), String.valueOf(delayTime) });
                    logger.info(" BEGIN ----> Thread id: {}", begin.getThreadId());
                } else if (entry.getEntryType() == EntryType.TRANSACTIONEND) {
                    TransactionEnd end = null;
                    try {
                        end = TransactionEnd.parseFrom(entry.getStoreValue());
                    } catch (InvalidProtocolBufferException e) {
                        throw new RuntimeException("parse event has an error , data:" + entry.toString(), e);
                    }
                    // 打印事务提交信息，事务id
                    logger.info("----------------\n");
                    logger.info(" END ----> transaction id: {}", end.getTransactionId());
                    logger.info(transaction_format,
                        new Object[] { entry.getHeader().getLogfileName(),
                                String.valueOf(entry.getHeader().getLogfileOffset()),
                                String.valueOf(entry.getHeader().getExecuteTime()), String.valueOf(delayTime) });
                }

                continue;
            }

            if (entry.getEntryType() == EntryType.ROWDATA) {
                RowChange rowChage = null;
                try {
                    rowChage = RowChange.parseFrom(entry.getStoreValue());
                } catch (Exception e) {
                    throw new RuntimeException("parse event has an error , data:" + entry.toString(), e);
                }

                EventType eventType = rowChage.getEventType();

                logger.info(row_format,
                    new Object[] { entry.getHeader().getLogfileName(),
                            String.valueOf(entry.getHeader().getLogfileOffset()), entry.getHeader().getSchemaName(),
                            entry.getHeader().getTableName(), eventType,
                            String.valueOf(entry.getHeader().getExecuteTime()), String.valueOf(delayTime) });

                if (eventType == EventType.QUERY || rowChage.getIsDdl()) {
                    logger.info(" sql ----> " + rowChage.getSql() + SEP);
                    continue;
                }

                for (RowData rowData : rowChage.getRowDatasList()) {
                    if (eventType == EventType.DELETE) {
                        printColumn(rowData.getBeforeColumnsList());
                    } else if (eventType == EventType.INSERT) {
                        printColumn(rowData.getAfterColumnsList());
                    } else if (eventType == EventType.UPDATE) {
                        printColumn(rowData.getAfterColumnsList());
                    } else {
                    	printColumn(rowData.getAfterColumnsList());
                    }
                }
            }
        }
    }

    protected void printColumn(List<Column> columns) {
        for (Column column : columns) {
            StringBuilder builder = new StringBuilder();
            builder.append(column.getName() + " : " + column.getValue());
            builder.append("    type=" + column.getMysqlType());
            if (column.getUpdated()) {
                builder.append("    update=" + column.getUpdated());
            }
            builder.append(SEP);
            logger.info(builder.toString());
        }
    }

	

}
