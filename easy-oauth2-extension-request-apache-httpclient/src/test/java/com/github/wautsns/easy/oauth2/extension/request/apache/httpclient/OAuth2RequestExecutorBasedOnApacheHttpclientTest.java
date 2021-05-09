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

import com.github.wautsns.easy.oauth2.core.request.executor.AbstractOAuth2RequestExecutor;
import com.github.wautsns.easy.oauth2.core.request.executor.OAuth2RequestExecutorFactory;
import com.github.wautsns.easy.oauth2.core.request.executor.OAuth2RequestExecutorFactoryManager;
import com.github.wautsns.easy.oauth2.core.request.executor.configuration.OAuth2RequestExecutorProperties;
import com.github.wautsns.easy.oauth2.core.request.model.basic.OAuth2Headers;
import com.github.wautsns.easy.oauth2.core.request.model.basic.OAuth2URL;
import com.github.wautsns.easy.oauth2.core.request.model.request.OAuth2Request;
import com.github.wautsns.easy.oauth2.core.request.model.request.OAuth2RequestMethod;
import com.github.wautsns.easy.oauth2.core.request.model.response.AbstractOAuth2Response;
import com.github.wautsns.easy.oauth2.core.request.util.OAuth2DataUtils;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.InputStream;

/**
 * Test {@link OAuth2RequestExecutorBasedOnApacheHttpclient}.
 *
 * @author wautsns
 * @since Mar 31, 2021
 */
public class OAuth2RequestExecutorBasedOnApacheHttpclientTest {

    /** Logger. */
    private static final Logger log = LoggerFactory.getLogger(OAuth2RequestExecutorBasedOnApacheHttpclientTest.class);

    // ######################################################################################

    @Test
    public void testExecute() throws Exception {
        OAuth2RequestExecutorFactory<?> factory = OAuth2RequestExecutorFactoryManager.any();
        AbstractOAuth2RequestExecutor<?> executor = factory.create(new OAuth2RequestExecutorProperties());
        String urlWithoutQueryAndAnchor = "https://api.github.com/users/wautsns";
        OAuth2URL url = new OAuth2URL(urlWithoutQueryAndAnchor, 1);
        OAuth2Request<?> request = new OAuth2Request<>(OAuth2RequestMethod.GET, url);
        request.url().query().unique("key", "value");
        request.headers(new OAuth2Headers(1).userAgentEasyOAuth2());
        AbstractOAuth2Response response = executor.execute(request);
        Assert.assertEquals(200, response.status());
        InputStream bodyInputStream = response.bodyInputStream();
        Assert.assertNotNull(bodyInputStream);
        String body = OAuth2DataUtils.readInputStreamAsText(bodyInputStream);
        log.info("response status: {}, response body: {}", response.status(), body);
    }

}
