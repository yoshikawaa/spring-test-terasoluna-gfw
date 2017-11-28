package io.github.yoshikawaa.gfw.test.web.servlet.result;

public final class TerasolunaGfwMockMvcResultMatchers {

    public static ResultMessagesResultMatchers resultMessages() {
        return new ResultMessagesResultMatchers();
    }

    public static ResultMessagesResultMatchers resultMessages(String resultMessagesAttributeName) {
        return new ResultMessagesResultMatchers(resultMessagesAttributeName);
    }

    private TerasolunaGfwMockMvcResultMatchers() {
    }

}
