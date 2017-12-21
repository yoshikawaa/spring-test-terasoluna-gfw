/**
 * Copyright (c) 2017 Atsushi Yoshikawa (https://yoshikawaa.github.io)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.yoshikawaa.gfw.test.support;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.terasoluna.gfw.web.logging.mdc.MDCClearFilter;
import org.terasoluna.gfw.web.logging.mdc.XTrackMDCPutFilter;

import io.github.yoshikawaa.gfw.test.context.web.WebAppContextConfiguration;

/**
 * Controller test support using {@link MockMvc} configured with web application context.
 * 
 * @author Atsushi Yoshikawa
 * @see MockMvcSupport
 * @see WebAppContextConfiguration
 */
@WebAppContextConfiguration
public abstract class WebAppContextMockMvcSupport extends MockMvcSupport {

    @Autowired
    private WebApplicationContext context;

    /**
     * {@inheritDoc}
     */
    @Override
    protected MockMvc setupMockMvc() {
        return MockMvcBuilders.webAppContextSetup(context)
                .addFilters(new MDCClearFilter(), new DelegatingFilterProxy("exceptionLoggingFilter", context),
                        new XTrackMDCPutFilter(), new CharacterEncodingFilter("UTF-8", true))
                .apply(springSecurity())
                .alwaysDo(log())
                .build();
    }

}
