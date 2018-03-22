package com.example.commons.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Created by zhanghesheng
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {
    private String id;
    private String name;
    private Integer age;
    private Date lastModifyTime;
}
