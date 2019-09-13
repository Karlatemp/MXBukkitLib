/*
 * Copyright 2019 Karlatemp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.mcres.gyhhy.MXLib.printer;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

/**
 *
 * @author Karlatemp
 */
public class RawPrinter implements DataPrinter {

    public static final RawPrinter instance = new RawPrinter();

    @Override
    public void print(Object object, CharSequence prefix, int count, Appendable append) throws IOException {
        if (prefix != null) {
            int c = count;
            while (c-- > 0) {
                append.append(prefix);
            }
        }
        if (object == null) {
            append.append("null");
        } else if (object instanceof Collection) {
            append.append("Collection: ").append(object.getClass().getName()).append('\n');
            int c = count + 1;
            for (Object element : (Collection) object) {
                print(element, prefix, c, append);
            }
        } else if (object instanceof Map) {
            Map<?, ?> mmp = (Map) object;
            append.append("Map: ").append(mmp.getClass().getName());
            for (Map.Entry<?, ?> entry : mmp.entrySet()) {
                append.append('\n');
                print(entry.getKey(), prefix, count + 1, append);
                append.append(":\n");
                print(entry.getValue(), prefix, count + 2, append);
            }
        } else if (object instanceof Enum) {
            append.append("Enum@").append(object.getClass().getName()).append('$').append(((Enum) object).name());
        } else {
            append.append(String.valueOf(object));
        }
    }

}
