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

import org.apache.maven.plugin.logging.Log;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertSame;
import static org.mockito.Mockito.mock;


public class MatchingFieldMapperTest {
    private PluginContext context;
    private MatchingFieldMapper mapper;

    @Test(expected = IllegalArgumentException.class)
    public void fieldNameDoesNotMatch() {
        Log log = mock(Log.class);

        mapper.map(context, "doesNotExist", log);
    }

    @Test
    public void fieldNameMatches() {
        Log log = mock(Log.class);

        mapper.map(context, "log", log);

        assertSame(log, context.getLog());
    }

    @Before
    public void setUp() throws Exception {
        context = new PluginContext();
        mapper = new MatchingFieldMapper();
    }
}
