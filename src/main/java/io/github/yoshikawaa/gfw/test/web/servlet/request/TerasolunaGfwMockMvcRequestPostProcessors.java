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
package io.github.yoshikawaa.gfw.test.web.servlet.request;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.util.StringUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.FrameworkServlet;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.handler.MappedInterceptor;
import org.terasoluna.gfw.web.token.TokenStringGenerator;
import org.terasoluna.gfw.web.token.transaction.TransactionToken;
import org.terasoluna.gfw.web.token.transaction.TransactionTokenInfo;
import org.terasoluna.gfw.web.token.transaction.TransactionTokenInterceptor;
import org.terasoluna.gfw.web.token.transaction.TransactionTokenStore;
import org.terasoluna.gfw.web.token.transaction.TransactionTokenType;

public final class TerasolunaGfwMockMvcRequestPostProcessors {

    public static TransactionTokenRequestPostProcessor transaction() {
        return new TransactionTokenRequestPostProcessor();
    }

    public static TransactionTokenRequestPostProcessor transaction(String namespace) {
        return new TransactionTokenRequestPostProcessor(namespace);
    }

    public static TransactionTokenRequestPostProcessor transaction(String classTokenName, String methodTokenName) {
        return new TransactionTokenRequestPostProcessor(classTokenName, methodTokenName);
    }

    public static class TransactionTokenRequestPostProcessor implements RequestPostProcessor {

        private static final String GLOBAL_TOKEN_NAME = "globalToken";

        private final String namespace;
        private boolean useInvalidToken;

        private TransactionTokenRequestPostProcessor() {
            this.namespace = GLOBAL_TOKEN_NAME;
        }

        private TransactionTokenRequestPostProcessor(String namespace) {
            this.namespace = (namespace != null && !namespace.isEmpty()) ? namespace : GLOBAL_TOKEN_NAME;
        }

        private TransactionTokenRequestPostProcessor(String classTokenName, String methodTokenName) {
            this.namespace = createTokenName(classTokenName, methodTokenName);
        }

        public TransactionTokenRequestPostProcessor useInvalidToken() {
            this.useInvalidToken = true;
            return this;
        }

        @Override
        public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {

            TransactionTokenInterceptor interceptor = getInterceptor(request);
            TestTransactionTokenStore tokenStore = setupTokenStore(interceptor);
            TokenStringGenerator generator = getGenerator(interceptor);
            TransactionToken token = createToken(tokenStore, generator);

            request.setParameter(TransactionTokenInterceptor.TOKEN_REQUEST_PARAMETER, token.getTokenString());
            return request;
        }

        private String createTokenName(final String classTokenName, final String methodTokenName) {

            StringBuilder tokenNameStringBuilder = new StringBuilder();
            if (StringUtils.hasText(classTokenName)) {
                tokenNameStringBuilder.append(classTokenName);
            }
            if (StringUtils.hasText(methodTokenName)) {
                if (tokenNameStringBuilder.length() != 0) {
                    tokenNameStringBuilder.append("/");
                }
                tokenNameStringBuilder.append(methodTokenName);
            }
            if (tokenNameStringBuilder.length() == 0) {
                tokenNameStringBuilder.append(GLOBAL_TOKEN_NAME);
            }

            return tokenNameStringBuilder.toString();
        }

        private TransactionTokenInterceptor getInterceptor(MockHttpServletRequest request) {

            WebApplicationContext context = WebApplicationContextUtils
                    .getWebApplicationContext(request.getServletContext(), FrameworkServlet.SERVLET_CONTEXT_PREFIX);
            for (Entry<String, MappedInterceptor> mapped : context.getBeansOfType(MappedInterceptor.class).entrySet()) {
                HandlerInterceptor interceptor = mapped.getValue().getInterceptor();
                if (interceptor instanceof TransactionTokenInterceptor) {
                    return (TransactionTokenInterceptor) interceptor;
                }
            }

            throw new NoSuchBeanDefinitionException(TransactionTokenInterceptor.class);
        }

        private TestTransactionTokenStore setupTokenStore(TransactionTokenInterceptor interceptor) {

            TransactionTokenStore actualTokenStore = (TransactionTokenStore) ReflectionTestUtils.getField(interceptor,
                    "tokenStore");
            if (actualTokenStore instanceof TestTransactionTokenStore) {
                return (TestTransactionTokenStore) actualTokenStore;
            }

            TestTransactionTokenStore tokenStore = new TestTransactionTokenStore(actualTokenStore);
            ReflectionTestUtils.setField(interceptor, "tokenStore", tokenStore);

            return tokenStore;
        }

        private TokenStringGenerator getGenerator(TransactionTokenInterceptor interceptor) {
            return (TokenStringGenerator) ReflectionTestUtils.getField(interceptor, "generator");
        }

        private TransactionToken createToken(TestTransactionTokenStore tokenStore, TokenStringGenerator generator) {
            TransactionTokenInfo tokenInfo = new TransactionTokenInfo(namespace, TransactionTokenType.BEGIN);
            String tokenKey = tokenStore.createTestTokenKey();
            String tokenValue = generator.generate(UUID.randomUUID().toString());
            TransactionToken token = new TransactionToken(tokenInfo.getTokenName(), tokenKey, tokenValue);
            tokenStore.remove(token);
            tokenStore.store(token);

            return useInvalidToken ? new TransactionToken(tokenInfo.getTokenName(), tokenKey, "invalid" + tokenValue)
                    : token;
        }

        private static class TestTransactionTokenStore implements TransactionTokenStore {

            private final static String ENABLED_TEST_TOKEN_KEY = TestTransactionTokenStore.class.getSimpleName()
                    .concat(".TEST");

            private final Map<String, String> store = new HashMap<>();

            private final TransactionTokenStore delegate;

            private TestTransactionTokenStore(TransactionTokenStore delegate) {
                this.delegate = delegate;
            }

            @Override
            public String getAndClear(TransactionToken token) {

                if (ENABLED_TEST_TOKEN_KEY.equals(token.getTokenKey())) {
                    String tokenValue = store.get(token.getTokenName());
                    store.remove(token.getTokenName());
                    return tokenValue;
                } else {
                    return delegate.getAndClear(token);
                }
            }

            @Override
            public void remove(TransactionToken token) {

                if (ENABLED_TEST_TOKEN_KEY.equals(token.getTokenKey())) {
                    store.remove(token.getTokenName());
                } else {
                    delegate.remove(token);
                }
            }

            @Override
            public String createAndReserveTokenKey(String tokenName) {
                return delegate.createAndReserveTokenKey(tokenName);
            }

            @Override
            public void store(TransactionToken token) {

                if (ENABLED_TEST_TOKEN_KEY.equals(token.getTokenKey())) {
                    store.put(token.getTokenName(), token.getTokenValue());
                } else {
                    delegate.store(token);
                }
            }

            public String createTestTokenKey() {
                return ENABLED_TEST_TOKEN_KEY;
            }
        }
    }

    private TerasolunaGfwMockMvcRequestPostProcessors() {
    }

}
