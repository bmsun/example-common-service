package com.example.commons.example;

public enum EnumClassExample {
    ENUM_1("enum1"),
    ENUM_2("enum2");
   private String paramName;
    EnumClassExample(String paramName){
        this.paramName=paramName;
    }

    public String getParamName(){
        return paramName;
    }

}
