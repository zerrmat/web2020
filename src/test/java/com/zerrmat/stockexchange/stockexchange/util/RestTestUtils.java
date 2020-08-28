package com.zerrmat.stockexchange.stockexchange.util;

import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.Charset;

public class RestTestUtils {
    public static String generateResponseBody(String jsonName) {
        String requestBody = "";
        try {
            requestBody = StreamUtils.copyToString(
                    new ClassPathResource(jsonName).getInputStream(),
                    Charset.defaultCharset());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return requestBody;
    }
}
