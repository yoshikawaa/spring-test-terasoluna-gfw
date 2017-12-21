package io.github.yoshikawaa.gfw.test.context.jdbc;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.Appender;
import io.github.yoshikawaa.gfw.test.context.TestContextConfiguration;

@RunWith(SpringRunner.class)
@TestContextConfiguration
@Transactional
public class SqlAfterTest {

    @SuppressWarnings("unchecked")
    private static final Appender<ILoggingEvent> mockAppender = mock(Appender.class);
    private static int execDropCount = 0;

    @BeforeClass
    public static void setupBeforeClass() {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        Logger logger = loggerContext.getLogger("jdbc.sqltiming");
        when(mockAppender.getName()).thenReturn("MOCK");
        logger.addAppender(mockAppender);
    }

    @Test
    @SqlAfter("classpath:META-INF/database/sqlafter.sql")
    public void testValue() {
        assertTrue(true);
    }

    @Test
    @SqlAfter(scripts = "classpath:META-INF/database/sqlafter.sql")
    public void testScripts() {
        assertTrue(true);
    }

    @Test
    @SqlAfter(statements = "drop table if exists todo")
    public void testStatement() {
        assertTrue(true);
    }

    @After
    public void tearDown() {
        execDropCount++;
    }

    @AfterClass
    public static void tearDownAfterClass() {
        verify(mockAppender, times(execDropCount)).doAppend(argThat(new ArgumentMatcher<LoggingEvent>() {
            @Override
            public boolean matches(Object argument) {
                String message = ((LoggingEvent) argument).getFormattedMessage();
                return message.startsWith(" org.springframework.jdbc.datasource.init.ScriptUtils.executeSqlScript")
                        && message.contains("drop table if exists todo");
            }
        }));
    }

}
