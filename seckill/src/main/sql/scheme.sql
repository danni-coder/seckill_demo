--数据库初始化脚本

--创建数据库
CREATE DATABASE SECKILL;
use seckill;
--创建数据库表
create table seckill(
`seckill_id` BIGINT not null  AUTO_INCREMENT COMMENT '秒杀商品库存id',
`name` varchar(120) not null  comment '秒杀商品名称',
`number` int not null comment '库存数量',
`start_time` timestamp not  null comment '开始时间',
`end_time` timestamp not null  comment '结束时间',
`create_time` timestamp  not null default current_timestamp comment '创建时间',
PRIMARY KEY (seckill_id),
KEY INX_START_TIME(start_time),
KEY INX_END_TIME(end_time),
KEY INE_CREATE_TIME(create_time)
)ENGINE=InnoDB AUTO_INCREMENT=1000  comment='秒杀库存表'


--初始化历史数据
insert into
    seckill(name,number,start_time,end_time)
values
    ('1000秒杀iphone11',100,'2020-08-26 00:00:00','2020-09-26 00:00:00'),
        ('500秒杀iphoneX',200,'2020-09-26 00:00:00','2020-10-26 00:00:00'),
            ('300秒杀iphone8',300,'2020-09-26 00:00:00','2020-10-26 00:00:00'),
                ('200秒杀iphone7',400,'2020-09-26 00:00:00','2020-10-26 00:00:00')


--秒杀成功明细表
create table success_killed(
`seckill_id` bigint not null comment '描述商品ID',
`user_phone` bigint not null comment '用户手机号',
`state` tinyint not null default 0 comment '状态标识：-1 无效,0 成功,1 已付款',
`create_time` timestamp not null  default current_timestamp comment '创建时间',
primary key(seckill_id,user_phone),
key inx_create_time(create_time)
)ENGINE=InnoDB  comment='秒杀成功明细表'