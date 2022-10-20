package io.github.yoshikawaa.gfw.test.web.servlet.result;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Arrays;

import org.junit.Test;

public class MvcResultAttributeObtainStrategyTest {

    @Test
    public void testEnumGenerated() throws Exception {
        assertThat(Arrays.asList(MvcResultAttributeObtainStrategy.values()),
                hasItems(MvcResultAttributeObtainStrategy.MODEL, MvcResultAttributeObtainStrategy.FLASH_MAP,
                        MvcResultAttributeObtainStrategy.REQUEST, MvcResultAttributeObtainStrategy.SESSION));
        assertThat(MvcResultAttributeObtainStrategy.valueOf("REQUEST"), is(MvcResultAttributeObtainStrategy.REQUEST));
    }

}
