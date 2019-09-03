package com.github.charlemaznable.amber.spring;

import com.github.charlemaznable.amber.AmberLogin;
import com.github.charlemaznable.amber.CookieValue;
import com.github.charlemaznable.amber.config.AmberConfig;
import com.github.charlemaznable.net.Url;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import lombok.var;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Optional;

import static com.github.charlemaznable.codec.Base64.unBase64;
import static com.github.charlemaznable.codec.Json.unJson;
import static com.github.charlemaznable.crypto.AES.decrypt;
import static com.github.charlemaznable.lang.Condition.nullThen;
import static com.github.charlemaznable.lang.Str.isEmpty;
import static org.springframework.core.annotation.AnnotatedElementUtils.findMergedAnnotation;

@Slf4j
@Component
public class AmberInterceptor implements HandlerInterceptor {

    @Autowired(required = false)
    private AmberConfig amberConfig;
    private Cache<HandlerAmberLoginCacheKey, Optional<AmberLogin>>
            handlerAmberLoginCache = CacheBuilder.newBuilder().build();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) return true;
        if (null == amberConfig) return false;

        val handlerMethod = (HandlerMethod) handler;
        val cacheKey = new HandlerAmberLoginCacheKey(handlerMethod);
        val amberLogin = handlerAmberLoginCache.get(cacheKey, () -> findAmberLogin(cacheKey));

        // +-----------------------------+-------------------+--------------------+
        // |                             | forceLogin = true | forceLogin = false |
        // +-----------------------------+-------------------+--------------------+
        // |    AmberLogin not Present   |     intercept     |        pass        |
        // +-----------------------------+-------------------+--------------------+
        // |  AmberLogin required = true |     intercept     |      intercept     |
        // +-----------------------------+-------------------+--------------------+
        // | AmberLogin required = false |        pass       |        pass        |
        // +-----------------------------+-------------------+--------------------+
        if (amberLogin.isPresent() && !amberLogin.get().required()) return true;
        if (!amberLogin.isPresent() && !amberConfig.forceLogin()) return true;

        val appId = amberConfig.appId();
        val cookieName = amberConfig.cookieName();
        val encryptKey = amberConfig.encryptKey();
        val amberLoginUrl = amberConfig.amberLoginUrl();
        val localUrl = amberConfig.localUrl();

        if (null == appId || null == cookieName || null == encryptKey ||
                null == amberLoginUrl || null == localUrl) return false;

        val cookies = nullThen(request.getCookies(), () -> new Cookie[]{});
        for (val cookie : cookies) {
            if (cookie.getName().equals(cookieName)) {
                val decrypted = decrypt(unBase64(cookie.getValue()), encryptKey);
                val cookieValue = unJson(decrypted, CookieValue.class);
                if (isEmpty(cookieValue.getUsername())) break;
                if (cookieValue.getExpiredTime().isAfter(DateTime.now())) return true;
            }
        }

        var location = amberLoginUrl + "?appId=" + appId;
        location += "&redirectUrl=" + Url.encode(localUrl + request.getRequestURI());
        response.sendRedirect(location);
        return false;
    }

    private Optional<AmberLogin> findAmberLogin(HandlerAmberLoginCacheKey cacheKey) {
        val methodAmberLogin = findMergedAnnotation(cacheKey.getMethod(), AmberLogin.class);
        if (null != methodAmberLogin) return Optional.of(methodAmberLogin);

        val classAmberLogin = findMergedAnnotation(cacheKey.getDeclaringClass(), AmberLogin.class);
        if (null != classAmberLogin) return Optional.of(classAmberLogin);

        return Optional.empty();
    }

    @Getter
    @EqualsAndHashCode
    static class HandlerAmberLoginCacheKey {

        private Method method;
        private Class<?> declaringClass;

        HandlerAmberLoginCacheKey(HandlerMethod handlerMethod) {
            this.method = handlerMethod.getMethod();
            this.declaringClass = this.method.getDeclaringClass();
        }
    }
}
