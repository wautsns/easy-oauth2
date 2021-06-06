/*
 * Copyright 2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.wautsns.easy.oauth2.extension.request.apache.httpclient;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import com.github.wautsns.easy.oauth2.core.request.executor.AbstractOAuth2RequestExecutor;
import com.github.wautsns.easy.oauth2.core.request.executor.OAuth2RequestExecutorFactoryManager;
import com.github.wautsns.easy.oauth2.core.request.model.basic.OAuth2Headers;
import com.github.wautsns.easy.oauth2.core.request.model.basic.OAuth2URL;
import com.github.wautsns.easy.oauth2.core.request.model.request.OAuth2Request;
import com.github.wautsns.easy.oauth2.core.request.model.request.OAuth2RequestMethod;
import com.github.wautsns.easy.oauth2.core.request.model.request.builtin.entity.OAuth2RequestURLEncodedFormEntity;
import com.github.wautsns.easy.oauth2.core.request.model.response.AbstractOAuth2Response;

import com.fasterxml.jackson.databind.JsonNode;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

/**
 * Test {@link ApacheHttpclientOAuth2RequestExecutor}.
 *
 * @author wautsns
 * @see <a href="http://www.httpbin.org/">httpbin</a>
 * @since Mar 31, 2021
 */
public class ApacheHttpclientOAuth2RequestExecutorTest {

    /** Logger. */
    private static final @NotNull Logger log =
            LoggerFactory.getLogger(ApacheHttpclientOAuth2RequestExecutorTest.class);

    /** Request executor. */
    private static final @NotNull AbstractOAuth2RequestExecutor<?> REQUEST_EXECUTOR =
            OAuth2RequestExecutorFactoryManager.factory().create();

    // ##################################################################################

    @Test
    public void testHeaderRelatedMethods() throws Exception {
        // Initialize request.
        OAuth2URL url = new OAuth2URL("http://www.httpbin.org/anything", 0);
        OAuth2Request<?> request = new OAuth2Request<>(OAuth2RequestMethod.GET, url);
        request.headers(
                new OAuth2Headers(2)
                        .unique("Unique-Key", "value")
                        .repeatable("Repeatable-Key", Arrays.asList("value1", "value2"))
        );

        // Execute request and assert.
        AbstractOAuth2Response response = REQUEST_EXECUTOR.execute(request);
        assertEquals(200, response.status());
        assertFalse(response.headers("Content-Type").isEmpty());
        assertEquals("application/json", response.firstHeader("Content-Type"));
        assertEquals("application/json", response.lastHeader("Content-Type"));
        JsonNode body = response.readJSONBodyInputStreamAsTree();
        log.info("response: { status: {}, body: {} }", response.status(), body);
        assertNotNull(body);
        JsonNode headers = body.required("headers");
        assertEquals("value", headers.required("Unique-Key").asText());
        assertEquals("value1,value2", headers.required("Repeatable-Key").asText());
    }

    @Test
    public void testRequestGetRelatedMethods() throws Exception {
        // Initialize request.
        OAuth2URL url = new OAuth2URL("http://www.httpbin.org/anything", 2);
        OAuth2Request<?> request = new OAuth2Request<>(OAuth2RequestMethod.GET, url);
        request.url().query()
                .unique("uniqueKey", "value")
                .repeatable("repeatableKey", Arrays.asList("value1", "value2"));

        // Execute request and assert.
        AbstractOAuth2Response response = REQUEST_EXECUTOR.execute(request);
        assertEquals(200, response.status());
        JsonNode body = response.readJSONBodyInputStreamAsTree();
        log.info("response: { status: {}, body: {} }", response.status(), body);
        assertNotNull(body);
        JsonNode args = body.required("args");
        assertEquals("value", args.required("uniqueKey").asText());
        assertEquals(2, args.required("repeatableKey").size());
        assertNotNull(body.path("headers").required("User-Agent").asText());
    }

    @Test
    public void testRequestPostRelatedMethods() throws Exception {
        // Initialize request.
        OAuth2URL url = new OAuth2URL("http://www.httpbin.org/anything", 0);
        OAuth2Request<OAuth2RequestURLEncodedFormEntity> request =
                new OAuth2Request<>(OAuth2RequestMethod.POST, url);
        request.entity(
                new OAuth2RequestURLEncodedFormEntity(2)
                        .unique("uniqueKey", "value")
                        .repeatable("repeatableKey", Arrays.asList("value1", "value2"))
        );

        // Execute request and assert.
        AbstractOAuth2Response response = REQUEST_EXECUTOR.execute(request);
        assertEquals(200, response.status());
        JsonNode body = response.readJSONBodyInputStreamAsTree();
        log.info("response: { status: {}, body: {} }", response.status(), body);
        assertNotNull(body);
        JsonNode form = body.required("form");
        assertEquals("value", form.required("uniqueKey").asText());
        assertEquals(2, form.required("repeatableKey").size());
        assertNotNull(body.path("headers").required("User-Agent").asText());
    }

}
