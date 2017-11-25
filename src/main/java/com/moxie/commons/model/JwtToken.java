package com.moxie.commons.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Created by wangyanbo on 16/11/30.
 */
@Data
public class JwtToken {
    private String token;
    @JsonProperty("exp")
    private Long expireAt;
}
