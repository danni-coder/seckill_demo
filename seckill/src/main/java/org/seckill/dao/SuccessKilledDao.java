package org.seckill.dao;

import org.apache.ibatis.annotations.Param;
import org.seckill.entity.SuccessKilled;

public interface SuccessKilledDao {
    /**
     * 插入购买明细，可过滤重复
     * 设计主键时，设置了联合主键
     * @param seckillId
     * @param userPhone
     * @return  插入的行数
     */
    int insertSuccessKilled(@Param("seckillId") long seckillId,@Param("userPhone") long userPhone);
    /**
     * 根据ID查询成功记录并携带秒杀活动信息
     */
    SuccessKilled queryByIdWithSeckill(@Param("seckillId") long seckillId,@Param("userPhone") long userPhone);
}