package org.seckill.dao.cache;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import org.seckill.entity.Seckill;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.Serializable;

public class RedisDao {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private JedisPool jedisPool;

    private RuntimeSchema<Seckill> schema = RuntimeSchema.createFrom(Seckill.class);

    public RedisDao(String ip, int port){
        jedisPool = new JedisPool(ip,port);
    }

    /**
     * 获取缓存对象
     * @param seckillId
     * @return
     */
    public Seckill getSeckill(Long seckillId){
        Jedis jedis = null;
        //redis操作逻辑
        try{
            jedis = jedisPool.getResource();

            String key = "seckill:" + seckillId;
            //并没有实现内部序列化操作
            //采用自定义序列化
            byte[] bytes = jedis.get(key.getBytes());
            if(bytes != null){
                //获取一个空对象
                Seckill seckill = schema.newMessage();
                //缓存中获取到  反序列化并放到空对象中
                ProtostuffIOUtil.mergeFrom(bytes,seckill,schema);
                return seckill;
            }
        }catch(Exception e){
            logger.error(e.getMessage(),e);
        }finally {
            if(jedis!= null){
                jedis.close();
            }
        }
        return null;
    }
    //放置缓存
    public String putSeckill(Seckill seckill){
        //set Object(Seckill) -> 序列化 -》byte[]
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();

            String key = "seckill:"+seckill.getSeckillId();
            byte[] bytes = ProtostuffIOUtil.toByteArray(seckill,schema,
                    LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
            //超时缓存
            int timeout = 60 * 60; //1小时
            String result = jedis.setex(key.getBytes(),timeout,bytes);
            return result;
        }catch(Exception e){
            logger.error(e.getMessage(),e);
        }finally {
            if(jedis!= null){
                jedis.close();
            }
        }
        return null;
    }
}
