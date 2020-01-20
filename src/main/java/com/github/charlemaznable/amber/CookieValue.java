package com.github.charlemaznable.amber;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;
import org.joda.time.DateTime;

@Getter
@Setter
public final class CookieValue {

    @JSONField(name = "Username")
    private String username;

    @JSONField(name = "Random")
    private String random;

    @JSONField(name = "ExpiredTime")
    private DateTime expiredTime;
}
