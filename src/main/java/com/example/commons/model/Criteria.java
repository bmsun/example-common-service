package com.example.commons.model;

import lombok.Getter;

/**
 * Created by wangyanbo on 17/3/3.
 */
@Getter
public class Criteria {
    private String colName;
    private Type type;
    private Object value;

    protected Criteria(String colName) {
        this.colName = colName;
    }

    protected Criteria(Type type, Object value) {
        this.type = type;
        this.value = value;
    }

    public static Criteria column(String colName) {
        return new Criteria(colName);
    }

    public Criteria eq(Object value) {
        this.type = Type.EQ;
        this.value = value;
        return this;
    }

    public Criteria ne(Object value) {
        this.type = Type.NE;
        this.value = value;
        return this;
    }

    public Criteria gt(Object value) {
        this.type = Type.GT;
        this.value = value;
        return this;
    }

    public Criteria ge(Object value) {
        this.type = Type.GE;
        this.value = value;
        return this;
    }

    public Criteria lt(Object value) {
        this.type = Type.LT;
        this.value = value;
        return this;
    }

    public Criteria le(Object value) {
        this.type = Type.LE;
        this.value = value;
        return this;
    }

    public Criteria in(Object value) {
        this.type = Type.IN;
        this.value = value;
        return this;
    }

    public enum Type {
        EQ, //等于
        NE, //不等于
        GT, //大于
        GE, //大于或等于
        LT, //小于
        LE, //小于或等于
        IN, //在范围内
        NIN //不在范围内
    }
}
