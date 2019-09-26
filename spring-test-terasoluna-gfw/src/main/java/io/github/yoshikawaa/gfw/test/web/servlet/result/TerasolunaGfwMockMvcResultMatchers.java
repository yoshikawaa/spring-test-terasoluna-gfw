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

import org.springframework.test.web.servlet.ResultActions;
import org.terasoluna.gfw.common.message.ResultMessages;
import org.terasoluna.gfw.web.token.transaction.TransactionToken;

/**
 * {@link ResultActions} result matchers for TERASOLUNA common libraries.
 * 
 * @author Atsushi Yoshikawa
 * @see ResultActions
 */
public final class TerasolunaGfwMockMvcResultMatchers {

    /**
     * Matches {@link TransactionToken}.
     * 
     * @return result matchers
     * @see TransactionToken
     */
    public static TransactionTokenResultMatchers transactionToken() {
        return new TransactionTokenResultMatchers();
    }

    /**
     * Matches {@link ResultMessages} with default messages attribute name.
     * 
     * @return result matchers
     * @see ResultMessages
     */
    public static ResultMessagesResultMatchers resultMessages() {
        return new ResultMessagesResultMatchers();
    }

    /**
     * Matches {@link ResultMessages} with specific messages attribute name.
     * 
     * @param resultMessagesAttributeName attribute name to be obtain
     * @return result matchers
     * @see ResultMessages
     */
    public static ResultMessagesResultMatchers resultMessages(String resultMessagesAttributeName) {
        return new ResultMessagesResultMatchers(resultMessagesAttributeName);
    }

    private TerasolunaGfwMockMvcResultMatchers() {
    }

}
