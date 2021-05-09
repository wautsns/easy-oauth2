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
import com.github.wautsns.easy.oauth2.core.client.function.api.OAuth2APIExchangeTokenForUser;
import com.github.wautsns.easy.oauth2.core.client.function.api.OAuth2APIExchangeTokenForUserIdentifier;
import com.github.wautsns.easy.oauth2.core.client.function.api.OAuth2APIRefreshToken;
import com.github.wautsns.easy.oauth2.core.client.function.callback.OAuth2CallbackAfterRefreshingToken;
import com.github.wautsns.easy.oauth2.core.client.function.callback.OAuth2CallbackBeforeRefreshingToken;
import com.github.wautsns.easy.oauth2.core.client.model.token.AbstractRefreshableOAuth2Token;
import com.github.wautsns.easy.oauth2.core.client.model.user.AbstractOAuth2User;
import com.github.wautsns.easy.oauth2.core.exception.OAuth2Exception;
import com.github.wautsns.easy.oauth2.core.exception.specific.OAuth2AccessTokenExpiredException;
import org.jetbrains.annotations.NotNull;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * Token refreshable oauth2 client.
 *
 * @param <A> the type of oauth2 application properties
 * @param <O> the type of oauth2 authorization properties
 * @param <T> the type of refreshable oauth2 token
 * @param <U> the type of oauth2 user
 * @author wautsns
 * @since Apr 20, 2021
 */
public abstract class AbstractTokenRefreshableOAuth2Client<A extends AbstractOAuth2ApplicationProperties, O extends AbstractOAuth2AuthorizationProperties, T extends AbstractRefreshableOAuth2Token, U extends AbstractOAuth2User>
        extends AbstractTokenAvailableOAuth2Client<A, O, T, U>
        implements OAuth2APIRefreshToken<T> {

    /** OAuth2 api: refresh token. */
    protected final @NotNull OAuth2APIRefreshToken<T> refreshToken;
    /** OAuth2 callbacks: before refreshing token. */
    protected final @NotNull List<@NotNull OAuth2CallbackBeforeRefreshingToken<T>> callbacksBeforeRefreshingToken = new LinkedList<>();
    /** OAuth2 callbacks: after refreshing token. */
    protected final @NotNull List<@NotNull OAuth2CallbackAfterRefreshingToken<T>> callbacksAfterRefreshingToken = new LinkedList<>();

    // ######################################################################################
    // #################### enhanced getter #################################################
    // ######################################################################################

    /**
     * Return callbacks before refreshing token.
     *
     * @return callbacks before refreshing token
     */
    public final @NotNull List<@NotNull OAuth2CallbackBeforeRefreshingToken<T>> callbacksBeforeRefreshingToken() {
        return callbacksBeforeRefreshingToken;
    }

    /**
     * Return callbacks after refreshing token.
     *
     * @return callbacks after refreshing token
     */
    public final @NotNull List<@NotNull OAuth2CallbackAfterRefreshingToken<T>> callbacksAfterRefreshingToken() {
        return callbacksAfterRefreshingToken;
    }

    // #########################################################################################
    // #################### oauth2 function ####################################################
    // #########################################################################################

    @Override
    public final @NotNull T refreshToken(@NotNull T token) throws OAuth2Exception {
        log.debug("Ready to refresh token. token: {}", token.raw());
        try {
            for (OAuth2CallbackBeforeRefreshingToken<T> callback : callbacksBeforeRefreshingToken) {
                callback.beforeRefreshingToken(token);
            }
            T refreshedToken = refreshToken.refreshToken(token);
            log.debug("Token has been refreshed. old: {}, new: {}", token.raw(), refreshedToken.raw());
            for (OAuth2CallbackAfterRefreshingToken<T> callback : callbacksAfterRefreshingToken) {
                callback.afterRefreshingToken(token, refreshedToken);
            }
            return refreshedToken;
        } catch (RuntimeException | OAuth2Exception e) {
            log.error("Failed to refresh token. token: {}", token.raw(), e);
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
    protected AbstractTokenRefreshableOAuth2Client(@NotNull OAuth2ClientMetadata<A, O> metadata) {
        super(metadata);
        this.refreshToken = Objects.requireNonNull(initializeOAuth2APIRefreshToken());
    }

    // ######################################################################################

    /**
     * Initialize oauth2 api: refresh token.
     *
     * @return oauth2 api: refresh token
     */
    protected abstract @NotNull OAuth2APIRefreshToken<T> initializeOAuth2APIRefreshToken();

    @Override
    protected final @NotNull OAuth2APIExchangeTokenForUserIdentifier<T> initializeOAuth2APIExchangeTokenForUserIdentifier() {
        OAuth2APIExchangeTokenForUserIdentifier<T> api = Objects.requireNonNull(
                initializeOAuth2APIExchangeTokenForUserIdentifierWithoutTryingToRefreshTokenAutomatically()
        );
        return token -> {
            try {
                return api.exchangeForUserIdentifier(token);
            } catch (OAuth2AccessTokenExpiredException e) {
                log.warn("Try to refresh token automatically due to expired token. token: {}", token.raw(), e);
                return api.exchangeForUserIdentifier(refreshToken(token));
            }
        };
    }

    @Override
    protected final @NotNull OAuth2APIExchangeTokenForUser<T, U> initializeOAuth2APIExchangeTokenForUser() {
        OAuth2APIExchangeTokenForUser<T, U> api =
                Objects.requireNonNull(
                        initializeOAuth2APIExchangeTokenForUserWithoutTryingToRefreshTokenAutomatically()
                );
        return token -> {
            try {
                return api.exchangeForUser(token);
            } catch (OAuth2AccessTokenExpiredException e) {
                log.warn("Try to refresh token automatically due to expired token. token: {}", token.raw(), e);
                return api.exchangeForUser(refreshToken(token));
            }
        };
    }

    // ######################################################################################

    /**
     * Initialize oauth2 api: exchange token for user identifier.
     *
     * @return oauth2 api: exchange token for user identifier
     */
    protected abstract @NotNull OAuth2APIExchangeTokenForUserIdentifier<T> initializeOAuth2APIExchangeTokenForUserIdentifierWithoutTryingToRefreshTokenAutomatically();

    /**
     * Initialize oauth2 api: exchange token for user.
     *
     * @return oauth2 api: exchange token for user
     */
    protected abstract @NotNull OAuth2APIExchangeTokenForUser<T, U> initializeOAuth2APIExchangeTokenForUserWithoutTryingToRefreshTokenAutomatically();

}
