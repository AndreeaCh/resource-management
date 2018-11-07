package com.resource.management.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class SubscribeDetachmentsRequestJsonDeserializationTest {

    @Autowired
    private JacksonTester<SubscribeDetachmentsRequest> json;

    @Before
    public void setup() {
        ObjectMapper objectMapper = new ObjectMapper();
        JacksonTester.initFields(this, objectMapper);
    }

    @Test
    public void testDeserialize() throws Exception {
        String content = "{\"type\":\"SubscribeDetachmentsRequest\"}";
        assertThat(this.json.parse(content))
                .isEqualTo(new SubscribeDetachmentsRequest("SubscribeDetachmentsRequest"));
    }
}
