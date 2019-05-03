package ru.javawebinar.basejava.model;

public abstract class Section {
    abstract void appendToHtmlStringBuilder(StringBuilder builder);

    @Override
    public String toString(){
        StringBuilder builder = new StringBuilder();
        appendToHtmlStringBuilder(builder);
        return builder.toString();
    }
}