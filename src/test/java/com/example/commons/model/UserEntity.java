package com.example.commons.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Created by zhanghesheng
 *    内部类方式赋值：Builder。
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {
    private String id;
    private String name;
    private Integer age;
    private Date lastModifyTime;

    private UserEntity(Builder builder) {
        setId(builder.id);
        setName(builder.name);
        setAge(builder.age);
        setLastModifyTime(builder.lastModifyTime);
    }

    public static final class Builder {
        private String id;
        private String name;
        private Integer age;
        private Date lastModifyTime;

        public Builder() {
        }

        public Builder id(String val) {
            id = val;
            return this;
        }

        public Builder name(String val) {
            name = val;
            return this;
        }

        public Builder age(Integer val) {
            age = val;
            return this;
        }

        public Builder lastModifyTime(Date val) {
            lastModifyTime = val;
            return this;
        }

        public UserEntity build() {
            return new UserEntity(this);
        }
    }
}
