package com.imadcn.framework.redis.lock.jedis;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

import com.imadcn.framework.redis.pubsub.PubSubEntry;

public abstract class JedisPublishSubscribe<E extends PubSubEntry<E>> {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(JedisPublishSubscribe.class);
	
	private final ConcurrentMap<String, E> entries =  new ConcurrentHashMap<String, E>(); // 锁资源监听
	private final ConcurrentMap<String, JedisPubSub> LISTENER_MAP = new ConcurrentHashMap<String, JedisPubSub>(); // 监听器map
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
    public E subscribe(String entryName, String channelName, Jedis jedis) {
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
            JedisPubSub listener = creatMessageListener(channelName, value);
    		if (LISTENER_MAP.putIfAbsent(entryName, listener) == null) {
    			LOGGER.debug("message listener added with entry name [{}], channel name [{}]", entryName, channelName);
    			jedis.subscribe(listener, channelName);
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
	public void unsubscribe(E entry, String entryName, Jedis jedis) {
		synchronized (this) {
            if (entry.release() == 0) {
                boolean removed = entries.remove(entryName) == entry;
                if (removed) {
                	JedisPubSub listener = LISTENER_MAP.remove(entryName);
            		if (listener != null) {
            			listener.unsubscribe();
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
	private JedisPubSub creatMessageListener(final String channelName, final E value) {
		final String tag = channelName + ":" + Thread.currentThread().getId();
		JedisPubSub pubSub = new JedisPubSub() {
			@Override
			public void onMessage(String channel, String message) {
				LOGGER.debug("message received in channel-thread [{}]", tag);
				JedisPublishSubscribe.this.onMessage(value, message);
			}
		};
		return pubSub;
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
