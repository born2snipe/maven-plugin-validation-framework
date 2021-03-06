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


public class AggregateValidator implements PluginValidator {
    private final boolean stopOnFirstError;
    private final PluginValidator[] validators;

    public AggregateValidator(boolean stopOnFirstError, PluginValidator... validators) {
        this.stopOnFirstError = stopOnFirstError;
        this.validators = validators;
    }

    public AggregateValidator(PluginValidator... validators) {
        this(false, validators);
    }

    public void validate(PluginContext context, PluginErrors errors) {
        for (PluginValidator validator : validators) {
            validator.validate(context, errors);
            if (stopOnFirstError && errors.hasErrors()) {
                break;
            }
        }
    }
}
