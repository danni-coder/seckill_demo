package org.seckill.dao;

import org.apache.ibatis.annotations.Param;
import org.seckill.entity.Seckill;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface SeckillDao {
    /**
     * 减库存
     * @param seckillId  活动ID
     * @param killTime 描述时间
     * @return 影响行数
     */
    int reduceNumber(@Param("seckillId") long seckillId,@Param("killTime") Date killTime);

    /**
     * 根据ID查询描述对象
     * @param seckillId 活动ID
     * @return 活动对象
     */
    Seckill queryById(long seckillId);
    /**
     * 根据偏移量查询秒杀商品列表
     */
    List<Seckill> queryAll(@Param("offset") int offset,@Param("limit") int limit);

    /**
     * 存储过程执行秒杀
     * @param objectMap
     */
    void killByProcedure(Map<String,Object> objectMap);
}
