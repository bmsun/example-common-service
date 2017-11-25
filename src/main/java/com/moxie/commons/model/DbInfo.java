package com.moxie.commons.model;

import lombok.Data;

/**
 * Created by wangyanbo on 17/2/26.
 */
@Data
public class DbInfo {
    private String url;
    private String user;
    private String pass;

    public DbInfo(String url, String user, String pass) {
        this.url = url;
        this.user = user;
        this.pass = pass;
    }
}
