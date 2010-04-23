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

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;


public class PluginValidatorContextFactoryTest {
    private PluginValidatorContextFactory factory;

    @Test
    public void multipleFields() {
        PluginValidatorContext context = factory.build(new MultipleFields());

        assertEquals(2, context.size());
        assertEquals("value", context.get("field"));
        assertEquals("value2", context.get("field2"));
    }

    @Test
    public void singleField() {
        PluginValidatorContext context = factory.build(new SingleField());

        assertEquals(1, context.size());
        assertEquals("value", context.get("name"));
    }

    @Test
    public void noFields() {
        PluginValidatorContext context = factory.build(new NoFields());

        assertNotNull(context);
        assertEquals(0, context.size());
    }

    @Before
    public void setUp() throws Exception {
        factory = new PluginValidatorContextFactory();
    }

    private static class MultipleFields {
        private String field = "value";
        private String field2 = "value2";
    }

    private static class SingleField {
        private String name = "value";
    }

    private static class NoFields {
    }
}
