package com.github.charlemaznable.amber.anno;

import com.github.charlemaznable.amber.CookieValue;
import lombok.SneakyThrows;
import lombok.val;
import org.joda.time.DateTime;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockCookie;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.time.Duration;

import static com.github.charlemaznable.core.codec.Base64.base64;
import static com.github.charlemaznable.core.codec.Json.json;
import static com.github.charlemaznable.core.crypto.AES.encrypt;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@SpringJUnitWebConfig(AnnoConfiguration.class)
@TestInstance(Lifecycle.PER_CLASS)
public class AnnoConfigTest {

    private static MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeAll
    public void setup() {
        mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    @SneakyThrows
    @Test
    public void testAnnoIndex() {
        val cookieValue = new CookieValue();
        cookieValue.setUsername("a");
        cookieValue.setRandom("b");
        cookieValue.setExpiredTime(DateTime.now().plusSeconds(3));
        val jsonString = json(cookieValue);
        val mockCookie = new MockCookie("cookie-name", base64(encrypt(jsonString, "A916EFFC3121F935")));

        val response = mockMvc.perform(get("/anno/index")
                .cookie(mockCookie))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        assertNull(response.getRedirectedUrl());

        assertDoesNotThrow(() ->
                await().pollDelay(Duration.ofMillis(5000)).until(() -> {
                    val response2 = mockMvc.perform(get("/anno/index")
                            .cookie(mockCookie))
                            .andExpect(status().isFound())
                            .andReturn().getResponse();
                    return "amber-login-url?appId=anno&redirectUrl=local-url%2Fanno%2Findex"
                            .equals(response2.getRedirectedUrl());
                }));
    }

    @SuppressWarnings("Duplicates")
    @SneakyThrows
    @Test
    public void testAnnoExclude() {
        val response = mockMvc.perform(get("/anno/exclude"))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        assertNull(response.getRedirectedUrl());

        assertDoesNotThrow(() ->
                await().pollDelay(Duration.ofMillis(5000)).until(() -> {
                    val response2 = mockMvc.perform(get("/anno/exclude"))
                            .andExpect(status().isOk())
                            .andReturn().getResponse();
                    return null == response.getRedirectedUrl();
                }));
    }
}
