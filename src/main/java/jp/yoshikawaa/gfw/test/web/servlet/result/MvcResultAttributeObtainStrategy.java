package jp.yoshikawaa.gfw.test.web.servlet.result;

import org.springframework.test.web.servlet.MvcResult;

public enum MvcResultAttributeObtainStrategy {

    MODEL {
        @Override
        protected Object obtainAttribute(MvcResult result, String attributeName) {
            return result.getModelAndView().getModel().get(attributeName);
        }
    },
    FLASH_MAP {
        @Override
        protected Object obtainAttribute(MvcResult result, String attributeName) {
            return result.getFlashMap().get(attributeName);
        }
    },
    REQUEST {
        @Override
        protected Object obtainAttribute(MvcResult result, String attributeName) {
            return result.getRequest().getAttribute(attributeName);
        }
    },
    SESSION {
        @Override
        protected Object obtainAttribute(MvcResult result, String attributeName) {
            return result.getRequest().getSession().getAttribute(attributeName);
        }
    };

    protected abstract Object obtainAttribute(MvcResult result, String attributeName);
}
