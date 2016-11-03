package com.sztx.trade.payment.gateway.dataaccess.dao.redis.impl;

import com.sztx.se.common.util.StringUtil;
import com.sztx.se.dataaccess.redis.callback.RedisCallback;
import com.sztx.se.dataaccess.redis.impl.BaseRedisDAOImpl;
import com.sztx.trade.payment.gateway.dataaccess.dao.redis.RedisConstant;
import com.sztx.trade.payment.gateway.dataaccess.dao.redis.VerifyCodeRedisDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * fuquanemail@gmail.com 2016/10/24 16:19
 * description:
 */

@Repository("verifyCodeRedisDAOImpl")
public class VerifyCodeRedisDAOImpl extends BaseRedisDAOImpl implements VerifyCodeRedisDAO {

    private static final String CODE_KEY = "code";
    private static final String VERIFY_NUM = "num";
    private static final long EXPIRE_SECONDS = 60 * 3;

    private static Logger log = LoggerFactory.getLogger(VerifyCodeRedisDAOImpl.class);

    @Override
    public Boolean save(final String orderNo, final String code) {

        return redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public String getKey() {
                return RedisConstant.VERIFYCODE_KEY + orderNo;
            }

            @Override
            public Boolean doInRedis(RedisConnection connection, byte[] key) {
                Map<byte[], byte[]> hashes = new HashMap<>();

                hashes.put(serialize(CODE_KEY), serialize(code));
                hashes.put(serialize(VERIFY_NUM), serialize(0));

                connection.hMSet(key, hashes);
                connection.expire(key, EXPIRE_SECONDS);
                log.info("redis save verifyCode, key={} , code={}",RedisConstant.VERIFYCODE_KEY + orderNo, code );

                return Boolean.TRUE;
            }
        });

    }

    @Override
    public VerifyResult verify(final String orderNo, final String code) {

        final String lua = "if(redis.call('EXISTS',KEYS[1]) ~= 1 ) then" +
                "   return '1';" +
                "   end;" +
                "if(redis.call('HEXISTS', KEYS[1], KEYS[2]) ~= 1 ) then " +
                "   return '1';" +
                "end;" +
                "local counter = redis.call('HINCRBY', KEYS[1], KEYS[3], 1); " +
                "if (counter > 2) then " +
                "     redis.call('DEL',KEYS[1]);" +
                "     return '2';" +
                "else " +
                "     return redis.call('HGET',KEYS[1],KEYS[2]); " +
                "end;";

        final VerifyResult result = new VerifyResult();
        redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public String getKey() {
                return RedisConstant.VERIFYCODE_KEY + orderNo;
            }

            @Override
            public Boolean doInRedis(RedisConnection connection, byte[] key) {

                byte[] bytes = connection.eval(serialize(lua), ReturnType.VALUE, 3, key, serialize(CODE_KEY), serialize(VERIFY_NUM));
                if (bytes == null || bytes.length == 0) {
                    result.setErrCode("1");
                    return Boolean.FALSE;
                }

                String response = deserialize(String.class, bytes);

                log.info("redis get verifyCode, key={} , code={}",RedisConstant.VERIFYCODE_KEY + orderNo, response );

                if (StringUtil.isEmpty(response) || "1".equals(response)) {
                    result.setErrCode("2");
                    return Boolean.FALSE;
                }

                if ("2".equals(response)) {
                    result.setErrCode("4");
                    return Boolean.FALSE;
                }

                if (response.equals(code)) {
                    connection.del(key);
                    return Boolean.TRUE;
                } else {
                    result.setErrCode("3");
                    return Boolean.FALSE;
                }
            }
        });

        return result;
    }

    public Boolean clear(final String orderNo) {
        return redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public String getKey() {
                return RedisConstant.VERIFYCODE_KEY + orderNo;
            }

            @Override
            public Boolean doInRedis(RedisConnection connection, byte[] key) {
                connection.del(key);
                return Boolean.TRUE;
            }
        });
    }

    public static class VerifyResult implements Serializable {

        private boolean succeeded;

        // 1. 程序异常
        // 2. 验证码以失效，请重新获取.
        // 3. 验证码错误，请核对后重新输入.
        // 4. 验证码超过规定次数，请重新生成订单.

        private String errCode;

        public String getErrCode() {
            return errCode;
        }

        public void setErrCode(String errCode) {
            this.errCode = errCode;
        }

        public boolean isSucceeded() {
            return StringUtil.isEmpty(errCode);
        }
    }
}
