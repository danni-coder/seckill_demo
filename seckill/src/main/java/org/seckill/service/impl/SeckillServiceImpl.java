package org.seckill.service.impl;

import org.apache.commons.collections.MapUtils;
import org.seckill.dao.SeckillDao;
import org.seckill.dao.SuccessKilledDao;
import org.seckill.dao.cache.RedisDao;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.entity.SuccessKilled;
import org.seckill.enums.SeckillStateEnum;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;
import org.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SeckillServiceImpl implements SeckillService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    //MD5盐值  用来混淆MD5
    private final String slat="sdadwq2443546fsdx_=qw=sasd22";

    @Resource
    private SeckillDao seckillDao;

    @Resource
    private SuccessKilledDao successKilledDao;

    @Resource
    private RedisDao redisDao;

    @Override
    public List<Seckill> getSeckillList() {
        return seckillDao.queryAll(0,4);
    }

    @Override
    public Seckill getById(long seckillId) {
        return seckillDao.queryById(seckillId);
    }

    @Override
    public Exposer exportSeckillUrl(long seckillId) {
        //缓存优化
        //Seckill seckill = this.getById(seckillId);
        //1:访问缓存
        Seckill seckill = redisDao.getSeckill(seckillId);
        if(seckill == null){
            //return new Exposer(false,seckillId);
            //2:访问数据库
            seckill = this.getById(seckillId);
            if(seckill == null){
                return new Exposer(false,seckillId);
            }
            //3:存在 则放入缓存中
            redisDao.putSeckill(seckill);
        }
        //获取活动开始结束时间戳
        long startTime = seckill.getStartTime().getTime();
        long endTime = seckill.getEndTime().getTime();
        //系统当前时间
        long now = new Date().getTime();
        //活动未开始或者已结束
        if(now < startTime || now > endTime){
            return new Exposer(false,seckillId,now,startTime,endTime);
        }
        //转化特定字符串的过程，不可逆
        String md5 = this.getMD5(seckillId);
        return new Exposer(true,md5,seckillId);
    }

    private String getMD5 (long seckillId){
        String base = seckillId + "/" + slat;
        return DigestUtils.md5DigestAsHex(base.getBytes());
    }
    @Override
    @Transactional
    /**
     * 抛出运行期异常将回滚
     * 使用注解控制事物方法的优点
     * 1：开发团队达成一致约定  明确标注事物方法的编程风格
     * 2：保证事物方法的执行时间尽可能短，尽量不要穿插其他的网络操作 RPC/HTTP 如果必须要  尽量剥离出此方法
     * 3：不是所有的方法都需要开启事物 如只有一条修改操作或者只读操作
     */
    public SeckillExecution executeSeckill(Long seckillId, Long userPhone, String md5) throws SeckillException, SeckillCloseException,RepeatKillException {
        if(md5 == null || !md5.equals(this.getMD5(seckillId))){
            throw new RepeatKillException("seckill data rewrite");
        }
        //当前时间
        Date now = new Date();
        try{
            //先insert在update 减少行级锁
            //记录购买行为
            int insertNum = successKilledDao.insertSuccessKilled(seckillId,userPhone);
            if (insertNum == 0){
                throw new RepeatKillException("seckill is Repeat");
            }else{
                //执行秒杀  减库存 + 记录
                int updateNum = seckillDao.reduceNumber(seckillId, now);
                if(updateNum <= 0){
                    //没有减库存
                    throw new SeckillCloseException("seckill is close");
                }else{
                    //秒杀成功
                    SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId,userPhone);
                    return new SeckillExecution(seckillId, SeckillStateEnum.SUCCESS,successKilled);
                }
            }
        }catch (SeckillCloseException e1){
            throw e1;
        }catch(RepeatKillException e2){
            throw e2;
        }catch(Exception e){
            logger.error("executeSeckill error:{}",e.getMessage());
            //所有编译器异常转化为运行期异常
            throw  new SeckillException("seckill inner error:" + e.getMessage());
        }
    }

    @Override
    public SeckillExecution executeSeckillProceduce(Long seckillId, Long userPhone, String md5) {
        //md5的验证
        if(md5 == null || !md5.equals(this.getMD5(seckillId))){
            return new SeckillExecution(seckillId,SeckillStateEnum.DATA_REWRITE);
        }
        Date killTime = new Date();
        Map<String, Object> map = new HashMap<>();
        map.put("seckillId",seckillId);
        map.put("phone",userPhone);
        map.put("killTime",killTime);
        map.put("result",null);
        try{
            seckillDao.killByProcedure(map);
            int result = MapUtils.getInteger(map,"result",-2);
            if(result == 1){
                SuccessKilled sk = successKilledDao.queryByIdWithSeckill(seckillId,userPhone);
                return new SeckillExecution(seckillId,SeckillStateEnum.SUCCESS,sk);
            }else{
                return new SeckillExecution(seckillId,SeckillStateEnum.getStateInfo(result));
            }

        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return new SeckillExecution(seckillId,SeckillStateEnum.INNER_ERROR);
        }
    }
}
