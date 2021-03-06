package com.github.charlemaznable.amber;

import lombok.val;
import org.junit.jupiter.api.Test;

import static com.github.charlemaznable.core.codec.Json.unJson;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CookieValueTest {

    @Test
    public void testCookieValue() {
        val jsonString = "{\"Username\":\"a\",\"Random\":\"b\",\"ExpiredTime\":\"2019-03-05T23:33:59.029596352+08:00\"}";
        val cookieValue = unJson(jsonString, CookieValue.class);

        assertEquals("a", cookieValue.getUsername());
        assertEquals("b", cookieValue.getRandom());
        val expired = cookieValue.getExpiredTime();
        assertEquals(2019, expired.getYear());
        assertEquals(3, expired.getMonthOfYear());
        assertEquals(5, expired.getDayOfMonth());
        assertEquals(23, expired.getHourOfDay());
        assertEquals(33, expired.getMinuteOfHour());
        assertEquals(59, expired.getSecondOfMinute());
    }
}
