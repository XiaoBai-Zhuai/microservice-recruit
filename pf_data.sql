-- auto-generated definition
-- Unexpected: LocalDataSource

create table message (
                         id int primary key auto_increment,
                         from_id int not null default 0 comment '发送方id，系统发送则id为0',
                         to_id int not null default 0 comment '接收方id',
                         title varchar(30) not null comment '站内信标题',
                         content varchar(128) not null comment '站内信内容',
                         read_state bool not null comment '是否已阅',
                         create_time    timestamp default '2016-12-31 16:00:01' not null comment '创建时间',
                         update_time    timestamp default '2016-12-31 16:00:01' not null on update CURRENT_TIMESTAMP comment '更新时间'
) charset = utf8;

create table user_info (
                           id int primary key auto_increment,
                           user_id int not null comment '用户id',
                           name varchar(30) comment '姓名',
                           username varchar(30) comment '用户名',
                           nickname varchar(30) comment '昵称',
                           sex varchar(1) comment '性别',
                           introduce varchar(128) comment '个人介绍',
                           address varchar(30) comment '地址',
                           intention_company varchar(30) comment '意向公司',
                           end_time int comment '毕业时间',
                           school varchar(30) comment '毕业学校',
                           education varchar(10) comment '学历',
                           intention_job varchar(30) comment '意向岗位',
                           avatar varchar(40) comment '头像',
                           create_time    timestamp default '2016-12-31 16:00:01' not null comment '创建时间',
                           update_time    timestamp default '2016-12-31 16:00:01' not null on update CURRENT_TIMESTAMP comment '更新时间'
) charset = utf8;

create table company (
                         id int primary key auto_increment,
                         name varchar(30) comment '公司名称',
                         address varchar(30) comment '地址',
                         introduce varchar(128) comment '公司简介',
                         scale varchar(30) comment '公司规模',
                         type varchar(30) comment '公司类型',
                         avatar varchar(40) comment '头像',
                         create_time    timestamp default '2016-12-31 16:00:01' not null comment '创建时间',
                         update_time    timestamp default '2016-12-31 16:00:01' not null on update CURRENT_TIMESTAMP comment '更新时间'
) charset = utf8;

create table recruit (
                         id int primary key auto_increment,
                         company_id int not null comment '关联的公司id',
                         hr_id int not null comment '关联的hr_id',
                         content varchar(128) comment '招聘内容',
                         title varchar(30) comment '招聘标题',
                         skill_str varchar(128) comment '招聘技能要求',
                         create_time    timestamp default '2016-12-31 16:00:01' not null comment '创建时间',
                         update_time    timestamp default '2016-12-31 16:00:01' not null on update CURRENT_TIMESTAMP comment '更新时间'
) charset = utf8;