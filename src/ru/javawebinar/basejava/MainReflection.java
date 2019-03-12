package ru.javawebinar.basejava;

import ru.javawebinar.basejava.model.Resume;

import java.lang.reflect.Field;

public class MainReflection {

    public static void main(String[] args) throws ReflectiveOperationException {
        Resume r = new Resume();
        Field field = r.getClass().getDeclaredFields()[0];
        field.setAccessible(true);
        System.out.println(field.getName());
        System.out.println(field.get(r));
        field.set(r, "new_uuid");
        System.out.println("call via reflection:");
        r.getClass().getMethod("toString").invoke(r);
        // TODO : invoke r.toString via reflection
        System.out.println(r);
    }
}