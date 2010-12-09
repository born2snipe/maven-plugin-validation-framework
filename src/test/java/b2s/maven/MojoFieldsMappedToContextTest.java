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

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.factory.ArtifactFactory;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.resolver.ArtifactResolver;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;


public class MojoFieldsMappedToContextTest {
    @Test
    public void test() throws MojoExecutionException, MojoFailureException {
        Log log = mock(Log.class);
        ArtifactFactory artifactFactory = mock(ArtifactFactory.class);
        ArtifactResolver artifactResolver = mock(ArtifactResolver.class);
        ArtifactRepository localRepository = mock(ArtifactRepository.class);
        List<Artifact> pluginDependencies = Arrays.asList(mock(Artifact.class));
        MavenProject mavenProject = mock(MavenProject.class);
        List<ArtifactRepository> remoteRepositories = Arrays.asList(mock(ArtifactRepository.class));

        ShuntMojo mojo = new ShuntMojo();
        mojo.setArtifactFactory(artifactFactory);
        mojo.setArtifactResolver(artifactResolver);
        mojo.setLocalRepository(localRepository);
        mojo.setLog(log);
        mojo.setPluginDependencies(pluginDependencies);
        mojo.setProject(mavenProject);
        mojo.setRemoteRepositories(remoteRepositories);

        mojo.execute();

        assertSame(artifactFactory, mojo.context.getArtifactFactory());
        assertSame(artifactResolver, mojo.context.getArtifactResolver());
        assertSame(localRepository, mojo.context.getLocalRepository());
        assertSame(log, mojo.context.getLog());
        assertSame(pluginDependencies, mojo.context.getPluginDependencies());
        assertSame(mavenProject, mojo.context.getProject());
        assertSame(remoteRepositories, mojo.context.getRemoteRepositories());
    }

    private static class ShuntMojo extends AbstractMojo {
        PluginContext context;

        @Override
        protected void executePlugin(PluginContext context) throws MojoExecutionException, MojoFailureException {
            this.context = context;
        }
    }
}
