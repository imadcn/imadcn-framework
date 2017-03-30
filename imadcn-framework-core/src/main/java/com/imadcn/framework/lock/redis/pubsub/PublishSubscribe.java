package com.imadcn.framework.lock.redis.pubsub;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

/**
 * 发布订阅工具
 * @author imadcn
 */
public abstract class PublishSubscribe<E extends PubSubEntry<E>> {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PublishSubscribe.class);
	
	private final ConcurrentMap<String, E> entries =  new ConcurrentHashMap<String, E>(); // 锁资源监听
    private final ConcurrentMap<String, MessageListener> LISTENER_MAP = new ConcurrentHashMap<String, MessageListener>(); // 监听器map
    
    /**
     * 获取事件实例
     * @param entryName
     * @return
     */
    public E getEntry(String entryName) {
        return entries.get(entryName);
    }
	
    /**
     * 订阅监听事件
     * @param entryName
     * @param channelName
     * @param container
     * @return
     */
    public E subscribe(String entryName, String channelName, RedisMessageListenerContainer container) {
    	synchronized (this) {
            E entry = entries.get(entryName);
            if (entry != null) {
                entry.acquire();
                return entry;
            }

            E value = createEntry(channelName);
            value.acquire();

            E oldValue = entries.putIfAbsent(entryName, value);
            if (oldValue != null) {
                oldValue.acquire();
                return entry;
            }
            
    		MessageListener listener = creatMessageListener(channelName, value); // 创建监听器
    		if (LISTENER_MAP.putIfAbsent(entryName, listener) == null) {
    			LOGGER.debug("message listener added with entry name [{}], channel name [{}]", entryName, channelName);
    			container.addMessageListener(listener, new ChannelTopic(channelName));
    		}
    		return value;
        }
	}
	
	/**
	 * 取消订阅监听事件
	 * @param entry
	 * @param entryName
	 * @param container
	 */
	public void unsubscribe(E entry, String entryName, RedisMessageListenerContainer container) {
		synchronized (this) {
            if (entry.release() == 0) {
                boolean removed = entries.remove(entryName) == entry;
                if (removed) {
                	MessageListener listener = LISTENER_MAP.remove(entryName);
            		if (listener != null) {
            			container.removeMessageListener(listener);
            		}
                }
            }
            LOGGER.debug("entry size [{}], listener size [{}]", entries.size(), LISTENER_MAP.size());
        }
	}
	
	/**
	 * 实例化监听触发接口
	 * @param channelName
	 * @param value
	 * @return
	 */
	private MessageListener creatMessageListener(final String channelName, final E value) {
		final String tag = channelName + ":" + Thread.currentThread().getId();
		MessageListenerAdapter listener = new MessageListenerAdapter() {
			@Override
			public void onMessage(Message message, byte[] pattern) {
				LOGGER.debug("message received in channel-thread [{}]", tag);
				Object pubsubMessage = extractMessage(message); // 订阅消息内容
				PublishSubscribe.this.onMessage(value, pubsubMessage);
			}
		};
		return listener;
	}
	
	/**
	 * 创建资源监听订阅实例
	 * @param channelName
	 * @return
	 */
	protected abstract E createEntry(String channelName);
	
    /**
     * 订阅消息获取后，处理方法
     * @param value
     * @param message
     */
    protected abstract void onMessage(E value, Object message);
}
