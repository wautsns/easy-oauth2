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
package com.github.wautsns.easy.oauth2.core.assembly.kernel.exchange;

import com.github.wautsns.easy.oauth2.core.assembly.configuration.AbstractOAuth2ApplicationProperties;
import com.github.wautsns.easy.oauth2.core.assembly.configuration.OAuth2PlatformSupplier;
import com.github.wautsns.easy.oauth2.core.assembly.kernel.exchange.configuration.OAuth2ExchangerMetadata;
import com.github.wautsns.easy.oauth2.core.assembly.kernel.exchange.function.api.OAuth2APIExchangeCallbackQueryForUser;
import com.github.wautsns.easy.oauth2.core.assembly.kernel.exchange.function.api.OAuth2APIExchangeCallbackQueryForUserIdentifier;
import com.github.wautsns.easy.oauth2.core.assembly.kernel.exchange.model.OAuth2CallbackQuery;
import com.github.wautsns.easy.oauth2.core.assembly.kernel.exchange.model.user.AbstractOAuth2User;
import com.github.wautsns.easy.oauth2.core.exception.OAuth2Exception;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Objects;

/**
 * Abstract oauth2 exchanger.
 *
 * @param <A> the actual type of {@link AbstractOAuth2ApplicationProperties}
 * @param <U> the actual type of {@link AbstractOAuth2User}
 * @author wautsns
 * @since May 10, 2021
 */
public abstract class AbstractOAuth2Exchanger<A extends AbstractOAuth2ApplicationProperties, U extends AbstractOAuth2User>
        implements OAuth2PlatformSupplier,
                   OAuth2APIExchangeCallbackQueryForUserIdentifier,
                   OAuth2APIExchangeCallbackQueryForUser<U> {

    /** Logger. */
    protected final @NotNull Logger log = LoggerFactory.getLogger(getClass());

    // ##################################################################################

    /** Metadata of {@code this} exchanger. */
    protected final @NotNull OAuth2ExchangerMetadata<A> metadata;

    /** OAuth2 api: exchange callback query for user identifier. */
    private final @NotNull OAuth2APIExchangeCallbackQueryForUserIdentifier exchangeCallbackQueryForUserIdentifier;
    /** OAuth2 api: exchange callback query for user. */
    private final @NotNull OAuth2APIExchangeCallbackQueryForUser<U> exchangeCallbackQueryForUser;

    // ##################################################################################
    // #################### enhanced getter #############################################
    // ##################################################################################

    @Override
    public final @NotNull String platform() {
        return metadata.platform();
    }

    /**
     * Return identifier.
     *
     * @return identifier, for more details see {@link OAuth2ExchangerMetadata#identifier()}
     */
    public final @NotNull String identifier() {
        return metadata.identifier();
    }

    // ##################################################################################
    // #################### oauth2 api ##################################################
    // ##################################################################################

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

    // ##################################################################################
    // #################### constructor #################################################
    // ##################################################################################

    /**
     * Construct an instance.
     *
     * @param metadata metadata
     */
    protected AbstractOAuth2Exchanger(@NotNull OAuth2ExchangerMetadata<A> metadata) {
        this.metadata = Objects.requireNonNull(metadata);
        this.exchangeCallbackQueryForUserIdentifier = Objects.requireNonNull(
                initializeAPIExchangeCallbackQueryForUserIdentifier()
        );
        this.exchangeCallbackQueryForUser = Objects.requireNonNull(initializeAPIExchangeCallbackQueryForUser());
    }

    // ##################################################################################
    // #################### initialize oauth2 api #######################################
    // ##################################################################################

    /**
     * Initialize oauth2 api: exchange callback query for user identifier.
     *
     * @return oauth2 api: exchange callback query for user identifier
     */
    protected abstract @NotNull OAuth2APIExchangeCallbackQueryForUserIdentifier initializeAPIExchangeCallbackQueryForUserIdentifier();

    /**
     * Initialize oauth2 api: exchange callback query for user.
     *
     * @return oauth2 api: exchange callback query for user
     */
    protected abstract @NotNull OAuth2APIExchangeCallbackQueryForUser<U> initializeAPIExchangeCallbackQueryForUser();

}
