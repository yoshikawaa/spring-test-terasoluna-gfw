package io.github.yoshikawaa.gfw.app;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.terasoluna.gfw.web.token.transaction.TransactionTokenCheck;
import org.terasoluna.gfw.web.token.transaction.TransactionTokenType;

@Controller
@RequestMapping("transaction/global")
public class TransactionTokenGlobalTokenController {

    @GetMapping
    @TransactionTokenCheck(type = TransactionTokenType.BEGIN)
    public String begin() {
        return "success";
    }
    
    @PostMapping("in")
    @TransactionTokenCheck(type = TransactionTokenType.IN)
    public String in() {
        return "success";
    }
    
    @PostMapping("check")
    @TransactionTokenCheck(type = TransactionTokenType.CHECK)
    public String check() {
        return "success";
    }
    
    @PostMapping("end")
    @TransactionTokenCheck(type = TransactionTokenType.END)
    public String end() {
        return "success";
    }
    
    @GetMapping("non-token")
    public String nonToken() {
        return "success";
    }
    
}
