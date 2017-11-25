package com.moxie.commons.model;

import lombok.Data;

import java.util.Date;

/**
 * Created by wangyanbo on 17/3/4.
 */
@Data
public class UserEntity {
    private String id;
    private String name;
    private Integer age;
    private Date lastModifyTime;
}
