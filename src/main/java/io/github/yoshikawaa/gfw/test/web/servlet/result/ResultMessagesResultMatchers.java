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

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.context.MessageSource;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.ui.Model;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.FlashMap;
import org.terasoluna.gfw.common.message.ResultMessage;
import org.terasoluna.gfw.common.message.ResultMessages;
import org.terasoluna.gfw.common.message.StandardResultMessageType;

/**
 * Result matchers for {@link ResultMessages}.
 * 
 * @author Atsushi Yoshikawa
 * @see ResultMatcher
 * @see ResultMessages
 * @see ResultMessage
 * @see MessageSource
 */
public class ResultMessagesResultMatchers {

    private final String resultMessagesAttributeName;
    private MvcResultAttributeObtainStrategy strategy = MvcResultAttributeObtainStrategy.REQUEST;

    /**
     * With default messages attribute name.
     */
    protected ResultMessagesResultMatchers() {
        this.resultMessagesAttributeName = ResultMessages.DEFAULT_MESSAGES_ATTRIBUTE_NAME;
    }

    /**
     * With specific messages attribute name.
     * 
     * @param resultMessagesAttributeName attribute name to be obtain
     */
    protected ResultMessagesResultMatchers(String resultMessagesAttributeName) {
        this.resultMessagesAttributeName = resultMessagesAttributeName;
    }

    /**
     * Obtain {@link ResultMessages} from {@link Model}.
     * 
     * @return itself
     */
    public ResultMessagesResultMatchers fromModel() {
        this.strategy = MvcResultAttributeObtainStrategy.MODEL;
        return this;
    }

    /**
     * Obtain {@link ResultMessages} from {@link FlashMap}.
     * 
     * @return itself
     */
    public ResultMessagesResultMatchers fromFlashMap() {
        this.strategy = MvcResultAttributeObtainStrategy.FLASH_MAP;
        return this;
    }

    /**
     * Obtain {@link ResultMessages} from {@link HttpServletRequest}.
     * 
     * @return itself
     */
    public ResultMessagesResultMatchers fromRequest() {
        this.strategy = MvcResultAttributeObtainStrategy.REQUEST;
        return this;
    }

    /**
     * Obtain {@link ResultMessages} from {@link HttpSession}.
     * 
     * @return itself
     */
    public ResultMessagesResultMatchers fromSession() {
        this.strategy = MvcResultAttributeObtainStrategy.SESSION;
        return this;
    }

    /**
     * {@link ResultMessages} exists.
     * 
     * @return result matcher
     */
    public ResultMatcher exists() {
        return new ResultMatcher() {
            @Override
            public void match(MvcResult result) {
                getResultMessagesWithAssert(result);
            }
        };
    }

    /**
     * {@link ResultMessages} not exists.
     * 
     * @return result matcher
     */
    public ResultMatcher notExists() {
        return new ResultMatcher() {
            @Override
            public void match(MvcResult result) {
                ResultMessages resultMessages = getResultMessages(result);
                assertThat("ResultMessages named '" + resultMessagesAttributeName + "' is found.", resultMessages,
                        nullValue());
            }
        };
    }

    /**
     * {@link ResultMessages} has specific type.
     * <p>
     * Correspond to etc {@link ResultMessages#success()}, {@link ResultMessages#warn()}...
     * </p>
     * 
     * @param type type of result messages
     * @return result matcher
     */
    public ResultMatcher type(final StandardResultMessageType type) {
        return new ResultMatcher() {
            @Override
            public void match(MvcResult result) {
                ResultMessages resultMessages = getResultMessagesWithAssert(result);
                assertThat("ResultMessages type is not '" + type + "'.", resultMessages.getType(), is(type));
            }
        };
    }

    /**
     * {@link ResultMessages} has specific codes.
     * <p>
     * Correspond to {@link ResultMessage#fromCode(String, Object...)}.
     * </p>
     * 
     * @param codes codes of result message
     * @return result matcher
     */
    public ResultMatcher codeExists(final String... codes) {
        return new ResultMatcher() {
            @Override
            public void match(MvcResult result) {
                List<String> codesOfResultMessages = new ArrayList<>();
                for (ResultMessage resultMessage : getResultMessagesWithAssert(result)) {
                    codesOfResultMessages.add(resultMessage.getCode());
                }
                for (String code : codes) {
                    assertThat("ResultMessages code '" + code + "' not exists.", codesOfResultMessages, hasItem(code));
                }
            }
        };
    }

    /**
     * {@link ResultMessages} has specific texts.
     * <p>
     * Correspond to {@link ResultMessage#fromText(String)}
     * </p>
     * 
     * @param texts texts of result message
     * @return result matcher
     */
    public ResultMatcher textExists(final String... texts) {
        return new ResultMatcher() {
            @Override
            public void match(MvcResult result) {
                List<String> textsOfResultMessages = new ArrayList<>();
                for (ResultMessage resultMessage : getResultMessagesWithAssert(result)) {
                    textsOfResultMessages.add(resultMessage.getText());
                }
                for (String text : texts) {
                    assertThat("ResultMessages text '" + text + "' not exists.", textsOfResultMessages, hasItem(text));
                }
            }
        };
    }

    /**
     * {@link ResultMessages} has specific messages with default locale.
     * <p>
     * Correspond to {@link ResultMessage#fromCode(String, Object...)} and {@link ResultMessage#fromText(String)}.
     * Message is resolved from {@link ResultMessage#getCode()} and {@link ResultMessage#getArgs()} using
     * {@link MessageSource}.
     * </p>
     * 
     * @param messages resolved messages of result message
     * @return result matcher
     * @see MessageSource
     */
    public ResultMatcher messageExists(final String... messages) {
        return new ResultMatcher() {
            @Override
            public void match(MvcResult result) {
                MessageSource messageSource = getMessageSource(result);
                List<String> messagesOfResultMessages = new ArrayList<>();
                for (ResultMessage resultMessage : getResultMessagesWithAssert(result)) {
                    messagesOfResultMessages.add(resolveMessage(messageSource, resultMessage, Locale.getDefault()));
                }
                for (String message : messages) {
                    assertThat("ResultMessages message '" + message + "' not exists.", messagesOfResultMessages,
                            hasItem(message));
                }
            }
        };
    }

    /**
     * {@link ResultMessages} has specific messages with specific locale.
     * <p>
     * Correspond to {@link ResultMessage#fromCode(String, Object...)} and {@link ResultMessage#fromText(String)}.
     * Message is resolved from {@link ResultMessage#getCode()} and {@link ResultMessage#getArgs()} using
     * {@link MessageSource} with specific locale.
     * </p>
     * 
     * @param locale locale using resolve message
     * @param messages resolved messages of result message
     * @return result matcher
     * @see MessageSource
     */
    public ResultMatcher messageExists(final Locale locale, final String... messages) {
        return new ResultMatcher() {
            @Override
            public void match(MvcResult result) {
                MessageSource messageSource = getMessageSource(result);
                List<String> messagesOfResultMessages = new ArrayList<>();
                for (ResultMessage resultMessage : getResultMessagesWithAssert(result)) {
                    messagesOfResultMessages.add(resolveMessage(messageSource, resultMessage, locale));
                }
                for (String message : messages) {
                    assertThat("ResultMessages message '" + message + "' not exists.", messagesOfResultMessages,
                            hasItem(message));
                }
            }
        };
    }

    private ResultMessages getResultMessagesWithAssert(MvcResult result) {

        ResultMessages resultMessages = getResultMessages(result);
        assertThat("ResultMessages named '" + resultMessagesAttributeName + "' is not found.", resultMessages,
                notNullValue());
        return resultMessages;
    }

    private ResultMessages getResultMessages(MvcResult result) {
        return (ResultMessages) strategy.obtainAttribute(result, resultMessagesAttributeName);
    }

    private String resolveMessage(MessageSource messageSource, ResultMessage resultMessage, Locale locale) {

        String code = resultMessage.getCode();
        if (code != null) {
            return messageSource.getMessage(code, resultMessage.getArgs(), locale);
        } else {
            return resultMessage.getText();
        }
    }

    private MessageSource getMessageSource(MvcResult result) {

        WebApplicationContext context = WebApplicationContextUtils
                .getWebApplicationContext(result.getRequest().getServletContext());
        return context.getBean(MessageSource.class);
    }

}
