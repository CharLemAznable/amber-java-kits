package com.github.charlemaznable.amber.spring;

import com.github.charlemaznable.amber.config.AmberConfig;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.github.charlemaznable.configservice.ConfigFactory.getConfig;
import static com.github.charlemaznable.core.lang.Condition.blankThen;
import static com.github.charlemaznable.core.lang.Condition.nullThen;
import static com.github.charlemaznable.core.lang.Mapp.getLong;
import static com.github.charlemaznable.core.lang.Mapp.getStr;
import static com.github.charlemaznable.core.lang.Str.isBlank;
import static com.github.charlemaznable.core.net.Http.errorHttpStatus;
import static com.github.charlemaznable.core.net.Http.fetchParameterMap;
import static java.util.Objects.isNull;
import static org.joda.time.DateTimeConstants.MILLIS_PER_SECOND;
import static org.joda.time.DateTimeZone.UTC;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Slf4j
@Component
public class AmberCocsHandler {

    private final AmberConfig amberConfig;

    @Autowired
    public AmberCocsHandler(@Nullable AmberConfig amberConfig) {
        this.amberConfig = nullThen(amberConfig, () -> getConfig(AmberConfig.class));
    }

    @SneakyThrows
    public void handle(@Nonnull HttpServletRequest request,
                       @Nonnull HttpServletResponse response) {
        val appId = amberConfig.appId();
        val cookieName = amberConfig.cookieName();
        val encryptKey = amberConfig.encryptKey();
        val amberLoginUrl = amberConfig.amberLoginUrl();
        val localUrl = amberConfig.localUrl();
        if (isBlank(appId) || isBlank(cookieName) || isBlank(encryptKey) ||
                isBlank(amberLoginUrl) || isBlank(localUrl)) {
            errorHttpStatus(response, INTERNAL_SERVER_ERROR);
            return;
        }

        val parameterMap = fetchParameterMap(request);
        val redirect = blankThen(getStr(parameterMap, "redirect"), () -> localUrl);
        val expires = getLong(parameterMap, "e"); // Unix time in seconds (UTC)
        if (isNull(expires)) {
            errorHttpStatus(response, BAD_REQUEST);
            return;
        }
        val cookieValue = getStr(parameterMap, cookieName);
        if (isBlank(cookieValue)) {
            errorHttpStatus(response, BAD_REQUEST);
            return;
        }

        val cookie = new Cookie(cookieName, cookieValue);
        val utcNow = DateTime.now(UTC);
        val utcExpires = new DateTime(expires * MILLIS_PER_SECOND, UTC);
        cookie.setMaxAge((int) new Duration(utcNow, utcExpires).getStandardSeconds());
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
        response.sendRedirect(redirect);
    }
}
