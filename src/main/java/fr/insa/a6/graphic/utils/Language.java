package fr.insa.a6.graphic.utils;

public class Language {
    private final String name;
    private final String value;

    public Language(String n, String v){
        name = n;
        value = v;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
