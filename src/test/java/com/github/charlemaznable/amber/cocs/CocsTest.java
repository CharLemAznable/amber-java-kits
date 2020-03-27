package com.github.charlemaznable.amber.cocs;

import lombok.SneakyThrows;
import lombok.val;
import lombok.var;
import org.joda.time.DateTime;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static com.github.charlemaznable.core.lang.Str.toStr;
import static org.joda.time.DateTimeConstants.MILLIS_PER_SECOND;
import static org.joda.time.DateTimeZone.UTC;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = CocsConfiguration.class)
@WebAppConfiguration
@TestInstance(Lifecycle.PER_CLASS)
public class CocsTest {

    private static MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeAll
    public void setup() {
        mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    @SneakyThrows
    @Test
    public void testCocs() {
        var response = mockMvc.perform(get("/cocs"))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse();
        assertEquals(BAD_REQUEST.getReasonPhrase(), response.getContentAsString());

        val e = toStr(DateTime.now(UTC).plusSeconds(5).getMillis() / MILLIS_PER_SECOND);
        response = mockMvc.perform(get("/cocs")
                .param("e", e))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse();
        assertEquals(BAD_REQUEST.getReasonPhrase(), response.getContentAsString());

        response = mockMvc.perform(get("/cocs")
                .param("e", e)
                .param("cookie-name", "cookieValue"))
                .andExpect(status().isFound())
                .andReturn().getResponse();
        assertEquals("local-url", response.getRedirectedUrl());
        val cookie = response.getCookie("cookie-name");
        assertNotNull(cookie);
        assertEquals("cookieValue", cookie.getValue());
        assertTrue(cookie.getMaxAge() <= 5);
        assertEquals("/", cookie.getPath());
        assertTrue(cookie.isHttpOnly());
        assertTrue(cookie.getSecure());
    }
}
