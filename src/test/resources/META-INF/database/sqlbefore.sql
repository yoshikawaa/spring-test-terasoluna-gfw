drop table if exists todo;
create table if not exists todo(todo_id varchar(36) primary key, todo_title varchar(30), finished boolean, created_at timestamp);
insert into todo ( todo_id, todo_title, created_at, finished ) values ('todo-001', 'todo 001', '2017-10-05', false);