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
package com.github.wautsns.easy.oauth2.extension.client.builtin;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.wautsns.easy.oauth2.core.client.AbstractOAuth2Client;
import com.github.wautsns.easy.oauth2.core.client.AbstractTokenAvailableOAuth2Client;
import com.github.wautsns.easy.oauth2.core.client.AbstractTokenRefreshableOAuth2Client;
import com.github.wautsns.easy.oauth2.core.client.model.OAuth2CallbackQuery;
import com.github.wautsns.easy.oauth2.core.client.model.token.AbstractOAuth2Token;
import com.github.wautsns.easy.oauth2.core.client.model.token.AbstractRefreshableOAuth2Token;
import com.github.wautsns.easy.oauth2.core.client.model.user.AbstractOAuth2User;
import com.github.wautsns.easy.oauth2.core.exception.OAuth2Exception;
import com.github.wautsns.easy.oauth2.core.request.model.basic.OAuth2URL;
import com.github.wautsns.easy.oauth2.core.request.util.OAuth2DataUtils;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Objects;
import java.util.UUID;

/**
 * Abstract oauth2 client test.
 *
 * <ul>
 * <li style="list-style-type:none">########## VM options ###############</li>
 * <li>-Dclient-id=${clientId}</li>
 * <li>-Dclient-secret=${clientSecret}</li>
 * <li>-Dcallback-url=${callbackURL}</li>
 * </ul>
 *
 * @author wautsns
 * @since May 08, 2021
 */
@SuppressWarnings("all")
public abstract class AbstractOAuth2ClientTest {

    /** Logger. */
    private final Logger log = LoggerFactory.getLogger(getClass());

    // #########################################################################################

    /** OAuth2 client. */
    private final AbstractOAuth2Client<?, ?, ?> client = initializeOAuth2Client(
            Objects.requireNonNull(System.getProperty("client-id")),
            Objects.requireNonNull(System.getProperty("client-secret")),
            Objects.requireNonNull(System.getProperty("callback-url"))
    );

    // #########################################################################################

    @Test
    public final void testInitializeAuthorizeURL() throws OAuth2Exception {
        String state = UUID.randomUUID().toString();
        OAuth2URL authorizeURL = client.initializeAuthorizeURL(state);
        Assert.assertNotNull(authorizeURL);
        log.info("authorize url: {}", authorizeURL.asText());
    }

    // #########################################################################################

    @Test
    public final void testOAuth2Function() throws OAuth2Exception {
        JsonNode raw = OAuth2DataUtils.newObjectNode().put("code", authorizeCode());
        OAuth2CallbackQuery query = new OAuth2CallbackQuery(raw);
        if (client instanceof AbstractTokenRefreshableOAuth2Client) {
            testOAuth2Function(query, (AbstractTokenRefreshableOAuth2Client) client);
        } else if (client instanceof AbstractTokenAvailableOAuth2Client) {
            testOAuth2Function(query, (AbstractTokenAvailableOAuth2Client) client);
        } else {
            testOAuth2Function(query, client);
        }
    }

    /**
     * Test oauth2 function
     *
     * @param query callback query
     * @param client oauth2 client
     * @throws OAuth2Exception if oauth2 related error occurs
     */
    private void testOAuth2Function(
            @NotNull OAuth2CallbackQuery query, @NotNull AbstractOAuth2Client client) throws OAuth2Exception {
        client.exchangeForUser(query);
    }

    /**
     * Test oauth2 function
     *
     * @param query callback query
     * @param client token available oauth2 client
     * @throws OAuth2Exception if oauth2 related error occurs
     */
    private void testOAuth2Function(
            @NotNull OAuth2CallbackQuery query, @NotNull AbstractTokenAvailableOAuth2Client client) throws OAuth2Exception {
        AbstractOAuth2Token token = client.exchangeForToken(query);
        Assert.assertNotNull(token);
        AbstractOAuth2User user = client.exchangeForUser(token);
        Assert.assertNotNull(user);
        String userIdentifier = client.exchangeForUserIdentifier(token);
        Assert.assertNotNull(userIdentifier);
    }

    /**
     * Test oauth2 function
     *
     * @param query callback query
     * @param client token refreshable oauth2 client
     * @throws OAuth2Exception if oauth2 related error occurs
     */
    private void testOAuth2Function(
            @NotNull OAuth2CallbackQuery query, @NotNull AbstractTokenRefreshableOAuth2Client client) throws OAuth2Exception {
        AbstractOAuth2Token token = client.exchangeForToken(query);
        Assert.assertNotNull(token);
        AbstractOAuth2User user = client.exchangeForUser(token);
        Assert.assertNotNull(user);
        String userIdentifier = client.exchangeForUserIdentifier(token);
        Assert.assertNotNull(userIdentifier);
        token = client.refreshToken((AbstractRefreshableOAuth2Token) token);
        Assert.assertNotNull(token);
        user = client.exchangeForUser(token);
        Assert.assertNotNull(user);
    }

    // #########################################################################################
    // #################### protected abstract method ##########################################
    // #########################################################################################

    /**
     * Return authorize code.
     *
     * @return authorize code
     */
    protected abstract @NotNull String authorizeCode();

    /**
     * Initialize oauth2 client.
     *
     * @param clientSecret client secret
     * @return oauth2 client
     */
    protected abstract @NotNull AbstractOAuth2Client<?, ?, ?> initializeOAuth2Client(@NotNull String clientId, @NotNull String clientSecret, @NotNull String callbackURL);

}
