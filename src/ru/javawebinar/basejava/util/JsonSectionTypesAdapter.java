package ru.javawebinar.basejava.util;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import ru.javawebinar.basejava.model.SectionTypes;

import java.lang.reflect.Type;

public class JsonSectionTypesAdapter<T> implements JsonDeserializer<T> { //JsonSerializer<T>,
    private static final String CLASSNAME = "CLASSNAME";
    private static final String INSTANCE = "INSTANCE";

    @Override
    public T deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
        String name = json.getAsString();
        SectionTypes sectionType = null;
        for (SectionTypes t : SectionTypes.values()) {
            if (t.toString().equals(name)) {
                sectionType = t;
            }
        }
        return (T) sectionType;

    }
//
//    @Override
//    public JsonElement serialize(T contactType, Type type, JsonSerializationContext context) {
//        JsonObject retValue = new JsonObject();
//        retValue.addProperty(((ContactTypes)contactType).name(), "");
//        return retValue;
//    }
}
