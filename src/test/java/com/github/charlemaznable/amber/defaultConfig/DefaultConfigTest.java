package com.github.charlemaznable.amber.defaultConfig;

import com.github.charlemaznable.amber.CookieValue;
import lombok.SneakyThrows;
import lombok.val;
import org.joda.time.DateTime;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockCookie;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static com.github.charlemaznable.core.codec.Base64.base64;
import static com.github.charlemaznable.core.codec.Json.json;
import static com.github.charlemaznable.core.crypto.AES.encrypt;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = DefaultConfiguration.class)
@WebAppConfiguration
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

        Thread.sleep(5000);
        val response2 = mockMvc.perform(get("/default/index")
                .cookie(verboseMockCookie, mockCookie))
                .andExpect(status().isFound())
                .andReturn().getResponse();
        assertEquals("amber-login-url?appId=default&redirectUrl=local-url%2Fdefault%2Findex", response2.getRedirectedUrl());
    }
}
