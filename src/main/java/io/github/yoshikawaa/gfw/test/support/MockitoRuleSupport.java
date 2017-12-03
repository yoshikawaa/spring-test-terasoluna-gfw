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
package io.github.yoshikawaa.gfw.test.support;

import org.junit.Rule;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

/**
 * General mock test support with {@link MockitoRule}.
 * <p>
 * using {@link Mock}, {@link InjectMocks} as annotation driven.
 * </p>
 * 
 * @author Atsushi Yoshikawa
 * @see MockitoRule
 * @see Mock
 * @see InjectMocks
 */
public abstract class MockitoRuleSupport {

    @Rule
    public MockitoRule mockito = MockitoJUnit.rule();
}
