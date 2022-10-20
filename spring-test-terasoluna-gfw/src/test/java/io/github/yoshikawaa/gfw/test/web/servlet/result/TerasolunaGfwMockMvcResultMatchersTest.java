package io.github.yoshikawaa.gfw.test.web.servlet.result;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import java.lang.reflect.Constructor;

import org.junit.Test;

public class TerasolunaGfwMockMvcResultMatchersTest {

    @Test
    public void testConstructor() throws Exception {
        Constructor<?> constructor = TerasolunaGfwMockMvcResultMatchers.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        assertThat(constructor.newInstance(), notNullValue());
    }

}
