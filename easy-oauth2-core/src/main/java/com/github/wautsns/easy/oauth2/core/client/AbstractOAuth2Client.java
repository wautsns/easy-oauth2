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
package com.github.wautsns.easy.oauth2.core.client;

import com.github.wautsns.easy.oauth2.core.client.configuration.AbstractOAuth2ApplicationProperties;
import com.github.wautsns.easy.oauth2.core.client.configuration.AbstractOAuth2AuthorizationProperties;
import com.github.wautsns.easy.oauth2.core.client.configuration.OAuth2ClientMetadata;
import com.github.wautsns.easy.oauth2.core.client.configuration.OAuth2PlatformIdentifierSupplier;
import com.github.wautsns.easy.oauth2.core.client.function.api.OAuth2APIExchangeCallbackQueryForUser;
import com.github.wautsns.easy.oauth2.core.client.function.api.OAuth2APIExchangeCallbackQueryForUserIdentifier;
import com.github.wautsns.easy.oauth2.core.client.function.operation.OAuth2OperationInitializeAuthorizeURL;
import com.github.wautsns.easy.oauth2.core.client.model.OAuth2CallbackQuery;
import com.github.wautsns.easy.oauth2.core.client.model.user.AbstractOAuth2User;
import com.github.wautsns.easy.oauth2.core.exception.OAuth2Exception;
import com.github.wautsns.easy.oauth2.core.request.model.basic.OAuth2URL;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Objects;

/**
 * Abstract oauth2 client.
 *
 * @param <A> the type of oauth2 application properties
 * @param <O> the type of oauth2 authorization properties
 * @param <U> the type of oauth2 user
 * @author wautsns
 * @since Apr 01, 2021
 */
public abstract class AbstractOAuth2Client<A extends AbstractOAuth2ApplicationProperties, O extends AbstractOAuth2AuthorizationProperties, U extends AbstractOAuth2User>
        implements OAuth2PlatformIdentifierSupplier,
                   OAuth2OperationInitializeAuthorizeURL,
                   OAuth2APIExchangeCallbackQueryForUserIdentifier,
                   OAuth2APIExchangeCallbackQueryForUser<U> {

    /** Logger. */
    protected final @NotNull Logger log = LoggerFactory.getLogger(getClass());

    // ######################################################################################

    /** OAuth2 client metadata. */
    protected final @NotNull OAuth2ClientMetadata<A, O> metadata;

    /** OAuth2 operation: initialize authorize url. */
    protected final @NotNull OAuth2OperationInitializeAuthorizeURL initializeAuthorizeURL;
    /** OAuth2 api: exchange callback query for user identifier. */
    protected final @NotNull OAuth2APIExchangeCallbackQueryForUserIdentifier exchangeCallbackQueryForUserIdentifier;
    /** OAuth2 api: exchange callback query for user. */
    protected final @NotNull OAuth2APIExchangeCallbackQueryForUser<U> exchangeCallbackQueryForUser;

    // ######################################################################################
    // #################### enhanced getter #################################################
    // ######################################################################################

    /**
     * Return platform identifier.
     *
     * @return platform identifier
     */
    @Override
    public final @NotNull String platformIdentifier() {
        return metadata.application().platformIdentifier();
    }

    // #########################################################################################
    // #################### oauth2 function ####################################################
    // #########################################################################################

    @Override
    public final @NotNull OAuth2URL initializeAuthorizeURL(@Nullable String state) throws OAuth2Exception {
        log.debug("Ready to initialize authorize url. state: {}", state);
        try {
            OAuth2URL url = initializeAuthorizeURL.initializeAuthorizeURL(state);
            log.debug("Authorize url has been initialized. url: {}", url);
            return url;
        } catch (RuntimeException | OAuth2Exception e) {
            log.error("Failed to initialize authorize url. state: {}", state, e);
            throw e;
        }
    }

    @Override
    public final @NotNull String exchangeForUserIdentifier(@NotNull OAuth2CallbackQuery query) throws OAuth2Exception {
        log.debug("Ready to exchange callback query for user identifier. callbackQuery: {}", query.raw());
        try {
            String userIdentifier = exchangeCallbackQueryForUserIdentifier.exchangeForUserIdentifier(query);
            log.debug(
                    "User identifier has been exchanged with callback query. callbackQuery: {}, userIdentifier: {}",
                    query.raw(), userIdentifier
            );
            return userIdentifier;
        } catch (RuntimeException | OAuth2Exception e) {
            log.error("Failed to exchange callback query for user identifier. callbackQuery: {}", query.raw(), e);
            throw e;
        }
    }

    @Override
    public final @NotNull U exchangeForUser(@NotNull OAuth2CallbackQuery query) throws OAuth2Exception {
        log.debug("Ready to exchange callback query for user. callbackQuery: {}", query.raw());
        try {
            U user = exchangeCallbackQueryForUser.exchangeForUser(query);
            log.debug(
                    "User has been exchanged with callback query. callbackQuery: {}, user: {}",
                    query.raw(), user.raw()
            );
            return user;
        } catch (RuntimeException | OAuth2Exception e) {
            log.error("Failed to exchange callback query for user. callbackQuery: {}", query.raw(), e);
            throw e;
        }
    }

    // ######################################################################################
    // #################### constructor #####################################################
    // ######################################################################################

    /**
     * Construct an instance.
     *
     * @param metadata metadata
     */
    protected AbstractOAuth2Client(@NotNull OAuth2ClientMetadata<A, O> metadata) {
        this.metadata = Objects.requireNonNull(metadata);
        this.metadata.application().validate();
        this.metadata.authorization().validate();
        this.initializeAuthorizeURL = Objects.requireNonNull(initializeOAuth2OperationInitializeAuthorizeURL());
        this.exchangeCallbackQueryForUserIdentifier =
                Objects.requireNonNull(initializeOAuth2APIExchangeCallbackQueryForUserIdentifier());
        this.exchangeCallbackQueryForUser = Objects.requireNonNull(initializeOAuth2APIExchangeCallbackQueryForUser());
    }

    // ######################################################################################

    /**
     * Initialize oauth2 operation: initialize authorize url.
     *
     * @return oauth2 operation: initialize authorize url
     */
    protected abstract @NotNull OAuth2OperationInitializeAuthorizeURL initializeOAuth2OperationInitializeAuthorizeURL();

    /**
     * Initialize oauth2 api: exchange callback query for user identifier.
     *
     * @return oauth2 api: exchange callback query for user identifier
     */
    protected abstract @NotNull OAuth2APIExchangeCallbackQueryForUserIdentifier initializeOAuth2APIExchangeCallbackQueryForUserIdentifier();

    /**
     * Initialize oauth2 api: exchange callback query for user.
     *
     * @return oauth2 api: exchange callback query for user
     */
    protected abstract @NotNull OAuth2APIExchangeCallbackQueryForUser<U> initializeOAuth2APIExchangeCallbackQueryForUser();

}
