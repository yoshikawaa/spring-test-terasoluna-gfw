package io.github.yoshikawaa.gfw.test.web.servlet.setup;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import io.github.yoshikawaa.gfw.test.context.web.TerasolunaGfwWebAppTest;

@RunWith(SpringRunner.class)
@TerasolunaGfwWebAppTest
public class TerasolunaGfwMockMvcBuildersTest {

    @Autowired
    WebApplicationContext context;

    @Test
    public void testSetup() throws Exception {
        // setup
        MockMvc mvc = TerasolunaGfwMockMvcBuilders.setup(context).alwaysDo(log()).build();

        // execute & assert
        mvc.perform(post("/test").param("name", "test")) //
                .andExpect(status().isForbidden()) //
                .andExpect(forwardedUrl("/WEB-INF/views/common/error/missingCsrfTokenError.jsp"));

        mvc.perform(post("/test").with(csrf()).param("name", "test")) //
                .andExpect(status().isOk()) //
                .andExpect(view().name("success")) //
                .andExpect(model().attribute("name", "test"));
    }
}
