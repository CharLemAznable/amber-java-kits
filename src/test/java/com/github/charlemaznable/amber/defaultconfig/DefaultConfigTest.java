package com.github.charlemaznable.amber.defaultconfig;

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

@SpringJUnitWebConfig(DefaultConfiguration.class)
@TestInstance(Lifecycle.PER_CLASS)
public class DefaultConfigTest {

    private static MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeAll
    public void setup() {
        mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    @SneakyThrows
    @Test
    public void testDefaultConfig() {
        val cookieValue = new CookieValue();
        cookieValue.setUsername("a");
        cookieValue.setRandom("b");
        cookieValue.setExpiredTime(DateTime.now().plusSeconds(3));
        val jsonString = json(cookieValue);
        val mockCookie = new MockCookie("cookie-name", base64(encrypt(jsonString, "A916EFFC3121F935")));

        val verboseCookieValue = new CookieValue();
        verboseCookieValue.setUsername("a");
        verboseCookieValue.setRandom("b");
        verboseCookieValue.setExpiredTime(DateTime.now().plusSeconds(3));
        val verboseJsonString = json(verboseCookieValue);
        val verboseMockCookie = new MockCookie("verbose-cookie-name", base64(encrypt(verboseJsonString, "A916EFFC3121F935")));

        val response = mockMvc.perform(get("/default/index")
                .cookie(verboseMockCookie, mockCookie))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        assertNull(response.getRedirectedUrl());

        assertDoesNotThrow(() ->
                await().pollDelay(Duration.ofMillis(5000)).until(() -> {
                    val response2 = mockMvc.perform(get("/default/index")
                            .cookie(verboseMockCookie, mockCookie))
                            .andExpect(status().isFound())
                            .andReturn().getResponse();
                    return "amber-login-url?appId=default&redirectUrl=local-url%2Fdefault%2Findex"
                            .equals(response2.getRedirectedUrl());
                }));
    }
}
