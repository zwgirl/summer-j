package org.bark.remoting;


public class AbstractMapDeserializer implements Deserializer {

    public Object readObject(javax.json.JsonObject jsonObj, Object[] handlers, Object obj) throws Exception {
        return obj;
    }
}
