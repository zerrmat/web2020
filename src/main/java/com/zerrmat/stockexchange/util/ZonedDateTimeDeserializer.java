package com.zerrmat.stockexchange.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

public class ZonedDateTimeDeserializer extends JsonDeserializer<ZonedDateTime> {

    @Override
    public ZonedDateTime deserialize(JsonParser jsonParser,
                                     DeserializationContext deserializationContext) throws IOException {
        String dateString = jsonParser.getText();
        String pattern = "yyyy-MM-dd'T'HH:mm:ssZ";
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
        TemporalAccessor parse = dateTimeFormatter.parse(dateString);

        return ZonedDateTime.from(parse);
    }
}