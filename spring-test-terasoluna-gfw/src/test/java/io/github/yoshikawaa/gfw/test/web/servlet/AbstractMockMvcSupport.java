package io.github.yoshikawaa.gfw.test.web.servlet;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;

import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import io.github.yoshikawaa.gfw.test.context.web.TerasolunaGfwWebAppTest;
import io.github.yoshikawaa.gfw.test.web.servlet.setup.TerasolunaGfwMockMvcBuilders;

@TerasolunaGfwWebAppTest
public abstract class AbstractMockMvcSupport {

    @Autowired
    private WebApplicationContext context;
    
    protected MockMvc mvc;

    @Before
    public void setup() {
        mvc = TerasolunaGfwMockMvcBuilders.setup(context).alwaysDo(log()).build();
    }
}
