package com.github.charlemaznable.amber.spring;

import com.github.charlemaznable.amber.AmberLogin;
import com.github.charlemaznable.amber.CookieValue;
import com.github.charlemaznable.amber.config.AmberConfig;
import com.github.charlemaznable.core.net.Url;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Method;
import java.util.Optional;

import static com.github.charlemaznable.configservice.ConfigFactory.getConfig;
import static com.github.charlemaznable.core.codec.Base64.unBase64;
import static com.github.charlemaznable.core.codec.Json.unJson;
import static com.github.charlemaznable.core.crypto.AES.decrypt;
import static com.github.charlemaznable.core.lang.Condition.nullThen;
import static com.github.charlemaznable.core.lang.Str.isBlank;
import static com.github.charlemaznable.core.lang.Str.isEmpty;
import static org.springframework.core.annotation.AnnotatedElementUtils.getMergedAnnotation;

@Slf4j
@Component
public final class AmberInterceptor implements HandlerInterceptor {

    private final AmberConfig amberConfig;
    private final Cache<HandlerAmberLoginCacheKey, Optional<AmberLogin>>
            handlerAmberLoginCache = CacheBuilder.newBuilder().build();

    @Autowired
    public AmberInterceptor(@Nullable AmberConfig amberConfig) {
        this.amberConfig = nullThen(amberConfig, () -> getConfig(AmberConfig.class));
    }

    @Override
    public boolean preHandle(@Nonnull HttpServletRequest request,
                             @Nonnull HttpServletResponse response,
                             @Nonnull Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod handlerMethod)) return true;

        val cacheKey = new HandlerAmberLoginCacheKey(handlerMethod);
        val amberLoginOptional = handlerAmberLoginCache.get(
                cacheKey, () -> findAmberLogin(cacheKey));
        if (dontIntercept(amberLoginOptional)) return true;

        val appId = amberConfig.appId();
        val cookieName = amberConfig.cookieName();
        val encryptKey = amberConfig.encryptKey();
        val amberLoginUrl = amberConfig.amberLoginUrl();
        val localUrl = amberConfig.localUrl();

        if (isBlank(appId) || isBlank(cookieName) || isBlank(encryptKey) ||
                isBlank(amberLoginUrl) || isBlank(localUrl)) return false;

        val cookies = nullThen(request.getCookies(), () -> new Cookie[]{});
        for (val cookie : cookies) {
            if (cookie.getName().equals(cookieName)) {
                val decrypted = decrypt(unBase64(cookie.getValue()), encryptKey);
                val cookieValue = unJson(decrypted, CookieValue.class);
                if (cookieValue.getExpiredTime().isBeforeNow() ||
                        isEmpty(cookieValue.getUsername())) break;
                return true;
            }
        }

        response.sendRedirect(amberLoginUrl + "?appId=" + appId
                + "&redirectUrl=" + Url.encode(localUrl + request.getRequestURI()));
        return false;
    }

    private Optional<AmberLogin> findAmberLogin(HandlerAmberLoginCacheKey cacheKey) {
        val methodAmberLogin = getMergedAnnotation(cacheKey.getMethod(), AmberLogin.class);
        if (null != methodAmberLogin) return Optional.of(methodAmberLogin);

        val classAmberLogin = getMergedAnnotation(cacheKey.getDeclaringClass(), AmberLogin.class);
        if (null != classAmberLogin) return Optional.of(classAmberLogin);

        return Optional.empty();
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private boolean dontIntercept(Optional<AmberLogin> amberLoginOptional) {
        // +-----------------------------+-------------------+--------------------+
        // |                             | forceLogin = true | forceLogin = false |
        // +-----------------------------+-------------------+--------------------+
        // |    AmberLogin not Present   |     intercept     |        pass        |
        // +-----------------------------+-------------------+--------------------+
        // |  AmberLogin required = true |     intercept     |      intercept     |
        // +-----------------------------+-------------------+--------------------+
        // | AmberLogin required = false |        pass       |        pass        |
        // +-----------------------------+-------------------+--------------------+
        return (amberLoginOptional.isPresent() && !amberLoginOptional.get().required()) ||
                (amberLoginOptional.isEmpty() && !amberConfig.forceLogin());
    }

    @Getter
    @EqualsAndHashCode
    static class HandlerAmberLoginCacheKey {

        private final Method method;
        private final Class<?> declaringClass;

        HandlerAmberLoginCacheKey(HandlerMethod handlerMethod) {
            this.method = handlerMethod.getMethod();
            this.declaringClass = this.method.getDeclaringClass();
        }
    }
}
