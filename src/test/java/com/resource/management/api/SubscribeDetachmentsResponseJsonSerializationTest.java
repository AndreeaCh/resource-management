package com.resource.management.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.resource.management.api.SubscribeDetachmentsRequest;
import com.resource.management.api.SubscribeDetachmentsResponse;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class SubscribeDetachmentsResponseJsonSerializationTest {

    @Autowired
    private JacksonTester<SubscribeDetachmentsResponse> json;

    @Before
    public void setup() {
        ObjectMapper objectMapper = new ObjectMapper();
        JacksonTester.initFields(this, objectMapper);
    }

    @Test
    public void testSerialize() throws Exception {
        SubscribeDetachmentsResponse response = new SubscribeDetachmentsResponse("CJ");
        assertThat(json.write(response)).extractingJsonPathStringValue("@.name")
                .isEqualTo("CJ");
    }
}
