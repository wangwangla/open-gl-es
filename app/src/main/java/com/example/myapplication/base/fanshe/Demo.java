package com.example.myapplication.base.fanshe;

import java.lang.reflect.Field;

public class Demo {
    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
        People people = new People();
        Field name = people.getClass().getField("name");
        name.set(Object.class,"1");
    }
}
