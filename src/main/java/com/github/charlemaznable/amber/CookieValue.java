package com.github.charlemaznable.amber;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import org.joda.time.DateTime;

@Data
public class CookieValue {

    @JSONField(name = "Username")
    private String username;

    @JSONField(name = "Random")
    private String random;

    @JSONField(name = "ExpiredTime")
    private DateTime expiredTime;
}
