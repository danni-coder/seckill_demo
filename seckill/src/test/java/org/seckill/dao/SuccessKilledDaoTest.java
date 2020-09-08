package org.seckill.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.entity.SuccessKilled;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import java.security.PrivateKey;

import static org.junit.Assert.*;
/**
 * 配置spring和junit整合，为了junit启动时加载ioc
 */
@RunWith(SpringJUnit4ClassRunner.class)
//告诉junit spring的配置文件
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SuccessKilledDaoTest {

    @Resource
    private SuccessKilledDao successKilledDao;

    @Test
    public void insertSuccessKilled() {
        int num = successKilledDao.insertSuccessKilled(1000, 13288882223L);
        System.out.println("num="+num);
    }

    @Test
    public void queryByIdWithSeckill() {
        SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(1000,13212233212l);
        System.out.println("successKilled="+successKilled);
    }
}