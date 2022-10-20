package io.github.yoshikawaa.gfw.test.web.servlet.request;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import java.lang.reflect.Constructor;

import org.junit.Test;

public class TerasolunaGfwMockMvcRequestPostProcessorsTest {

    @Test
    public void testConstructor() throws Exception {
        Constructor<?> constructor = TerasolunaGfwMockMvcRequestPostProcessors.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        assertThat(constructor.newInstance(), notNullValue());
    }

}
