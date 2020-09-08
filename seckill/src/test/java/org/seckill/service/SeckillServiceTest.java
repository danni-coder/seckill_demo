package org.seckill.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.List;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml","classpath:spring/spring-service.xml"})
public class SeckillServiceTest {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private SeckillService seckillService;

    @Test
    public void getSeckillList() {
        List<Seckill> seckillList = seckillService.getSeckillList();
        for(Seckill seckill : seckillList){
            System.out.println("seckill="+seckill);
        }
    }

    @Test
    public void getById() {
        long seckillId = 1000L;
        Seckill seckill = seckillService.getById(seckillId);
        System.out.println("seckill="+seckill);
    }

    @Test
    public void exportSeckillUrl() {
        long seckillId = 1001L;
        Exposer exposer = seckillService.exportSeckillUrl(seckillId);
        System.out.println("exposer="+exposer);
    }

    @Test
    public void executeSeckill() {
        long seckillId = 1000L;
        long userPhone = 13312321115L;
        Exposer exposer = seckillService.exportSeckillUrl(seckillId);
        if(exposer.isExpesed()){
            try{
                SeckillExecution seckillExecution = seckillService.executeSeckill(seckillId,userPhone,exposer.getMd5());
                logger.info("seckillExecution="+seckillExecution);
            }catch(Exception e){
                logger.error(e.getMessage(),e);
            }
        }else{
            logger.warn("exposer="+exposer);
        }
    }
    @Test
    public void executeSeckillProceduce() {
        long seckillId = 1000L;
        long userPhone = 13312321116L;
        Exposer exposer = seckillService.exportSeckillUrl(seckillId);
        if(exposer.isExpesed()){
            try{
                SeckillExecution seckillExecution = seckillService.executeSeckillProceduce(seckillId,userPhone,exposer.getMd5());
                logger.info("seckillExecution="+seckillExecution);
            }catch(Exception e){
                logger.error(e.getMessage(),e);
            }
        }else{
            logger.warn("exposer="+exposer);
        }
    }
}