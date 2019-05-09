package ru.javawebinar.basejava.util;

import com.google.gson.*;
import ru.javawebinar.basejava.model.SimpleTextSection;

import java.lang.reflect.Type;

public class JsonSectionAdapter<T> implements JsonDeserializer<T> {  //JsonSerializer<T>,
    private static final String CLASSNAME = "CLASSNAME";
    private static final String INSTANCE = "INSTANCE";

    @Override
    public T deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
//        JsonObject jsonObject = json.getAsJsonObject();
//        JsonPrimitive primitive = (JsonPrimitive) jsonObject.get(CLASSNAME);
//        String className = primitive.getAsString();
//
//        try {
//            Class clazz = Class.forName(className);
//            return context.deserialize(jsonObject.get(INSTANCE), clazz);
//        } catch (ClassNotFoundException e) {
//            throw new JsonParseException(e.getMessage());
//        }

//        JsonObject jsonObject = json.getAsJsonObject();
//        String name = jsonObject.get("name").getAsString();
//        String url = jsonObject.get("url").getAsString();
        return (T) new SimpleTextSection("-");

    }

//    @Override
//    public JsonElement serialize(T contact, Type type, JsonSerializationContext context) {
//        JsonObject retValue = new JsonObject();
//        retValue.addProperty(CLASSNAME, contact.getClass().getName());
//        JsonElement element = context.serialize(contact);
//        retValue.add(INSTANCE, element);
//        return retValue;
//    }
}
