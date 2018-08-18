package io.github.yoshikawaa.gfw.test.util;

import static org.junit.Assert.fail;

import java.lang.reflect.Constructor;

import org.junit.Test;

public class TransactionTokenUtilTest {

    @Test
    public void testConstructor() {
        try {
            Constructor<TransactionTokenUtil> constructor = TransactionTokenUtil.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            constructor.newInstance();
        } catch (Exception e) {
            fail(e.getMessage());
        }

    }
}
