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
package io.github.yoshikawaa.gfw.test.util;

import org.springframework.util.StringUtils;
import org.terasoluna.gfw.web.token.transaction.TransactionToken;
import org.terasoluna.gfw.web.token.transaction.TransactionTokenCheck;
import org.terasoluna.gfw.web.token.transaction.TransactionTokenInfoStore;

/**
 * Utility for handling {@link TransactionToken}.
 * 
 * @author Atsushi Yoshikawa
 * @see TransactionToken
 * @see TransactionTokenCheck
 * @see TransactionTokenInfoStore
 */
public class TransactionTokenUtil {

    /**
     * Default name of {@link TransactionToken}.
     * <p>
     * Using this name if not specified {@link TransactionTokenCheck#namespace()}.
     * </p>
     *  
     */
    public static final String GLOBAL_TOKEN_NAME = "globalToken";

    /**
     * Create name of {@link TransactionToken}.
     * <p>
     * Specify value of {@link TransactionTokenCheck#namespace()} at class level and method level.
     * </p>
     * 
     * @param classTokenName value of {@link TransactionTokenCheck#namespace()} at class level
     * @param methodTokenName value of {@link TransactionTokenCheck#namespace()} at method level
     * @return name of {@link TransactionToken}
     */
    public static String createTokenName(final String classTokenName, final String methodTokenName) {

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
    
    private TransactionTokenUtil() {
    }

}