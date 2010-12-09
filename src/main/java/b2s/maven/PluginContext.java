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
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class PluginContext {
    private Map<String, Object> values = new LinkedHashMap<String, Object>();
    private Log log;
    private MavenProject project;
    private ArtifactRepository localRepository;
    private List<ArtifactRepository> remoteRepositories;
    private ArtifactFactory artifactFactory;
    private ArtifactResolver artifactResolver;
    private List<Artifact> pluginDependencies;

    public <T> T get(String name, Class<T> clazz) {
        return (T) values.get(name);
    }

    public Object get(String name) {
        return values.get(name);
    }

    public void put(String name, Object value) {
        values.put(name, value);
    }

    public void putAll(Map<String, Object> values) {
        this.values.putAll(values);
    }

    public int size() {
        return values.size();
    }

    public Log getLog() {
        return log;
    }

    public MavenProject getProject() {
        return project;
    }

    public ArtifactRepository getLocalRepository() {
        return localRepository;
    }

    public List<ArtifactRepository> getRemoteRepositories() {
        return remoteRepositories;
    }

    public ArtifactFactory getArtifactFactory() {
        return artifactFactory;
    }

    public ArtifactResolver getArtifactResolver() {
        return artifactResolver;
    }

    public List<Artifact> getPluginDependencies() {
        return pluginDependencies;
    }
}
