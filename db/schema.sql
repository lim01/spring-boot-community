use community;

create table article (
	id int unsigned not null primary key auto_increment,
    regDate datetime not null,
    updateDate datetime not null,
    title varchar(100) not null,
    body text
);

insert into article set regDate = now(), updateDate = now(), title = '제목 1', body = '내용 1';
insert into article set regDate = now(), updateDate = now(), title = '제목 2', body = '내용 2';
insert into article set regDate = now(), updateDate = now(), title = '제목 3', body = '내용 3';

select * from article;