package org.seckill.dao.cache;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.dao.SeckillDao;
import org.seckill.entity.Seckill;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;
/**
 * 配置spring和junit整合，为了junit启动时加载ioc
 */
@RunWith(SpringJUnit4ClassRunner.class)
//告诉junit spring的配置文件
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class RedisDaoTest {
    private long id = 1001L;
    @Resource
    private RedisDao redisDao;
    @Resource
    private SeckillDao seckillDao;

    @Test
    public void getSeckill() {
        Seckill seckill = redisDao.getSeckill(id);
        if (seckill == null) {
            seckill = seckillDao.queryById(id);
            if (seckill != null) {
                String result = redisDao.putSeckill(seckill);
                System.out.println("result=" + result);
                redisDao.getSeckill(id);
                System.out.println(seckill);
            }
        } else {
            System.out.println("redis result=" + seckill);
        }
    }
}