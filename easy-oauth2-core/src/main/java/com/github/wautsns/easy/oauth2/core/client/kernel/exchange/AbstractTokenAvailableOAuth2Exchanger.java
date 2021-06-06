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
package com.github.wautsns.easy.oauth2.core.client.kernel.exchange;

import com.github.wautsns.easy.oauth2.core.client.configuration.AbstractOAuth2ApplicationProperties;
import com.github.wautsns.easy.oauth2.core.client.kernel.exchange.configuration.OAuth2ExchangerMetadata;
import com.github.wautsns.easy.oauth2.core.client.kernel.exchange.function.api.OAuth2APIExchangeCallbackQueryForToken;
import com.github.wautsns.easy.oauth2.core.client.kernel.exchange.function.api.OAuth2APIExchangeTokenForUser;
import com.github.wautsns.easy.oauth2.core.client.kernel.exchange.function.api.OAuth2APIExchangeTokenForUserIdentifier;
import com.github.wautsns.easy.oauth2.core.client.kernel.exchange.model.OAuth2CallbackQuery;
import com.github.wautsns.easy.oauth2.core.client.kernel.exchange.model.token.AbstractOAuth2Token;
import com.github.wautsns.easy.oauth2.core.client.kernel.exchange.model.user.AbstractOAuth2User;
import com.github.wautsns.easy.oauth2.core.exception.OAuth2Exception;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Abstract token available oauth2 client.
 *
 * @param <A> the actual type of {@link AbstractOAuth2ApplicationProperties}
 * @param <T> the actual type of {@link AbstractOAuth2Token}
 * @param <U> the actual type of {@link AbstractOAuth2User}
 * @author wautsns
 * @since Apr 20, 2021
 */
public abstract class AbstractTokenAvailableOAuth2Exchanger<A extends AbstractOAuth2ApplicationProperties, T extends AbstractOAuth2Token, U extends AbstractOAuth2User>
        extends AbstractOAuth2Exchanger<A, U>
        implements OAuth2APIExchangeCallbackQueryForToken<T>,
                   OAuth2APIExchangeTokenForUserIdentifier<T>,
                   OAuth2APIExchangeTokenForUser<T, U> {

    /** OAuth2 api: exchange callback query for token. */
    private final @NotNull OAuth2APIExchangeCallbackQueryForToken<T> exchangeCallbackQueryForToken;
    /** OAuth2 api: exchange token for user identifier. */
    private final @NotNull OAuth2APIExchangeTokenForUserIdentifier<T> exchangeTokenForUserIdentifier;
    /** OAuth2 api: exchange token for user. */
    private final @NotNull OAuth2APIExchangeTokenForUser<T, U> exchangeTokenForUser;

    // ##################################################################################
    // #################### oauth2 api ##################################################
    // ##################################################################################

    @Override
    public final @NotNull T exchangeForToken(@NotNull OAuth2CallbackQuery query) throws OAuth2Exception {
        log.debug("Ready to exchange callback query for token. callbackQuery: {}", query.raw());
        try {
            T token = exchangeCallbackQueryForToken.exchangeForToken(query);
            log.debug(
                    "Token has been exchanged with callback query. token: {}, callbackQuery: {}",
                    token.raw(), query.raw()
            );
            return token;
        } catch (RuntimeException | OAuth2Exception e) {
            log.error(
                    "Failed to exchange callback query for token. callbackQuery: {}",
                    query.raw(), e
            );
            throw e;
        }
    }

    @Override
    public final @NotNull String exchangeForUserIdentifier(@NotNull T token) throws OAuth2Exception {
        log.debug("Ready to exchange token for user identifier. token: {}", token.raw());
        try {
            String userIdentifier = exchangeTokenForUserIdentifier.exchangeForUserIdentifier(token);
            log.debug(
                    "User identifier has been exchanged with token. userIdentifier: {}, token: {}",
                    userIdentifier, token.raw()
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
            log.debug(
                    "User has been exchanged with token. user: {}, token: {}",
                    user.raw(), token.raw()
            );
            return user;
        } catch (RuntimeException | OAuth2Exception e) {
            log.error("Failed to exchange token for user. token: {}", token.raw(), e);
            throw e;
        }
    }

    // ##################################################################################
    // #################### constructor #################################################
    // ##################################################################################

    /**
     * Construct an instance.
     *
     * @param metadata {@link #metadata}
     */
    protected AbstractTokenAvailableOAuth2Exchanger(@NotNull OAuth2ExchangerMetadata<A> metadata) {
        super(metadata);
        this.exchangeCallbackQueryForToken =
                Objects.requireNonNull(initializeAPIExchangeCallbackQueryForToken());
        this.exchangeTokenForUserIdentifier =
                Objects.requireNonNull(initializeAPIExchangeTokenForUserIdentifier());
        this.exchangeTokenForUser = Objects.requireNonNull(initializeAPIExchangeTokenForUser());
    }

    // ##################################################################################
    // #################### initialize oauth2 api #######################################
    // ##################################################################################

    /**
     * Initialize oauth2 api: exchange callback query for token.
     *
     * @return oauth2 api: exchange callback query for token
     */
    protected abstract @NotNull OAuth2APIExchangeCallbackQueryForToken<T> initializeAPIExchangeCallbackQueryForToken();

    /**
     * Initialize oauth2 api: exchange token for user identifier.
     *
     * @return oauth2 api: exchange token for user identifier
     */
    protected abstract @NotNull OAuth2APIExchangeTokenForUserIdentifier<T> initializeAPIExchangeTokenForUserIdentifier();

    /**
     * Initialize oauth2 api: exchange token for user.
     *
     * @return oauth2 api: exchange token for user
     */
    protected abstract @NotNull OAuth2APIExchangeTokenForUser<T, U> initializeAPIExchangeTokenForUser();

}
