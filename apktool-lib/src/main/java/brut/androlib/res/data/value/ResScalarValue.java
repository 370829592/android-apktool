/**
 *  Copyright 2011 Ryszard Wiśniewski <brut.alll@gmail.com>
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package brut.androlib.res.data.value;

import brut.androlib.res.xml.ResValuesXmlSerializable;
import brut.androlib.res.xml.ResXmlEncodable;
import brut.androlib.AndrolibException;
import brut.androlib.res.data.ResResource;
import java.io.IOException;
import org.xmlpull.v1.XmlSerializer;

/**
 * @author Ryszard Wiśniewski <brut.alll@gmail.com>
 */
public abstract class ResScalarValue extends ResValue
        implements ResXmlEncodable, ResValuesXmlSerializable {
    protected final String mType;
    protected final String mRawValue;

    protected ResScalarValue(String type, String rawValue) {
        mType = type;
        mRawValue = rawValue;
    }

    public String encodeAsResXmlAttr() throws AndrolibException {
        if (mRawValue != null) {
            return mRawValue;
        }
        return encodeAsResXml();
    }

    public String encodeAsResXmlItemValue() throws AndrolibException {
        return encodeAsResXmlValue();
    }

    public String encodeAsResXmlValue() throws AndrolibException {
        if (mRawValue != null) {
            return mRawValue;
        }
        return encodeAsResXml();
    }

    public void serializeToResValuesXml(XmlSerializer serializer, ResResource res)
            throws IOException, AndrolibException {
        String type = res.getResSpec().getType().getName();
        boolean item = ! "reference".equals(mType) && ! type.equals(mType);
        
        String body = encodeAsResXmlValue();
        
        
        /* check for resource reference */
        if (body.contains("@")){
           // item = true; 
           // messes up strings with @, need to check if strings.xml ignore
        }
        
        /* check for using attrib as node or item */
        String tagName = item ? "item" : type;
        
        serializer.startTag(null, tagName);
        if (item) {
            serializer.attribute(null, "type", type);
        }
        serializer.attribute(null, "name", res.getResSpec().getName());

        serializeExtraXmlAttrs(serializer, res);

        if (! body.isEmpty()) {
            serializer.ignorableWhitespace(body);
        }

        serializer.endTag(null, tagName);
    }

    public String getType() {
        return mType;
    }

    protected void serializeExtraXmlAttrs(XmlSerializer serializer,
            ResResource res) throws IOException {
    }

    protected abstract String encodeAsResXml() throws AndrolibException;
}
