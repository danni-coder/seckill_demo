/*秒杀执行的存储过程*/
DELIMITER $$ -- 换行转换为 $$

/*参数 in 输入参数；out 输出参数*/
/*row_count():返回上一条修改类型sql影响行数 0:未修改数据 >0:标识修改的行数  <0:位置错误*/
create procedure `seckill`.`execute_seckill`
    (in v_seckill_id bigint,in v_phone bigint,in v_kill_time timestamp,out  r_result int)
    BEGIN
        DECLARE insert_count int DEFAULT 0;
        START TRANSACTION ;
        INSERT IGNORE INTO success_killed
            (SECKILL_ID, USER_PHONE, CREATE_TIME)
            VALUES (v_seckill_id,v_phone,v_kill_time);
        select row_count() into insert_count;
        IF(insert_count =0) THEN
            ROLLBACK ;
            SET r_result = -1;
        ELSEIF(insert_count < 0) THEN
            ROLLBACK ;
            SET r_result = -2;
        ELSE
            UPDATE seckill
            SET number = number -1
            WHERE seckill_id = v_seckill_id
            AND end_time > v_kill_time
            AND start_time < v_kill_time
            AND number > 0;
            SELECT ROW_COUNT() INTO insert_count;
            IF(insert_count = 0) THEN
                ROLLBACK ;
                SET r_result = 0;
            ELSEIF (insert_count < 0) THEN
                ROLLBACK ;
                SET r_result = -2;
            ELSE
                COMMIT ;
                SET r_result = 1;
            end if;
        end if;
    end;
$$
/*存储过程定义结束*/

DELIMITER ;
/**
  调用存储过程
 */
set @r_result =-3;
call execute_seckill(1001,1329999111,now(),@r_result);
/*查看结果*/
select @r_result;