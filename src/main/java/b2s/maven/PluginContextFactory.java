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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class PluginContextFactory {
    private PluginContextFieldMapper defaultFieldMapper = new PutFieldInMapMapper();
    private PluginContextFieldMapper matchingFieldMapper = new MatchingFieldMapper();
    private List<String> matchingFieldNames = new ArrayList<String>();

    public PluginContext build(Object object) {
        PluginContext context = new PluginContext();

        List<Field> fields = findAllFieldsFor(object.getClass());
        for (Field field : fields) {
            try {
                field.setAccessible(true);

                Object value = field.get(object);
                String fieldName = field.getName();

                if (matchingFieldNames.contains(fieldName)) {
                    matchingFieldMapper.map(context, fieldName, value);
                } else {
                    defaultFieldMapper.map(context, fieldName, value);
                }
            } catch (IllegalAccessException e) {

            }
        }

        return context;
    }

    private List<Field> findAllFieldsFor(Class clazz) {
        List<Field> fields = new ArrayList<Field>();
        fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
        if (clazz.getSuperclass() != null) {
            fields.addAll(findAllFieldsFor(clazz.getSuperclass()));
        }
        return fields;
    }

    void setDefaultFieldMapper(PluginContextFieldMapper defaultFieldMapper) {
        this.defaultFieldMapper = defaultFieldMapper;
    }

    void setMatchingFieldMapper(PluginContextFieldMapper matchingFieldMapper) {
        this.matchingFieldMapper = matchingFieldMapper;
    }

    public void registerMatchingField(String fieldName) {
        matchingFieldNames.add(fieldName);
    }
}
