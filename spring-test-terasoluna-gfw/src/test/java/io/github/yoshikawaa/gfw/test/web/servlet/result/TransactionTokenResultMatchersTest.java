package io.github.yoshikawaa.gfw.test.web.servlet.result;

import static io.github.yoshikawaa.gfw.test.web.servlet.request.TerasolunaGfwMockMvcRequestPostProcessors.transaction;
import static io.github.yoshikawaa.gfw.test.web.servlet.result.TerasolunaGfwMockMvcResultMatchers.transactionToken;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.ResultMatcher;

import io.github.yoshikawaa.gfw.test.web.servlet.AbstractMockMvcSupport;

@RunWith(SpringRunner.class)
public class TransactionTokenResultMatchersTest extends AbstractMockMvcSupport {

    @Test
    public void testTransactionGlobal() throws Exception {
        valid("/transaction/global/", transactionToken().global());
    }

    @Test
    public void testTransactionClass() throws Exception {
        valid("/transaction/class/", transactionToken().namespace("class"));
    }

    @Test
    public void testTransactionMethod() throws Exception {
        valid("/transaction/method/", transactionToken().namespace("method"));
    }

    @Test
    public void testTransactionClassMethod() throws Exception {
        valid("/transaction/class-method/", transactionToken().namespace("class", "method"));
    }

    @Test
    public void testNonToken() throws Exception {
        mvc.perform(get("/transaction/global/non-token").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("success"))
                .andExpect(transactionToken().notExists());
    }

    @Test
    public void testTransactionUpdated() throws Exception {
        mvc.perform(post("/transaction/global/in").with(csrf()).with(transaction()))
                .andExpect(status().isOk())
                .andExpect(view().name("success"))
                .andExpect(transactionToken().updated());
    }

    @Test
    public void testTransactionNotUpdated() throws Exception {
        mvc.perform(post("/transaction/global/check").with(csrf()).with(transaction()))
                .andExpect(status().isOk())
                .andExpect(view().name("success"))
                .andExpect(transactionToken().notUpdated());
    }

    private void valid(String path, ResultMatcher token) throws Exception {
        mvc.perform(get(path).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("success"))
                .andExpect(token);
    }

}
