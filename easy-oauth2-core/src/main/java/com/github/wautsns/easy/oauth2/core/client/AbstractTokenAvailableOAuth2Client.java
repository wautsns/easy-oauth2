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
import com.github.wautsns.easy.oauth2.core.client.function.api.OAuth2APIExchangeCallbackQueryForToken;
import com.github.wautsns.easy.oauth2.core.client.function.api.OAuth2APIExchangeTokenForUser;
import com.github.wautsns.easy.oauth2.core.client.function.api.OAuth2APIExchangeTokenForUserIdentifier;
import com.github.wautsns.easy.oauth2.core.client.model.OAuth2CallbackQuery;
import com.github.wautsns.easy.oauth2.core.client.model.token.AbstractOAuth2Token;
import com.github.wautsns.easy.oauth2.core.client.model.user.AbstractOAuth2User;
import com.github.wautsns.easy.oauth2.core.exception.OAuth2Exception;
import org.jetbrains.annotations.NotNull;
import java.util.Objects;

/**
 * Token available oauth2 client.
 *
 * @param <A> the type of oauth2 application properties
 * @param <O> the type of oauth2 authorization properties
 * @param <T> the type of oauth2 token
 * @param <U> the type of oauth2 user
 * @author wautsns
 * @since Apr 20, 2021
 */
public abstract class AbstractTokenAvailableOAuth2Client<A extends AbstractOAuth2ApplicationProperties, O extends AbstractOAuth2AuthorizationProperties, T extends AbstractOAuth2Token, U extends AbstractOAuth2User>
        extends AbstractOAuth2Client<A, O, U>
        implements OAuth2APIExchangeCallbackQueryForToken<T>,
                   OAuth2APIExchangeTokenForUserIdentifier<T>,
                   OAuth2APIExchangeTokenForUser<T, U> {

    /** OAuth2 api: exchange callback query for token. */
    protected final @NotNull OAuth2APIExchangeCallbackQueryForToken<T> exchangeCallbackQueryForToken;
    /** OAuth2 api: exchange token for user identifier. */
    protected final @NotNull OAuth2APIExchangeTokenForUserIdentifier<T> exchangeTokenForUserIdentifier;
    /** OAuth2 api: exchange token for user. */
    protected final @NotNull OAuth2APIExchangeTokenForUser<T, U> exchangeTokenForUser;

    // #########################################################################################
    // #################### oauth2 function ####################################################
    // #########################################################################################

    @Override
    public final @NotNull T exchangeForToken(@NotNull OAuth2CallbackQuery query) throws OAuth2Exception {
        log.debug("Ready to exchange callback query for token. callbackQuery: {}", query.raw());
        try {
            T token = exchangeCallbackQueryForToken.exchangeForToken(query);
            log.debug(
                    "Token has been exchanged with callback query. callbackQuery: {}, token: {}",
                    query.raw(), token.raw()
            );
            return token;
        } catch (RuntimeException | OAuth2Exception e) {
            log.error("Failed to exchange callback query for token. callbackQuery: {}", query.raw(), e);
            throw e;
        }
    }

    @Override
    public final @NotNull String exchangeForUserIdentifier(@NotNull T token) throws OAuth2Exception {
        log.debug("Ready to exchange token for user identifier. token: {}", token.raw());
        try {
            String userIdentifier = exchangeTokenForUserIdentifier.exchangeForUserIdentifier(token);
            log.debug(
                    "User identifier has been exchanged with token. token: {}, userIdentifier: {}",
                    token.raw(), userIdentifier
            );
            return userIdentifier;
        } catch (RuntimeException | OAuth2Exception e) {
            log.error("Failed to exchange token for user identifier. token: {}", token.raw(), e);
            throw e;
        }
    }

    @Override
    public final @NotNull U exchangeForUser(@NotNull T token) throws OAuth2Exception {
        log.debug("Ready to exchange token for user. token: {}", token.raw());
        try {
            U user = exchangeTokenForUser.exchangeForUser(token);
            log.debug("User has been exchanged with token. token: {}, user: {}", token.raw(), user.raw());
            return user;
        } catch (RuntimeException | OAuth2Exception e) {
            log.error("Failed to exchange token for user. token: {}", token.raw(), e);
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
    protected AbstractTokenAvailableOAuth2Client(@NotNull OAuth2ClientMetadata<A, O> metadata) {
        super(metadata);
        this.exchangeCallbackQueryForToken = Objects.requireNonNull(initializeOAuth2APIExchangeCallbackQueryForToken());
        this.exchangeTokenForUserIdentifier =
                Objects.requireNonNull(initializeOAuth2APIExchangeTokenForUserIdentifier());
        this.exchangeTokenForUser = Objects.requireNonNull(initializeOAuth2APIExchangeTokenForUser());
    }

    // ######################################################################################

    /**
     * Initialize oauth2 api: exchange callback query for token.
     *
     * @return oauth2 api: exchange callback query for token
     */
    protected abstract @NotNull OAuth2APIExchangeCallbackQueryForToken<T> initializeOAuth2APIExchangeCallbackQueryForToken();

    /**
     * Initialize oauth2 api: exchange token for user identifier.
     *
     * @return oauth2 api: exchange token for user identifier
     */
    protected abstract @NotNull OAuth2APIExchangeTokenForUserIdentifier<T> initializeOAuth2APIExchangeTokenForUserIdentifier();

    /**
     * Initialize oauth2 api: exchange token for user.
     *
     * @return oauth2 api: exchange token for user
     */
    protected abstract @NotNull OAuth2APIExchangeTokenForUser<T, U> initializeOAuth2APIExchangeTokenForUser();

}
