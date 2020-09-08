package org.seckill.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.entity.Seckill;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 配置spring和junit整合，为了junit启动时加载ioc
 */
@RunWith(SpringJUnit4ClassRunner.class)
//告诉junit spring的配置文件
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SeckillDaoTest {

    @Resource
    private SeckillDao seckillDao;
    @Test
    public void reduceNumber() {
        Date killTime = new Date();
        int num = seckillDao.reduceNumber(1001,killTime);
        System.out.println("num="+num);
    }

    @Test
    public void queryById() {
        Seckill seckill = seckillDao.queryById(1000);
        System.out.println("name=" + seckill.getName());
        System.out.println("seckill="+seckill);
    }

    @Test
    public void queryAll() {

        //java没有记录形参  queryAll(int offet, int limit); 相当于queryAll(arg0, arg1)
        //所以需要告诉mybatis具体的参数名  通过@Param注解
        List<Seckill> seckillList = seckillDao.queryAll(0,100);
        for(Seckill item : seckillList){
            System.out.println(item);
        }

    }
}