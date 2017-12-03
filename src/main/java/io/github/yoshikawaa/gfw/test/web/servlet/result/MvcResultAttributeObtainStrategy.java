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
