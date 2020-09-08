package org.seckill.service;

import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;

import java.util.List;

/**
 * 站在“使用者”角度设计接口
 */
public interface SeckillService {
    /**
     * 查询所有秒杀活动
     * @return
     */
    List<Seckill> getSeckillList();

    /**
     * 查询单个秒杀活动
     * @param seckillId
     * @return
     */
    Seckill getById(long seckillId);

    /**
     * 秒杀开启输出接口地址
     * 否则输出系统当前时间和活动开始结束时间
     * @param seckillId
     * @return
     */
    Exposer exportSeckillUrl(long seckillId);

    /**
     * 执行秒杀
     * @param seckillId 活动ID
     * @param userPhone 秒杀手机号
     * @param md5 加密MD5
     * @return 秒杀结果对象
     * @throws SeckillException   秒杀内部异常
     * @throws SeckillCloseException 秒杀关闭异常
     * @throws RepeatKillException 重复秒杀异常
     */
    SeckillExecution executeSeckill(Long seckillId, Long userPhone, String md5)
        throws SeckillException, SeckillCloseException, RepeatKillException;

    /**
     * 调用存储过程完成秒杀
     * @param seckillId
     * @param userPhone
     * @param md5
     * @return
     * @throws SeckillException
     * @throws SeckillCloseException
     * @throws RepeatKillException
     */
    SeckillExecution executeSeckillProceduce(Long seckillId, Long userPhone, String md5);
}
