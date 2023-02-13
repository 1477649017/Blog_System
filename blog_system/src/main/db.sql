-- 首先是数据库设计，有哪些表，表中的字段如何
-- 我们的博客系统主要是两个方面，一个用户，一个博客，所以设计两个表就好了
-- text是长文本数据，64kb 一般用来存储博客是完全够用的 单纯文本 图片啥的是另外的空间存储的
-- userId就是用户的id  --加上外键的约数
-- 自增主键从1开始，传入null会自动从1开始增加
-- 存在外键约数，注意表之间的顺序 以及外键定义的写法
-- 为什么新创建的库 表还需要先判断存在删除 因为在业务中给你的新的数据库不一定完全干净 所以这样可以确保你的数据库 表创建的没有错
create database if not exists java105_blog_system charset utf8mb4;

use java105_blog_system;


drop table if exists user;
create table user (
    userId int primary key auto_increment,
    username varchar(50) unique,
    password varchar(50)
);


drop table if exists blog;
create table blog (
    blogId int primary key auto_increment,
    title varchar(256),
    content text,
    postTime datetime,
    userId int,
    foreign key (userId) references user(userId)
);



insert into user values(null,"张三","1234");
insert into user values(null,"李四","8888");

insert into blog values(null,"博客系统设计","博客系统前端设计如下：XXX","2022-11-25 12:00:00",1);
insert into blog values(null,"博客系统设计","博客系统前端设计如下：XXX","2022-11-25 12:00:00",1);

insert into blog values(null,"博客系统设计","博客系统前端设计如下：XXX","2022-11-26 12:00:00",1);
insert into blog values(null,"博客系统设计","博客系统前端设计如下：XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXxxxXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXxx","2022-11-27 12:00:00",1);