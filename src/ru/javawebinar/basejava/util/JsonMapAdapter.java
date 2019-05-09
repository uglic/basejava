package ru.javawebinar.basejava.util;

import com.google.gson.*;
import ru.javawebinar.basejava.model.*;

import java.lang.reflect.Type;
import java.util.EnumMap;
import java.util.Map;

public class JsonMapAdapter<T> implements JsonDeserializer<T> {  //JsonSerializer<T>,
    @Override
    public T deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        Class<? extends Enum> enumClass = getEnumClassByJsonObject(jsonObject, ContactTypes.class, SectionTypes.class);
        if (enumClass == null) {
            return null;
        }

        T result;
        if (enumClass == ContactTypes.class) {
            result = deserializeContactTypes(jsonObject, context);
        } else if (enumClass == SectionTypes.class) {
            result = deserializeSectionTypes(jsonObject, context);
        } else {
            throw new JsonParseException("Unknown enum class: " + enumClass.getCanonicalName());
        }
        return result;

    }

    private Class<? extends Enum> getEnumClassByItemValue(String itemValue, Class<? extends Enum>... classes) {
        for (Class<? extends Enum> clazz : classes) {
            for (Enum enumValue : clazz.getEnumConstants()) {
                if (enumValue.name().equals(itemValue)
                        || enumValue.toString().equals(itemValue)) {
                    return clazz;
                }
            }
        }
        throw new JsonParseException("Unknown enum value: " + itemValue);
    }

    private Class<? extends Enum> getEnumClassByJsonObject(JsonObject json, Class<? extends Enum>... classes) {
        if (json.entrySet().size() > 0) {
            String anyEnumItem = json.entrySet().iterator().next().getKey();
            return getEnumClassByItemValue(anyEnumItem, classes);
        } else {
            return null;
        }
    }

    private T deserializeContactTypes(JsonObject json, JsonDeserializationContext context) throws JsonParseException {
        EnumMap<ContactTypes, Contact> resultMap = new EnumMap<>(ContactTypes.class);
        for (Map.Entry<String, JsonElement> entry : json.entrySet()) {
            String key = entry.getKey();
            ContactTypes enumValue = ContactTypes.valueOf(key);
            resultMap.put(enumValue, context.deserialize(entry.getValue(), Contact.class));
        }
        return (T) resultMap;
    }

    private T deserializeSectionTypes(JsonObject json, JsonDeserializationContext context) throws JsonParseException {
        EnumMap<SectionTypes, AbstractSection> resultMap = new EnumMap<>(SectionTypes.class);
        for (Map.Entry<String, JsonElement> entry : json.entrySet()) {
            String key = entry.getKey();
            SectionTypes enumValue = SectionTypes.valueOf(key);
            Class<? extends AbstractSection> sectionClass = null;
            switch (enumValue) {
                case OBJECTIVE:
                case PERSONAL:
                    sectionClass = SimpleTextSection.class;
                    break;
                case ACHIEVEMENT:
                case QUALIFICATIONS:
                    sectionClass = BulletedTextListSection.class;
                    break;
                case EXPERIENCE:
                case EDUCATION:
                    sectionClass = OrganizationSection.class;
                    break;
                default:
                    throw new JsonParseException("Unknown section: " + enumValue);
            }
            resultMap.put(enumValue, context.deserialize(entry.getValue(), sectionClass));
        }
        return (T) resultMap;
    }
}
