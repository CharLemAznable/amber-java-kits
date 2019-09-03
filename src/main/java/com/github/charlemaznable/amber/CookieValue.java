package com.github.charlemaznable.amber;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import org.joda.time.DateTime;

@Data
public class CookieValue {

    private String username;
    private String random;
    @JSONField(name = "expired-time")
    private DateTime expiredTime;
}
