package io.github.yoshikawaa.gfw.app;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.terasoluna.gfw.common.exception.BusinessException;
import org.terasoluna.gfw.common.message.ResultMessage;
import org.terasoluna.gfw.common.message.ResultMessages;

@Controller
@RequestMapping("result-messages")
public class ResultMessagesController {

    private final ResultMessages resultMessages = ResultMessages.error().add(ResultMessage.fromCode("e.xx.fw.8011"))
            .add(ResultMessage.fromCode("e.xx.fw.8021", "arg1", "arg2"))
            .add(ResultMessage.fromText("ResultMessage from text"));

    @GetMapping("non-messages")
    public String nonMessages() {
        return "success";
    }

    @GetMapping("business-error")
    public String businessError() {
        throw new BusinessException(resultMessages);
    }

    @GetMapping("from-model")
    public String fromModel(Model model) {
        model.addAttribute(resultMessages);
        return "success";
    }

    @GetMapping(value = "from-model", params = "name")
    public String fromModel(Model model, @RequestParam("name") String name) {
        model.addAttribute(name, resultMessages);
        return "success";
    }

    @GetMapping("from-flashmap")
    public String fromFlashMap(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute(resultMessages);
        return "redirect:success";
    }

    @GetMapping(value = "from-flashmap", params = "name")
    public String fromFlashMap(RedirectAttributes redirectAttributes, @RequestParam("name") String name) {
        redirectAttributes.addFlashAttribute(name, resultMessages);
        return "redirect:success";
    }

    @GetMapping("from-request")
    public String fromRequest(HttpServletRequest request) {
        request.setAttribute(ResultMessages.DEFAULT_MESSAGES_ATTRIBUTE_NAME, resultMessages);
        return "success";
    }

    @GetMapping(value = "from-request", params = "name")
    public String fromRequest(HttpServletRequest request, @RequestParam("name") String name) {
        request.setAttribute(name, resultMessages);
        return "success";
    }

    @GetMapping("from-session")
    public String fromSession(HttpSession session) {
        session.setAttribute(ResultMessages.DEFAULT_MESSAGES_ATTRIBUTE_NAME, resultMessages);
        return "success";
    }

    @GetMapping(value = "from-session", params = "name")
    public String fromSession(HttpSession session, @RequestParam("name") String name) {
        session.setAttribute(name, resultMessages);
        return "success";
    }

}
