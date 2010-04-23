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

import org.apache.commons.lang.StringUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;


public abstract class AbstractValidatingMojo extends AbstractMojo {
    private PluginValidator validator;
    private PluginValidatorContextFactory pluginValidatorContextFactory = new PluginValidatorContextFactory();

    public final void execute() throws MojoExecutionException, MojoFailureException {
        if (validator != null) {
            PluginValidatorContext context = pluginValidatorContextFactory.build(this);
            validator.validate(context);

            if (context.hasErrors()) {
                String errors = StringUtils.join(context.getErrors(), "\n\t");
                throw new MojoExecutionException("There is a problem with how you configured the plugin. Below are the reason(s):\n\t" + errors);
            }
        }

        executePlugin();
    }

    protected abstract void executePlugin() throws MojoExecutionException, MojoFailureException;

    protected void setValidator(PluginValidator validator) {
        this.validator = validator;
    }

    protected void setPluginValidatorContextFactory(PluginValidatorContextFactory pluginValidatorContextFactory) {
        this.pluginValidatorContextFactory = pluginValidatorContextFactory;
    }
}
