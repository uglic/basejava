package ru.javawebinar.basejava.util;

public class HtmlUtil {
    public static String toEntityValue(String value){
        if(value == null || value.isEmpty()) {
            return "";
        }
        return value
                .replace("&", "&amp;")
                .replace("\"", "&quot;")
                .replace("\'", "&apos;")
                .replace("<", "&lt;")
                .replace(">", "&gt;");
    }
}
