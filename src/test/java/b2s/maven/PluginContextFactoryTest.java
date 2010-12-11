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

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.mockito.Mockito.*;


public class PluginContextFactoryTest {
    private PluginContextFactory factory;
    private PluginContextFieldMapper defaultFieldMapper;
    private PluginContextFieldMapper matchingFieldMapper;

    @Test
    public void multipleFields() {
        PluginContext context = factory.build(new MultipleFields());

        verify(defaultFieldMapper).map(context, "field", "value");
        verify(defaultFieldMapper).map(context, "field2", "value2");
    }

    @Test
    public void singleField_toMatchingField() {
        factory.registerMatchingField("log");

        MatchingField field = new MatchingField();

        PluginContext context = factory.build(field);

        verify(matchingFieldMapper).map(context, "log", field.log);
    }

    @Test
    public void singleField() {
        PluginContext context = factory.build(new SingleField());

        verify(defaultFieldMapper).map(context, "name", "value");
    }

    @Test
    public void childHasSameFieldNameAsTheParent() {
        factory.registerMatchingField("log");

        ChildHasSameFieldName obj = new ChildHasSameFieldName();

        PluginContext context = factory.build(obj);

        verify(matchingFieldMapper).map(context, "log", obj.log);
        verifyNoMoreInteractions(matchingFieldMapper);
    }

    @Test
    public void inheritedFields() {
        PluginContext context = factory.build(new InHeritedField());

        verify(defaultFieldMapper).map(context, "name", "value");
        verify(defaultFieldMapper).map(context, "child", "childValue");
    }

    @Test
    public void noFields() {
        PluginContext context = factory.build(new NoFields());

        assertNotNull(context);
        assertEquals(0, context.size());
    }

    @Before
    public void setUp() throws Exception {
        defaultFieldMapper = mock(PluginContextFieldMapper.class);
        matchingFieldMapper = mock(PluginContextFieldMapper.class);

        factory = new PluginContextFactory();
        factory.setDefaultFieldMapper(defaultFieldMapper);
        factory.setMatchingFieldMapper(matchingFieldMapper);
    }

    private static class MultipleFields {
        private String field = "value";
        private String field2 = "value2";
    }

    private static class SingleField {
        private String name = "value";
    }

    private static class MatchingField {
        private Log log = mock(Log.class, "log");
    }

    private static class InHeritedField extends SingleField {
        private String child = "childValue";
    }

    private static class ChildHasSameFieldName extends MatchingField {
        private Log log = mock(Log.class, "logChild");
    }

    private static class NoFields {
    }
}
