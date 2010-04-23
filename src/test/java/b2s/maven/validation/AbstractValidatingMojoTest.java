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

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static junit.framework.Assert.*;
import static org.mockito.Mockito.*;


public class AbstractValidatingMojoTest {
    private PluginValidatorContext context;
    private PluginValidatorContextFactory contextFactory;
    private PluginValidator validator;
    private ShuntMojo mojo;

    @Test
    public void nullValidator() throws MojoExecutionException, MojoFailureException {
        mojo.setValidator(null);

        mojo.execute();

        assertTrue(mojo.executed);
    }
    
    @Test
    public void passesValidators() throws MojoExecutionException, MojoFailureException {
        mojo.execute();

        verify(validator).validate(context);
        assertTrue(mojo.executed);
    }

    @Test
    public void hasMultipleErrors() {
        when(context.hasErrors()).thenReturn(true);
        when(context.getErrors()).thenReturn(Arrays.asList("error", "error2"));

        try {
            mojo.execute();
            fail();
        } catch (MojoExecutionException e) {
            assertEquals("There is a problem with how you configured the plugin. Below are the reason(s):\n\terror\n\terror2", e.getMessage());
        } catch (MojoFailureException e) {

        }

        verify(validator).validate(context);
    }

    @Test
    public void hasSingleError() {
        when(context.hasErrors()).thenReturn(true);
        when(context.getErrors()).thenReturn(Arrays.asList("error"));

        try {
            mojo.execute();
            fail();
        } catch (MojoExecutionException e) {
            assertEquals("There is a problem with how you configured the plugin. Below are the reason(s):\n\terror", e.getMessage());
        } catch (MojoFailureException e) {

        }

        verify(validator).validate(context);
        assertFalse(mojo.executed);
    }

    @Before
    public void setUp() throws Exception {
        context = mock(PluginValidatorContext.class);
        contextFactory = mock(PluginValidatorContextFactory.class);
        validator = mock(PluginValidator.class);

        mojo = new ShuntMojo();
        mojo.setPluginValidatorContextFactory(contextFactory);
        mojo.setValidator(validator);

        when(contextFactory.build(mojo)).thenReturn(context);
    }

    private static class ShuntMojo extends AbstractValidatingMojo {
        private boolean executed = false;

        @Override
        protected void executePlugin() throws MojoExecutionException, MojoFailureException {
            executed = true;
        }
    }
}
