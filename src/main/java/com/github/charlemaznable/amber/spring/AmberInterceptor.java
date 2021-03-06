package com.github.charlemaznable.amber.spring;

import com.github.charlemaznable.amber.AmberLogin;
import com.github.charlemaznable.amber.CookieValue;
import com.github.charlemaznable.amber.config.AmberConfig;
import com.github.charlemaznable.core.net.Url;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Optional;

import static com.github.charlemaznable.core.codec.Base64.unBase64;
import static com.github.charlemaznable.core.codec.Json.unJson;
import static com.github.charlemaznable.core.crypto.AES.decrypt;
import static com.github.charlemaznable.core.lang.Condition.nullThen;
import static com.github.charlemaznable.core.lang.Str.isBlank;
import static com.github.charlemaznable.core.lang.Str.isEmpty;
import static com.github.charlemaznable.core.miner.MinerFactory.getMiner;
import static org.springframework.core.annotation.AnnotatedElementUtils.findMergedAnnotation;

@Slf4j
@Component
public final class AmberInterceptor implements HandlerInterceptor {

    private final AmberConfig amberConfig;
    private Cache<HandlerAmberLoginCacheKey, Optional<AmberLogin>>
            handlerAmberLoginCache = CacheBuilder.newBuilder().build();

    @Autowired
    public AmberInterceptor(@Nullable AmberConfig amberConfig) {
        this.amberConfig = nullThen(amberConfig, () -> getMiner(AmberConfig.class));
    }

    @Override
    public boolean preHandle(@Nonnull HttpServletRequest request,
                             @Nonnull HttpServletResponse response,
                             @Nonnull Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) return true;

        val handlerMethod = (HandlerMethod) handler;
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
                (!amberLoginOptional.isPresent() && !amberConfig.forceLogin());
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
