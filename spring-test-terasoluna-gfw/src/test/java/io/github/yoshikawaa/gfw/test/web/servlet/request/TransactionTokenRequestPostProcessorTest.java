package io.github.yoshikawaa.gfw.test.web.servlet.request;

import static io.github.yoshikawaa.gfw.test.web.servlet.request.TerasolunaGfwMockMvcRequestPostProcessors.transaction;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.terasoluna.gfw.web.token.transaction.TransactionToken;
import org.terasoluna.gfw.web.token.transaction.TransactionTokenInterceptor;

import io.github.yoshikawaa.gfw.app.TransactionTokenClassTokenController;
import io.github.yoshikawaa.gfw.app.TransactionTokenGlobalTokenController;
import io.github.yoshikawaa.gfw.test.web.servlet.AbstractMockMvcSupport;
import io.github.yoshikawaa.gfw.test.web.servlet.request.TerasolunaGfwMockMvcRequestPostProcessors.TransactionTokenRequestPostProcessor;

@RunWith(Enclosed.class)
public class TransactionTokenRequestPostProcessorTest {

    @RunWith(SpringRunner.class)
    public static class ForTerasolunaGfwWebBlankTest extends AbstractMockMvcSupport {

        @Rule
        public ExpectedException exception = ExpectedException.none();

        @Test
        public void testTransactionGlobal() throws Exception {
            valid("/transaction/global/in", transaction());
            valid("/transaction/global/check", transaction());
            valid("/transaction/global/end", transaction());
            valid("/transaction/global/end", transaction(null));
            valid("/transaction/global/end", transaction(""));
            valid("/transaction/global/end", transaction(null, null));
            valid("/transaction/global/end", transaction("", ""));
        }

        @Test
        public void testTransactionClass() throws Exception {
            valid("/transaction/class/in", transaction("class"));
            valid("/transaction/class/check", transaction("class"));
            valid("/transaction/class/end", transaction("class"));
            valid("/transaction/class/end", transaction("class", null));
            valid("/transaction/class/end", transaction("class", ""));
        }

        @Test
        public void testTransactionMethod() throws Exception {
            valid("/transaction/method/in", transaction("method"));
            valid("/transaction/method/check", transaction("method"));
            valid("/transaction/method/end", transaction("method"));
            valid("/transaction/method/end", transaction(null, "method"));
            valid("/transaction/method/end", transaction("", "method"));
        }

        @Test
        public void testTransactionClassMethod() throws Exception {
            valid("/transaction/class-method/in", transaction("class", "method"));
            valid("/transaction/class-method/check", transaction("class", "method"));
            valid("/transaction/class-method/end", transaction("class", "method"));

        }

        @Test
        public void testTransactionInvalid() throws Exception {
            invalid("/transaction/global/in", transaction().useInvalidToken());
        }

        @Test
        public void testTransactionTokenInterceptorNotFound() throws Exception {
            exception.expect(NoSuchBeanDefinitionException.class);
            exception.expectMessage(containsString("org.terasoluna.gfw.web.token.transaction.TransactionTokenInterceptor"));

            MockMvc mvc = MockMvcBuilders.standaloneSetup(new TransactionTokenGlobalTokenController()).build();
            mvc.perform(post("/transaction/global/in").with(transaction()));
        }

        @Test
        public void testUseActualTransactonTokenStore() throws Exception {

            MvcResult result = null;
            MockHttpSession session = null;
            TransactionToken token = null;

            // use TestTransactionTokenStore:in
            valid("/transaction/global/in", transaction());

            // use actual TransactionTokenStore:begin
            result = mvc.perform(get("/transaction/global"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("success"))
                    .andExpect(request().attribute(TransactionTokenInterceptor.NEXT_TOKEN_REQUEST_ATTRIBUTE_NAME,
                            instanceOf(TransactionToken.class)))
                    .andReturn();
            session = (MockHttpSession) result.getRequest().getSession();
            token = (TransactionToken) result.getRequest()
                    .getAttribute(TransactionTokenInterceptor.NEXT_TOKEN_REQUEST_ATTRIBUTE_NAME);

            // use actual TransactionTokenStore:in
            result = mvc
                    .perform(post("/transaction/global/in").session(session)
                            .param(TransactionTokenInterceptor.TOKEN_REQUEST_PARAMETER, token.getTokenString())
                            .with(csrf()))
                    .andExpect(status().isOk())
                    .andExpect(view().name("success"))
                    .andExpect(request().attribute(TransactionTokenInterceptor.NEXT_TOKEN_REQUEST_ATTRIBUTE_NAME,
                            instanceOf(TransactionToken.class)))
                    .andReturn();
            session = (MockHttpSession) result.getRequest().getSession();
            token = (TransactionToken) result.getRequest()
                    .getAttribute(TransactionTokenInterceptor.NEXT_TOKEN_REQUEST_ATTRIBUTE_NAME);

            // use actual TransactionTokenStore:check
            result = mvc
                    .perform(post("/transaction/global/check").session(session)
                            .param(TransactionTokenInterceptor.TOKEN_REQUEST_PARAMETER, token.getTokenString())
                            .with(csrf()))
                    .andExpect(status().isOk())
                    .andExpect(view().name("success"))
                    .andExpect(request().attribute(TransactionTokenInterceptor.NEXT_TOKEN_REQUEST_ATTRIBUTE_NAME,
                            instanceOf(TransactionToken.class)))
                    .andReturn();
            session = (MockHttpSession) result.getRequest().getSession();
            token = (TransactionToken) result.getRequest()
                    .getAttribute(TransactionTokenInterceptor.NEXT_TOKEN_REQUEST_ATTRIBUTE_NAME);

            // use actual TransactionTokenStore:end
            mvc.perform(post("/transaction/global/end").session(session)
                    .param(TransactionTokenInterceptor.TOKEN_REQUEST_PARAMETER, token.getTokenString())
                    .with(csrf()))
                    .andExpect(status().isOk())
                    .andExpect(view().name("success"))
                    .andExpect(request().attribute(TransactionTokenInterceptor.NEXT_TOKEN_REQUEST_ATTRIBUTE_NAME,
                            nullValue()));
        }

        private void valid(String path, TransactionTokenRequestPostProcessor transaction) throws Exception {
            mvc.perform(post(path).with(csrf()).with(transaction))
                    .andExpect(status().isOk())
                    .andExpect(view().name("success"));
        }

        private void invalid(String path, TransactionTokenRequestPostProcessor transaction) throws Exception {
            mvc.perform(post(path).with(csrf()).with(transaction))
                    .andExpect(status().isConflict())
                    .andExpect(view().name("common/error/transactionTokenError"));
        }
    }

    @RunWith(SpringRunner.class)
    @WebAppConfiguration
    @ContextConfiguration(classes = ForJavaConfigTest.TestConfig.class)
    public static class ForJavaConfigTest {

        @Autowired
        private WebApplicationContext context;

        private MockMvc mvc;

        @Before
        public void setup() throws Exception {
            mvc = MockMvcBuilders.webAppContextSetup(context).build();
        }

        @Test
        public void testTransactionGlobal() throws Exception {
            valid("/transaction/global/in", transaction());
            valid("/transaction/global/check", transaction());
            valid("/transaction/global/end", transaction());
            valid("/transaction/global/end", transaction(null));
            valid("/transaction/global/end", transaction(""));
            valid("/transaction/global/end", transaction(null, null));
            valid("/transaction/global/end", transaction("", ""));
        }

        private void valid(String path, TransactionTokenRequestPostProcessor transaction) throws Exception {
            mvc.perform(post(path).with(csrf()).with(transaction))
                    .andExpect(status().isOk())
                    .andExpect(view().name("success"));
        }

        @Configuration
        @EnableWebMvc
        @ComponentScan(basePackageClasses = TransactionTokenClassTokenController.class)
        public static class TestConfig implements WebMvcConfigurer {

            @Bean
            public TransactionTokenInterceptor transactionTokenInterceptor() {
                return new TransactionTokenInterceptor();
            }

            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                registry.addInterceptor(transactionTokenInterceptor());
            }
        }
    }
}
