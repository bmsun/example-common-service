package com.example.commons.model;

public enum ExcelVersionEnum {
    V2003("2003"),
    V2007("2007"),
    OTHER("");
    private String name;
     ExcelVersionEnum(String name){
         this.name=name;
     };

    public String getName() {
        return name;
    }

    //test
    public static void main(String[] args) {
        System.out.println(ExcelVersionEnum.valueOf("V2003"));//V2003
        System.out.println(ExcelVersionEnum.V2003.name());//V2003
        System.out.println(ExcelVersionEnum.V2003.getName());//"2003"
    }
}
