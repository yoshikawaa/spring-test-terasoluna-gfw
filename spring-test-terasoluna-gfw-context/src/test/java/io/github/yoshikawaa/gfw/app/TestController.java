package io.github.yoshikawaa.gfw.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.github.dozermapper.core.Mapper;

@Controller
@RequestMapping("/test")
public class TestController {

    @Autowired
    Mapper beanMapper;

    @PostMapping
    public String post(TestForm form, Model model) {
        model.addAttribute("name", beanMapper.map(form, TestModel.class).getName());
        return "success";
    }

    public static class TestForm {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class TestModel {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
