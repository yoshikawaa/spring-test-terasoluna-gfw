package jp.yoshikawaa.gfw.test.web.servlet.result;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.terasoluna.gfw.common.message.ResultMessage;
import org.terasoluna.gfw.common.message.ResultMessages;
import org.terasoluna.gfw.common.message.StandardResultMessageType;

public class ResultMessagesResultMatchers {

    private final String resultMessagesAttributeName;
    private MvcResultAttributeObtainStrategy strategy = MvcResultAttributeObtainStrategy.REQUEST;

    protected ResultMessagesResultMatchers() {
        this.resultMessagesAttributeName = ResultMessages.DEFAULT_MESSAGES_ATTRIBUTE_NAME;
    }

    protected ResultMessagesResultMatchers(String resultMessagesAttributeName) {
        this.resultMessagesAttributeName = resultMessagesAttributeName;
    }

    public ResultMessagesResultMatchers fromModel() {
        this.strategy = MvcResultAttributeObtainStrategy.MODEL;
        return this;
    }

    public ResultMessagesResultMatchers fromFlashMap() {
        this.strategy = MvcResultAttributeObtainStrategy.FLASH_MAP;
        return this;
    }

    public ResultMessagesResultMatchers fromRequest() {
        this.strategy = MvcResultAttributeObtainStrategy.REQUEST;
        return this;
    }

    public ResultMessagesResultMatchers fromSession() {
        this.strategy = MvcResultAttributeObtainStrategy.SESSION;
        return this;
    }

    public <T> ResultMatcher exists() {
        return new ResultMatcher() {
            @Override
            public void match(MvcResult result) {
                getResultMessagesWithAssert(result);
            }
        };
    }

    public <T> ResultMatcher notExists() {
        return new ResultMatcher() {
            @Override
            public void match(MvcResult result) {
                ResultMessages resultMessages = getResultMessages(result);
                assertThat("ResultMessages named '" + resultMessagesAttributeName + "' is found.", resultMessages,
                        nullValue());
            }
        };
    }

    public <T> ResultMatcher type(final StandardResultMessageType type) {
        return new ResultMatcher() {
            @Override
            public void match(MvcResult result) {
                ResultMessages resultMessages = getResultMessagesWithAssert(result);
                assertThat("ResultMessages type is not '" + type + "'.", resultMessages.getType(), is(type));
            }
        };
    }

    public <T> ResultMatcher codeExists(final String... codes) {
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

    public <T> ResultMatcher textExists(final String... texts) {
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

    public <T> ResultMatcher messageExists(final String... messages) {
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

    public <T> ResultMatcher messageExists(final Locale locale, final String... messages) {
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
