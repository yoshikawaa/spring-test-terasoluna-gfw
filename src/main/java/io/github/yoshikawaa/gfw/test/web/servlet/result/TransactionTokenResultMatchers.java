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
package io.github.yoshikawaa.gfw.test.web.servlet.result;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.terasoluna.gfw.web.token.transaction.TransactionToken;
import org.terasoluna.gfw.web.token.transaction.TransactionTokenCheck;
import org.terasoluna.gfw.web.token.transaction.TransactionTokenInterceptor;
import org.terasoluna.gfw.web.token.transaction.TransactionTokenType;

import io.github.yoshikawaa.gfw.test.util.TransactionTokenUtil;

/**
 * Result matchers for {@link TransactionToken}.
 * 
 * @author Atsushi Yoshikawa
 * @see ResultMatcher
 * @see TransactionToken
 */
public class TransactionTokenResultMatchers {

    /**
     * {@link TransactionToken} without {@link TransactionTokenCheck#namespace} exists.
     * 
     * @return result matcher
     */
    public ResultMatcher global() {
        return new ResultMatcher() {
            @Override
            public void match(MvcResult result) {
                TransactionToken token = getNextTokenWithAssert(result);
                assertThat("TransactionToken namespace is not match.", token.getTokenName(),
                        is(TransactionTokenUtil.GLOBAL_TOKEN_NAME));
            }
        };
    }

    /**
     * {@link TransactionToken} with simple {@link TransactionTokenCheck#namespace} exists.
     * 
     * @param namespace specified namespace at class or method.
     * @return result matcher
     */
    public ResultMatcher namespace(String namespace) {
        return new ResultMatcher() {
            @Override
            public void match(MvcResult result) {
                TransactionToken token = getNextTokenWithAssert(result);
                assertThat("TransactionToken namespace is not match.", token.getTokenName(), is(namespace));
            }
        };
    }

    /**
     * {@link TransactionToken} with complex {@link TransactionTokenCheck#namespace} exists.
     * 
     * @param classTokenName specified namespace at class.
     * @param methodTokenName specified namespace at method.
     * @return result matcher
     */
    public ResultMatcher namespace(String classTokenName, String methodTokenName) {
        return new ResultMatcher() {
            @Override
            public void match(MvcResult result) {
                TransactionToken token = getNextTokenWithAssert(result);
                assertThat("TransactionToken namespace is not match.", token.getTokenName(),
                        is(TransactionTokenUtil.createTokenName(classTokenName, methodTokenName)));
            }
        };
    }

    /**
     * {@link TransactionToken} not exists.
     * 
     * @return result matcher
     */
    public ResultMatcher notExists() {
        return new ResultMatcher() {
            @Override
            public void match(MvcResult result) {
                TransactionToken token = getNextToken(result);
                assertThat("TransactionToken is found.", token, nullValue());
            }
        };
    }

    /**
     * {@link TransactionToken} updated with {@link TransactionTokenType#IN}.
     * 
     * @return result matcher
     */
    public ResultMatcher updated() {
        return new ResultMatcher() {
            @Override
            public void match(MvcResult result) {
                TransactionToken token = getNextTokenWithAssert(result);
                String priviousTokenString = getPriviousTokenStringWithAssert(result);
                assertThat("TransactionToken is not updated.", token.getTokenString(), not(priviousTokenString));
            }
        };
    }

    /**
     * {@link TransactionToken} not updated with {@link TransactionTokenType#CHECK}.
     * 
     * @return result matcher
     */
    public ResultMatcher notUpdated() {
        return new ResultMatcher() {
            @Override
            public void match(MvcResult result) {
                TransactionToken token = getNextTokenWithAssert(result);
                String priviousTokenString = getPriviousTokenStringWithAssert(result);
                assertThat("TransactionToken is updated.", token.getTokenString(), is(priviousTokenString));
            }
        };
    }

    private TransactionToken getNextTokenWithAssert(MvcResult result) {
        TransactionToken token = getNextToken(result);
        assertThat("TransactionToken is not found.", token, notNullValue());
        return token;
    }

    private TransactionToken getNextToken(MvcResult result) {
        return (TransactionToken) result.getRequest()
                .getAttribute(TransactionTokenInterceptor.NEXT_TOKEN_REQUEST_ATTRIBUTE_NAME);
    }

    private String getPriviousTokenStringWithAssert(MvcResult result) {
        String priviousTokenString = getPriviousTokenString(result);
        assertThat("TransactionToken is not found in request parameter.", priviousTokenString, notNullValue());
        return priviousTokenString;
    }

    private String getPriviousTokenString(MvcResult result) {
        return result.getRequest().getParameter(TransactionTokenInterceptor.TOKEN_REQUEST_PARAMETER);
    }

}
