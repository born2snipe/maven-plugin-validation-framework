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
import org.apache.commons.lang.StringUtils;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.factory.ArtifactFactory;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.resolver.ArtifactResolver;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;

import java.util.List;


public abstract class AbstractMojo extends org.apache.maven.plugin.AbstractMojo {
    /**
     * @parameter default-value="${project}"
     */
    private MavenProject project;

    /**
     * @parameter default-value="${localRepository}"
     */
    private ArtifactRepository localRepository;

    /**
     * @parameter default-value="${project.remoteArtifactRepositories}"
     */
    private List<ArtifactRepository> remoteRepositories;

    /**
     * @component
     */
    private ArtifactFactory artifactFactory;

    /**
     * @component
     */
    private ArtifactResolver artifactResolver;

    /**
     * @parameter default-value="${plugin.artifacts}"
     */
    private List<Artifact> pluginDependencies;

    private PluginValidator validator;
    private PluginContextFactory pluginContextFactory = new PluginContextFactory();
    private PluginErrorsFactory pluginErrorsFactory = new PluginErrorsFactory();

    protected AbstractMojo() {
        pluginContextFactory.registerMatchingField("artifactFactory");
        pluginContextFactory.registerMatchingField("artifactResolver");
        pluginContextFactory.registerMatchingField("localRepository");
        pluginContextFactory.registerMatchingField("log");
        pluginContextFactory.registerMatchingField("pluginDependencies");
        pluginContextFactory.registerMatchingField("project");
        pluginContextFactory.registerMatchingField("remoteRepositories");
    }

    public final void execute() throws MojoExecutionException, MojoFailureException {
        PluginContext context = pluginContextFactory.build(this);
        if (validator != null) {
            PluginErrors errors = pluginErrorsFactory.build();
            validator.validate(context, errors);

            if (errors.hasErrors()) {
                String errorMessages = StringUtils.join(errors.getErrors(), "\n\t");
                throw new MojoExecutionException("There is a problem with how you configured the plugin. Below are the reason(s):\n\t" + errorMessages);
            }
        }
        executePlugin(context);
    }

    protected abstract void executePlugin(PluginContext context) throws MojoExecutionException, MojoFailureException;

    protected void setValidator(PluginValidator validator) {
        this.validator = validator;
    }

    protected void setPluginValidatorContextFactory(PluginContextFactory pluginContextFactory) {
        this.pluginContextFactory = pluginContextFactory;
    }

    protected void setPluginErrorsFactory(PluginErrorsFactory pluginErrorsFactory) {
        this.pluginErrorsFactory = pluginErrorsFactory;
    }

    void setProject(MavenProject project) {
        this.project = project;
    }

    void setLocalRepository(ArtifactRepository localRepository) {
        this.localRepository = localRepository;
    }

    void setRemoteRepositories(List<ArtifactRepository> remoteRepositories) {
        this.remoteRepositories = remoteRepositories;
    }

    void setArtifactFactory(ArtifactFactory artifactFactory) {
        this.artifactFactory = artifactFactory;
    }

    void setArtifactResolver(ArtifactResolver artifactResolver) {
        this.artifactResolver = artifactResolver;
    }

    void setPluginDependencies(List<Artifact> pluginDependencies) {
        this.pluginDependencies = pluginDependencies;
    }
}
