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

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Controller test support using {@link MockMvc}.
 * 
 * @author Atsushi Yoshikawa
 * @see SpringRunner
 * @see MockMvc
 * @see MockitoRuleSupport
 */
@RunWith(SpringRunner.class)
public abstract class MockMvcSupport extends MockitoRuleSupport {

    /**
     * {@link MockMvc} configured at {@link #setupMockMvc()}.
     */
    protected MockMvc mvc;

    /**
     * Set up test cases.
     */
    @Before
    public void setup() {
        mvc = setupMockMvc();
    }

    /**
     * Set up {@link MockMvc}.
     * 
     * @return configured {@link MockMvc}
     */
    protected abstract MockMvc setupMockMvc();

}