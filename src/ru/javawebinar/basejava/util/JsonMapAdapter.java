package ru.javawebinar.basejava.util;

import com.google.gson.*;
import ru.javawebinar.basejava.model.*;

import java.lang.reflect.Type;
import java.util.EnumMap;
import java.util.Map;

public class JsonMapAdapter<T> implements JsonDeserializer<T> {
    @Override
    public T deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        Class<? extends Enum> enumClass = getEnumClassByJsonObject(jsonObject, ContactTypes.class, SectionTypes.class);
        if (enumClass == null) {
            return null;
        }
        if (enumClass == ContactTypes.class) {
            return deserializeContacts(jsonObject, context);
        } else if (enumClass == SectionTypes.class) {
            return deserializeSections(jsonObject, context);
        } else {
            throw new JsonParseException("Unknown enum class: " + enumClass.getCanonicalName());
        }
    }

    private T deserializeContacts(JsonObject json, JsonDeserializationContext context) throws JsonParseException {
        EnumMap<ContactTypes, Contact> resultMap = new EnumMap<>(ContactTypes.class);
        for (Map.Entry<String, JsonElement> entry : json.entrySet()) {
            String key = entry.getKey();
            ContactTypes enumValue = ContactTypes.valueOf(key);
            resultMap.put(enumValue, context.deserialize(entry.getValue(), Contact.class));
        }
        return (T) resultMap;
    }


    private T deserializeSections(JsonObject json, JsonDeserializationContext context) throws JsonParseException {
        EnumMap<SectionTypes, AbstractSection> resultMap = new EnumMap<>(SectionTypes.class);
        for (Map.Entry<String, JsonElement> entry : json.entrySet()) {
            String key = entry.getKey();
            SectionTypes sectionType = SectionTypes.valueOf(key);
            Class<? extends AbstractSection> sectionClass;
            switch (sectionType) {
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
                    throw new JsonParseException("Unknown section type: " + sectionType);
            }
            resultMap.put(sectionType, context.deserialize(entry.getValue(), sectionClass));
        }
        return (T) resultMap;
    }

    @SafeVarargs
    final private Class<? extends Enum> getEnumClassByJsonObject(JsonObject json, Class<? extends Enum>... classes) {
        if (json.entrySet().size() > 0) {
            String string = json.entrySet().iterator().next().getKey();
            for (Class<? extends Enum> clazz : classes) {
                for (Enum enumValue : clazz.getEnumConstants()) {
                    if (enumValue.name().equals(string)
                            || enumValue.toString().equals(string)) {
                        return clazz;
                    }
                }
            }
            throw new JsonParseException("Unknown enum value: " + string);
        } else {
            return null;
        }
    }
}
