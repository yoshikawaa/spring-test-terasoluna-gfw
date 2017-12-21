package io.github.yoshikawaa.gfw.test.context.jdbc;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import io.github.yoshikawaa.gfw.test.context.TestContextConfiguration;

@RunWith(SpringRunner.class)
@TestContextConfiguration
@Transactional
public class SqlBeforeTest {

    private static final String SQL_SELECT = "SELECT * FROM todo WHERE todo_id=?";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @SqlBefore("classpath:META-INF/database/sqlbefore.sql")
    public void testValue() {
        assertTodo();
    }

    @Test
    @SqlBefore(scripts = "classpath:META-INF/database/sqlbefore.sql")
    public void testScripts() {
        assertTodo();
    }

    @Test
    @SqlBefore(statements = {
            "drop table if exists todo",
            "create table if not exists todo(todo_id varchar(36) primary key, todo_title varchar(30), finished boolean, created_at timestamp)",
            "insert into todo ( todo_id, todo_title, created_at, finished ) values ('todo-001', 'todo 001', '2017-10-05', false)" })
    public void testStatement() {
        assertTodo();
    }

    private void assertTodo() {
        Map<String, Object> todo = jdbcTemplate.queryForMap(SQL_SELECT, "todo-001");
        assertThat(todo, notNullValue());
        assertThat(todo.get("todo_id"), is("todo-001"));
        assertThat(todo.get("todo_title"), is("todo 001"));
        assertThat(todo.get("created_at"),
                is(Timestamp.from(LocalDate.parse("2017-10-05").atStartOfDay(ZoneId.systemDefault()).toInstant())));
        assertThat(todo.get("finished"), is(false));
    }
    
}
