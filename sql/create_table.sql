# 数据库初始化

-- 创建库
create database if not exists fragmentation;

-- 切换库
use fragmentation;

-- 用户表
create table if not exists user(
    id           bigint auto_increment comment 'id' primary key,
    user_account  varchar(256)                           not null comment '账号',
    user_password varchar(512)                           not null comment '密码',
    user_name     varchar(256)                           null comment '用户昵称',
    user_email     varchar(256)                           null comment '用户邮箱',
    user_avatar   varchar(1024)                          null comment '用户头像',
    user_profile  varchar(512)                           null comment '用户简介',
    user_role     varchar(256) default 'user'            not null comment '用户角色：user/admin/ban',
    create_time   datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time   datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    is_delete     tinyint      default 0                 not null comment '是否删除'
) comment '用户' collate = utf8mb4_unicode_ci;


-- 登录信息表
create table if not exists login_message(
                                   id           bigint auto_increment comment 'id' primary key,
                                   user_id  bigint                           not null comment '用户id',
                                   login_way varchar(512)                           not null comment '登录方式',
                                   login_device_name varchar(512)                           not null comment '设备名称',
                                   login_device_system varchar(512)                           not null comment '设备系统',
                                   login_time datetime                           not null comment '登录时间',
                                   create_time   datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
                                   update_time   datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
                                   is_delete     tinyint      default 0                 not null comment '是否删除'
) comment '用户登录信息表' collate = utf8mb4_unicode_ci;
