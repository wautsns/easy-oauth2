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
package com.github.wautsns.easy.oauth2.extension.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.wautsns.easy.oauth2.core.client.OAuth2Client;
import com.github.wautsns.easy.oauth2.core.client.assembly.OAuth2PlatformAssemblyFactory;
import com.github.wautsns.easy.oauth2.core.client.assembly.OAuth2PlatformAssemblyFactoryManager;
import com.github.wautsns.easy.oauth2.core.client.configuration.AbstractOAuth2ApplicationProperties;
import com.github.wautsns.easy.oauth2.core.client.kernel.authorize.configuration.AbstractOAuth2AuthorizationProperties;
import com.github.wautsns.easy.oauth2.core.client.kernel.authorize.configuration.OAuth2AuthorizeURLInitializerMetadata;
import com.github.wautsns.easy.oauth2.core.client.kernel.exchange.AbstractOAuth2Exchanger;
import com.github.wautsns.easy.oauth2.core.client.kernel.exchange.AbstractTokenAvailableOAuth2Exchanger;
import com.github.wautsns.easy.oauth2.core.client.kernel.exchange.AbstractTokenRefreshableOAuth2Exchanger;
import com.github.wautsns.easy.oauth2.core.client.kernel.exchange.configuration.OAuth2ExchangerMetadata;
import com.github.wautsns.easy.oauth2.core.client.kernel.exchange.model.OAuth2CallbackQuery;
import com.github.wautsns.easy.oauth2.core.client.kernel.exchange.model.token.AbstractOAuth2Token;
import com.github.wautsns.easy.oauth2.core.client.kernel.exchange.model.token.AbstractRefreshableOAuth2Token;
import com.github.wautsns.easy.oauth2.core.client.kernel.exchange.model.user.AbstractOAuth2User;
import com.github.wautsns.easy.oauth2.core.exception.OAuth2Exception;
import com.github.wautsns.easy.oauth2.core.request.executor.OAuth2RequestExecutorFactoryManager;
import com.github.wautsns.easy.oauth2.core.request.executor.configuration.OAuth2RequestExecutorProperties;
import com.github.wautsns.easy.oauth2.core.request.model.basic.OAuth2URL;
import com.github.wautsns.easy.oauth2.core.request.util.OAuth2DataUtils;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.Duration;
import java.util.Objects;
import java.util.UUID;

/**
 * Abstract oauth2 test.
 *
 * <ul>
 * <li style="list-style-type:none">########## VM options ###############</li>
 * <li>-Dclient-id=${clientId}</li>
 * <li>-Dclient-secret=${clientSecret}</li>
 * <li>-Dauthorize-callback=${callbackURL}</li>
 * <li>-Dconnect-timeout=${connectTimeout} (e.g. PT30S)</li>
 * <li>-Dsocket-timeout=${socketTimeout} (e.g. PT30S)</li>
 * <li>-Dproxy=${proxy}</li>
 * </ul>
 *
 * @author wautsns
 * @since May 08, 2021
 */
@SuppressWarnings("all")
public abstract class AbstractOAuth2Test {

    /** Logger. */
    private final Logger log = LoggerFactory.getLogger(getClass());

    // ##################################################################################

    /** Platform. */
    private final String platform;

    // ##################################################################################

    @Test
    public final void testAuthorize() throws OAuth2Exception {
        String state = UUID.randomUUID().toString();
        OAuth2URL authorizeURL = OAuth2Client.authorizeURL(platform, state);
        Assert.assertNotNull(authorizeURL);
        log.info("authorize url: {}", authorizeURL.asText());
    }

    // ##################################################################################

    @Test
    public final void testExchange() throws OAuth2Exception {
        JsonNode raw = OAuth2DataUtils.createObjectNode().put("code", authorizeCode());
        OAuth2CallbackQuery query = new OAuth2CallbackQuery(raw);
        AbstractOAuth2Exchanger exchanger = OAuth2Client.exchanger(platform);
        if (exchanger instanceof AbstractTokenAvailableOAuth2Exchanger) {
            testExchange(query, (AbstractTokenAvailableOAuth2Exchanger) exchanger);
        } else if (exchanger instanceof AbstractTokenRefreshableOAuth2Exchanger) {
            testExchange(query, (AbstractTokenRefreshableOAuth2Exchanger) exchanger);
        } else {
            testExchange(query, exchanger);
        }
    }

    /**
     * Test oauth2 function
     *
     * @param query callback query
     * @param exchanger exchanger
     * @throws OAuth2Exception if oauth2 related error occurs
     */
    private void testExchange(@NotNull OAuth2CallbackQuery query, @NotNull AbstractOAuth2Exchanger exchanger) throws OAuth2Exception {
        exchanger.exchangeForUser(query);
    }

    /**
     * Test oauth2 function
     *
     * @param query callback query
     * @param exchanger exchanger
     * @throws OAuth2Exception if oauth2 related error occurs
     */
    private void testExchange(
            @NotNull OAuth2CallbackQuery query, @NotNull AbstractTokenAvailableOAuth2Exchanger exchanger) throws OAuth2Exception {
        AbstractOAuth2Token token = exchanger.exchangeForToken(query);
        Assert.assertNotNull(token);
        AbstractOAuth2User user = exchanger.exchangeForUser(token);
        Assert.assertNotNull(user);
        String userIdentifier = exchanger.exchangeForUserIdentifier(token);
        Assert.assertNotNull(userIdentifier);
    }

    /**
     * Test oauth2 api.
     *
     * @param query callback query
     * @param exchanger exchanger
     * @throws OAuth2Exception if oauth2 related error occurs
     */
    private void testExchange(
            @NotNull OAuth2CallbackQuery query, @NotNull AbstractTokenRefreshableOAuth2Exchanger exchanger) throws OAuth2Exception {
        AbstractOAuth2Token token = exchanger.exchangeForToken(query);
        Assert.assertNotNull(token);
        AbstractOAuth2User user = exchanger.exchangeForUser(token);
        Assert.assertNotNull(user);
        String userIdentifier = exchanger.exchangeForUserIdentifier(token);
        Assert.assertNotNull(userIdentifier);
        token = exchanger.refreshToken((AbstractRefreshableOAuth2Token) token);
        Assert.assertNotNull(token);
        user = exchanger.exchangeForUser(token);
        Assert.assertNotNull(user);
    }

    // ##################################################################################
    // #################### enhanced getter #############################################
    // ##################################################################################

    /**
     * Return authorize code.
     *
     * @return authorize code
     */
    protected abstract @NotNull String authorizeCode();

    // ##################################################################################
    // #################### constructor #################################################
    // ##################################################################################

    protected AbstractOAuth2Test() {
        String clientId = Objects.requireNonNull(System.getProperty("client-id"));
        String clientSecret = Objects.requireNonNull(System.getProperty("client-secret"));
        String authorizeCallback = Objects.requireNonNull(System.getProperty("authorize-callback"));
        AbstractOAuth2ApplicationProperties applicationProperties = initializeApplicationProperties(
                clientId, clientSecret, authorizeCallback
        );
        this.platform = applicationProperties.platform();
        OAuth2PlatformAssemblyFactory factory = OAuth2PlatformAssemblyFactoryManager.one(platform);
        OAuth2Client.register(
                factory.createAuthorizeURLInitializer(
                        new OAuth2AuthorizeURLInitializerMetadata(
                                applicationProperties,
                                initializeAuthorizationProperties()
                        )
                )
        );
        OAuth2Client.register(
                factory.createExchanger(
                        new OAuth2ExchangerMetadata(
                                applicationProperties,
                                OAuth2RequestExecutorFactoryManager.any().create(initializeRequestExecutorProperties())
                        )
                )
        );
    }

    // ##################################################################################

    /**
     * Initialize application properties.
     *
     * @param clientId client id
     * @param clientSecret client secret
     * @param authorizeCallback authorize callback
     * @return application properties
     */
    protected abstract @NotNull AbstractOAuth2ApplicationProperties initializeApplicationProperties(
            @NotNull String clientId, @NotNull String clientSecret, @NotNull String authorizeCallback);

    /**
     * Initialize authorization properties.
     *
     * @return authorization properties
     */
    protected abstract @NotNull AbstractOAuth2AuthorizationProperties initializeAuthorizationProperties();

    /**
     * Initialize request executor properties.
     *
     * @return request executor properties
     */
    private @NotNull OAuth2RequestExecutorProperties initializeRequestExecutorProperties() {
        OAuth2RequestExecutorProperties requestExecutorProperties = new OAuth2RequestExecutorProperties();
        String connectionTimeout = System.getProperty("connect-timeout");
        if (connectionTimeout != null) {
            requestExecutorProperties.setConnectTimeout(Duration.parse(connectionTimeout));
        }
        String socketTimeout = System.getProperty("socket-timeout");
        if (socketTimeout != null) {
            requestExecutorProperties.setSocketTimeout(Duration.parse(socketTimeout));
        }
        String proxy = System.getProperty("proxy");
        if (proxy != null) {
            requestExecutorProperties.setProxy(proxy);
        }
        return requestExecutorProperties;
    }

}
