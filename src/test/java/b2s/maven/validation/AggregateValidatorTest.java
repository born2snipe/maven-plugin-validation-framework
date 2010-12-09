/**
 * Copyright to the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */
package b2s.maven.validation;

import b2s.maven.PluginContext;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;

import static org.mockito.Mockito.*;


public class AggregateValidatorTest {
    private PluginContext context;
    private PluginValidator child;
    private PluginErrors errors;

    @Test
    public void multipleValidators_stopOfFirstFailure() {
        PluginValidator child2 = mock(PluginValidator.class);

        AggregateValidator validator = new AggregateValidator(true, child, child2);

        when(errors.hasErrors()).thenReturn(true);

        validator.validate(context, errors);

        verify(child).validate(context, errors);
        verifyZeroInteractions(child2);
    }
    
    @Test
    public void multipleValidators() {
        PluginValidator child2 = mock(PluginValidator.class);

        AggregateValidator validator = new AggregateValidator(child, child2);

        InOrder inOrder = inOrder(child, child2);

        validator.validate(context, errors);

        inOrder.verify(child).validate(context, errors);
        inOrder.verify(child2).validate(context, errors);
    }

    @Test
    public void singleValidator() {
        AggregateValidator validator = new AggregateValidator(child);

        validator.validate(context, errors);

        verify(child).validate(context, errors);
    }

    @Before
    public void setUp() throws Exception {
        context = mock(PluginContext.class);
        errors = mock(PluginErrors.class);
        child = mock(PluginValidator.class);
    }
}
