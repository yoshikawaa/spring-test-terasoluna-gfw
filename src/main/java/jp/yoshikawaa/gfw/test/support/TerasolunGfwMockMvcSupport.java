package jp.yoshikawaa.gfw.test.support;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.terasoluna.gfw.web.logging.mdc.MDCClearFilter;
import org.terasoluna.gfw.web.logging.mdc.XTrackMDCPutFilter;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextHierarchy({
        @ContextConfiguration({ "classpath*:META-INF/spring/applicationContext.xml",
                "classpath*:META-INF/spring/spring-security.xml" }),
        @ContextConfiguration("classpath*:META-INF/spring/spring-mvc.xml") })
public abstract class TerasolunGfwMockMvcSupport extends MockitoRuleSupport {

    protected MockMvc mvc;

    @Autowired
    private WebApplicationContext context;

    @Before
    public void setup() {
        mvc = setupMockMvc();
    }

    protected MockMvc setupMockMvc() {
        return MockMvcBuilders.webAppContextSetup(context)
                .addFilters(new MDCClearFilter(), new DelegatingFilterProxy("exceptionLoggingFilter", context),
                        new XTrackMDCPutFilter(), new CharacterEncodingFilter("UTF-8", true))
                .apply(springSecurity())
                .alwaysDo(log())
                .build();

    }
}
