package jp.yoshikawaa.gfw.test.web.servlet.result;

import static jp.yoshikawaa.gfw.test.web.servlet.result.TerasolunaGfwMockMvcResultMatchers.resultMessages;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Locale;

import org.junit.Test;
import org.terasoluna.gfw.common.message.StandardResultMessageType;

import jp.yoshikawaa.gfw.test.support.TerasolunGfwMockMvcSupport;

public class ResultMessagesResultMatchersTest extends TerasolunGfwMockMvcSupport {

    @Test
    public void testNonMessages() throws Exception {
        mvc.perform(get("/result-messages/non-messages"))
                .andExpect(status().isOk())
                .andExpect(view().name("success"))
                .andExpect(resultMessages().notExists());
    }

    @Test
    public void testBusinessError() throws Exception {
        mvc.perform(get("/result-messages/business-error"))
                .andExpect(status().isConflict())
                .andExpect(view().name("common/error/businessError"))
                .andExpect(resultMessages().exists())
                .andExpect(resultMessages().type(StandardResultMessageType.ERROR))
                .andExpect(resultMessages().messageExists("ResultMessage from code [1].",
                        "ResultMessage from code [1] and args [arg1,arg2].", "ResultMessage from text"));
    }

    @Test
    public void testFromModel() throws Exception {
        mvc.perform(get("/result-messages/from-model"))
                .andExpect(status().isOk())
                .andExpect(view().name("success"))
                .andExpect(resultMessages().fromModel().exists())
                .andExpect(resultMessages().fromModel().type(StandardResultMessageType.ERROR))
                .andExpect(resultMessages().fromModel().messageExists("ResultMessage from code [1].",
                        "ResultMessage from code [1] and args [arg1,arg2].", "ResultMessage from text"));
    }

    @Test
    public void testFromModelNamed() throws Exception {
        String name = "messages";
        mvc.perform(get("/result-messages/from-model").param("name", name))
                .andExpect(status().isOk())
                .andExpect(view().name("success"))
                .andExpect(resultMessages(name).fromModel().exists())
                .andExpect(resultMessages(name).fromModel().type(StandardResultMessageType.ERROR))
                .andExpect(resultMessages(name).fromModel().messageExists("ResultMessage from code [1].",
                        "ResultMessage from code [1] and args [arg1,arg2].", "ResultMessage from text"));
    }

    @Test
    public void testFromFlashMap() throws Exception {
        mvc.perform(get("/result-messages/from-flashmap"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("success"))
                .andExpect(resultMessages().fromFlashMap().exists())
                .andExpect(resultMessages().fromFlashMap().type(StandardResultMessageType.ERROR))
                .andExpect(resultMessages().fromFlashMap().messageExists("ResultMessage from code [1].",
                        "ResultMessage from code [1] and args [arg1,arg2].", "ResultMessage from text"));
    }

    @Test
    public void testFromFlashMapNamed() throws Exception {
        String name = "messages";
        mvc.perform(get("/result-messages/from-flashmap").param("name", name))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("success"))
                .andExpect(resultMessages(name).fromFlashMap().exists())
                .andExpect(resultMessages(name).fromFlashMap().type(StandardResultMessageType.ERROR))
                .andExpect(resultMessages(name).fromFlashMap().messageExists("ResultMessage from code [1].",
                        "ResultMessage from code [1] and args [arg1,arg2].", "ResultMessage from text"));
    }

    @Test
    public void testFromRequest() throws Exception {
        mvc.perform(get("/result-messages/from-request"))
                .andExpect(status().isOk())
                .andExpect(view().name("success"))
                .andExpect(resultMessages().fromRequest().exists())
                .andExpect(resultMessages().fromRequest().type(StandardResultMessageType.ERROR))
                .andExpect(resultMessages().fromRequest().messageExists("ResultMessage from code [1].",
                        "ResultMessage from code [1] and args [arg1,arg2].", "ResultMessage from text"));
    }

    @Test
    public void testFromRequestNamed() throws Exception {
        String name = "messages";
        mvc.perform(get("/result-messages/from-request").param("name", name))
                .andExpect(status().isOk())
                .andExpect(view().name("success"))
                .andExpect(resultMessages(name).fromRequest().exists())
                .andExpect(resultMessages(name).fromRequest().type(StandardResultMessageType.ERROR))
                .andExpect(resultMessages(name).fromRequest().messageExists("ResultMessage from code [1].",
                        "ResultMessage from code [1] and args [arg1,arg2].", "ResultMessage from text"));
    }

    @Test
    public void testFromSession() throws Exception {
        mvc.perform(get("/result-messages/from-session"))
                .andExpect(status().isOk())
                .andExpect(view().name("success"))
                .andExpect(resultMessages().fromSession().exists())
                .andExpect(resultMessages().fromSession().type(StandardResultMessageType.ERROR))
                .andExpect(resultMessages().fromSession().messageExists("ResultMessage from code [1].",
                        "ResultMessage from code [1] and args [arg1,arg2].", "ResultMessage from text"));
    }

    @Test
    public void testFromSessionNamed() throws Exception {
        String name = "messages";
        mvc.perform(get("/result-messages/from-session").param("name", name))
                .andExpect(status().isOk())
                .andExpect(view().name("success"))
                .andExpect(resultMessages(name).fromSession().exists())
                .andExpect(resultMessages(name).fromSession().type(StandardResultMessageType.ERROR))
                .andExpect(resultMessages(name).fromSession().messageExists("ResultMessage from code [1].",
                        "ResultMessage from code [1] and args [arg1,arg2].", "ResultMessage from text"));
    }

    @Test
    public void testCodeExists() throws Exception {
        mvc.perform(get("/result-messages/business-error"))
                .andExpect(status().isConflict())
                .andExpect(view().name("common/error/businessError"))
                .andExpect(resultMessages().exists())
                .andExpect(resultMessages().type(StandardResultMessageType.ERROR))
                .andExpect(resultMessages().codeExists("e.xx.fw.8011", "e.xx.fw.8021"));
    }

    @Test
    public void testTextExists() throws Exception {
        mvc.perform(get("/result-messages/business-error"))
                .andExpect(status().isConflict())
                .andExpect(view().name("common/error/businessError"))
                .andExpect(resultMessages().exists())
                .andExpect(resultMessages().type(StandardResultMessageType.ERROR))
                .andExpect(resultMessages().textExists("ResultMessage from text"));
    }

    @Test
    public void testMessageExistsWithLocale() throws Exception {
        mvc.perform(get("/result-messages/business-error"))
                .andExpect(status().isConflict())
                .andExpect(view().name("common/error/businessError"))
                .andExpect(resultMessages().exists())
                .andExpect(resultMessages().type(StandardResultMessageType.ERROR))
                .andExpect(resultMessages().messageExists(Locale.FRANCE, "ResultMessage du code [1].",
                        "ResultMessage du code [1] et des arguments [arg1,arg2].", "ResultMessage from text"));
    }

}
