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
package b2s.maven;

import b2s.maven.validation.PluginErrors;
import b2s.maven.validation.PluginErrorsFactory;
import b2s.maven.validation.PluginValidator;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static junit.framework.Assert.*;
import static org.mockito.Mockito.*;


public class AbstractMojoTest {
    private PluginContext context;
    private PluginContextFactory contextFactory;
    private PluginValidator validator;
    private ShuntMojo mojo;
    private PluginErrorsFactory pluginErrorsFactory;
    private PluginErrors errors;

    @Test
    public void nullValidator() throws MojoExecutionException, MojoFailureException {
        mojo.setValidator(null);

        mojo.execute();

        assertTrue(mojo.executed);
    }
    
    @Test
    public void passesValidators() throws MojoExecutionException, MojoFailureException {
        mojo.execute();

        verify(validator).validate(context, errors);
        assertTrue(mojo.executed);
    }

    @Test
    public void hasMultipleErrors() {
        when(errors.hasErrors()).thenReturn(true);
        when(errors.getErrors()).thenReturn(Arrays.asList("error", "error2"));

        try {
            mojo.execute();
            fail();
        } catch (MojoExecutionException e) {
            assertEquals("There is a problem with how you configured the plugin. Below are the reason(s):\n\terror\n\terror2", e.getMessage());
        } catch (MojoFailureException e) {

        }

        verify(validator).validate(context, errors);
    }

    @Test
    public void hasSingleError() {
        when(errors.hasErrors()).thenReturn(true);
        when(errors.getErrors()).thenReturn(Arrays.asList("error"));

        try {
            mojo.execute();
            fail();
        } catch (MojoExecutionException e) {
            assertEquals("There is a problem with how you configured the plugin. Below are the reason(s):\n\terror", e.getMessage());
        } catch (MojoFailureException e) {

        }

        verify(validator).validate(context, errors);
        assertFalse(mojo.executed);
    }

    @Before
    public void setUp() throws Exception {
        context = mock(PluginContext.class);
        contextFactory = mock(PluginContextFactory.class);
        validator = mock(PluginValidator.class);
        pluginErrorsFactory = mock(PluginErrorsFactory.class);
        errors = mock(PluginErrors.class);

        mojo = new ShuntMojo();
        mojo.setPluginValidatorContextFactory(contextFactory);
        mojo.setValidator(validator);
        mojo.setPluginErrorsFactory(pluginErrorsFactory);

        when(contextFactory.build(mojo)).thenReturn(context);
        when(pluginErrorsFactory.build()).thenReturn(errors);
    }

    private static class ShuntMojo extends AbstractMojo {
        private boolean executed = false;

        @Override
        protected void executePlugin(PluginContext context) throws MojoExecutionException, MojoFailureException {
            executed = true;
        }
    }
}
