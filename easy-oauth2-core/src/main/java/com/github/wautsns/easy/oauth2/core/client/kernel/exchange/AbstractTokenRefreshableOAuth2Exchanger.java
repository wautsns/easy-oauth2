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
import com.github.wautsns.easy.oauth2.core.client.kernel.exchange.function.api.OAuth2APIExchangeTokenForUser;
import com.github.wautsns.easy.oauth2.core.client.kernel.exchange.function.api.OAuth2APIExchangeTokenForUserIdentifier;
import com.github.wautsns.easy.oauth2.core.client.kernel.exchange.function.api.OAuth2APIRefreshToken;
import com.github.wautsns.easy.oauth2.core.client.kernel.exchange.function.callback.OAuth2CallbackAfterRefreshingToken;
import com.github.wautsns.easy.oauth2.core.client.kernel.exchange.model.token.AbstractRefreshableOAuth2Token;
import com.github.wautsns.easy.oauth2.core.client.kernel.exchange.model.user.AbstractOAuth2User;
import com.github.wautsns.easy.oauth2.core.exception.OAuth2Exception;
import com.github.wautsns.easy.oauth2.core.exception.specific.OAuth2AccessTokenExpiredException;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Abstract token refreshable oauth2 exchanger.
 *
 * @param <A> the actual type of {@link AbstractOAuth2ApplicationProperties}
 * @param <T> the actual type of {@link AbstractRefreshableOAuth2Token}
 * @param <U> the actual type of {@link AbstractOAuth2User}
 * @author wautsns
 * @since Apr 20, 2021
 */
public abstract class AbstractTokenRefreshableOAuth2Exchanger<A extends AbstractOAuth2ApplicationProperties, T extends AbstractRefreshableOAuth2Token, U extends AbstractOAuth2User>
        extends AbstractTokenAvailableOAuth2Exchanger<A, T, U>
        implements OAuth2APIRefreshToken<T> {

    /** OAuth2 api: refresh token. */
    private final @NotNull OAuth2APIRefreshToken<T> refreshToken;

    /** OAuth2 callbacks: after refreshing token. */
    private final @NotNull List<@NotNull OAuth2CallbackAfterRefreshingToken<T>> callbacksAfterRefreshingToken = new CopyOnWriteArrayList<>();

    // ##################################################################################
    // #################### enhanced getter #############################################
    // ##################################################################################

    /**
     * Return callbacks after refreshing token.
     *
     * @return callbacks after refreshing token
     */
    public final @NotNull List<@NotNull OAuth2CallbackAfterRefreshingToken<T>> callbacksAfterRefreshingToken() {
        return callbacksAfterRefreshingToken;
    }

    // ##################################################################################
    // #################### oauth2 api ##################################################
    // ##################################################################################

    @Override
    public final @NotNull T refreshToken(@NotNull T token) throws OAuth2Exception {
        log.debug("Ready to refresh token. token: {}", token.raw());
        try {
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

    // ##################################################################################
    // #################### constructor #################################################
    // ##################################################################################

    /**
     * Construct an instance.
     *
     * @param metadata metadata
     */
    protected AbstractTokenRefreshableOAuth2Exchanger(@NotNull OAuth2ExchangerMetadata<A> metadata) {
        super(metadata);
        this.refreshToken = Objects.requireNonNull(initializeRefreshToken());
    }

    // ##################################################################################
    // #################### initialize oauth2 api #######################################
    // ##################################################################################

    @Override
    protected final @NotNull OAuth2APIExchangeTokenForUserIdentifier<T> initializeAPIExchangeTokenForUserIdentifier() {
        OAuth2APIExchangeTokenForUserIdentifier<T> api = Objects.requireNonNull(
                initializeAPIExchangeTokenForUserIdentifierWithoutRefreshingExpiredToken()
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
    protected final @NotNull OAuth2APIExchangeTokenForUser<T, U> initializeAPIExchangeTokenForUser() {
        OAuth2APIExchangeTokenForUser<T, U> api = Objects.requireNonNull(
                initializeAPIExchangeTokenForUserWithoutRefreshingExpiredToken()
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

    /**
     * Initialize oauth2 api: exchange token for user identifier.
     *
     * @return oauth2 api: exchange token for user identifier
     * @implNote The implementation method does not require attention to {@link
     *         OAuth2AccessTokenExpiredException}
     */
    protected abstract @NotNull OAuth2APIExchangeTokenForUserIdentifier<T> initializeAPIExchangeTokenForUserIdentifierWithoutRefreshingExpiredToken();

    /**
     * Initialize oauth2 api: exchange token for user.
     *
     * @return oauth2 api: exchange token for user
     * @implNote The implementation method does not require attention to {@link
     *         OAuth2AccessTokenExpiredException}
     */
    protected abstract @NotNull OAuth2APIExchangeTokenForUser<T, U> initializeAPIExchangeTokenForUserWithoutRefreshingExpiredToken();

    /**
     * Initialize oauth2 api: refresh token.
     *
     * @return oauth2 api: refresh token
     */
    protected abstract @NotNull OAuth2APIRefreshToken<T> initializeRefreshToken();

}
