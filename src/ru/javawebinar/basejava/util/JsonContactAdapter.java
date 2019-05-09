package ru.javawebinar.basejava.util;

import com.google.gson.*;
import ru.javawebinar.basejava.model.Contact;

import java.lang.reflect.Type;

public class JsonContactAdapter<T> implements JsonDeserializer<T> {  //JsonSerializer<T>,
    @Override
    public T deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String name = jsonObject.get("name").getAsString();
        String url = jsonObject.get("url").getAsString();
        return (T) new Contact(name, url);

    }
}
