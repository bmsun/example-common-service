package com.example.commons.utils.exp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultResponse {
    int httpStatusCode;
    String result;
    String httpMessage;
}
