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
import com.github.wautsns.easy.oauth2.core.client.kernel.authorize.AbstractOAuth2AuthorizeURLInitializer;
import com.github.wautsns.easy.oauth2.core.client.kernel.authorize.configuration.AbstractOAuth2AuthorizationProperties;
import com.github.wautsns.easy.oauth2.core.client.kernel.authorize.configuration.OAuth2AuthorizeURLInitializerMetadata;
import com.github.wautsns.easy.oauth2.core.client.kernel.exchange.AbstractOAuth2Exchanger;
import com.github.wautsns.easy.oauth2.core.client.kernel.exchange.configuration.OAuth2ExchangerMetadata;
import com.github.wautsns.easy.oauth2.core.client.kernel.exchange.model.user.AbstractOAuth2User;
import com.github.wautsns.easy.oauth2.core.exception.OAuth2Exception;
import com.github.wautsns.easy.oauth2.core.exception.OAuth2IOException;
import com.github.wautsns.easy.oauth2.core.request.model.basic.OAuth2URL;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * OAuth2 client.
 *
 * @author wautsns
 * @since May 13, 2021
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public final class OAuth2Client {

    /** Logger. */
    private static final @NotNull Logger log = LoggerFactory.getLogger(OAuth2Client.class);

    // ##################################################################################

    /**
     * Authorize url initializer group by {@link OAuth2AuthorizeURLInitializerMetadata#platform()
     * platform} and {@link OAuth2AuthorizeURLInitializerMetadata#identifier() identifier}.
     */
    private static final @NotNull Map<@NotNull String, @NotNull Map<@NotNull String, @NotNull AbstractOAuth2AuthorizeURLInitializer>> AUTHORIZE_URL_INITIALIZERS =
            new ConcurrentHashMap<>();
    /**
     * Exchanger group by {@link OAuth2ExchangerMetadata#platform() platform} and {@link
     * OAuth2ExchangerMetadata#identifier() identifier}.
     */
    private static final @NotNull Map<@NotNull String, @NotNull Map<@NotNull String, @NotNull AbstractOAuth2Exchanger>> EXCHANGERS =
            new ConcurrentHashMap<>();

    // ##################################################################################
    // #################### enhanced getter #############################################
    // ##################################################################################

    /**
     * Return authorize url for the given {@code platform}.
     *
     * <ul>
     * <li style="list-style-type:none">########## Notes ###############</li>
     * <li>The method equals to {@link #authorizeURL(String, String, String)
     * authorizeURL}({@code platform}, {@code null}, {@code state}).</li>
     * </ul>
     *
     * @param platform platform
     * @param state state
     * @return authorize url
     * @throws OAuth2IOException if an oauth2 related error occurs
     */
    public static @NotNull OAuth2URL authorizeURL(@NotNull String platform, @Nullable String state)
            throws OAuth2Exception {
        return authorizeURL(platform, null, state);
    }

    /**
     * Return authorize url for the given {@code platform} and the {@code identifier}.
     *
     * <ul>
     * <li style="list-style-type:none">########## Notes ###############</li>
     * <li>If the {@code identifier} is {@code null}, it will be initialized as the {@code
     * platform}.</li>
     * <li>If there is no such authorize url initializer, an {@link IllegalStateException} will be
     * thrown.</li>
     * </ul>
     *
     * @param platform platform
     * @param identifier identifier, for more details see {@link
     *         OAuth2AuthorizeURLInitializerMetadata#identifier()}
     * @param state state
     * @return authorize url
     * @throws OAuth2IOException if an oauth2 related error occurs
     */
    public static @NotNull OAuth2URL authorizeURL(
            @NotNull String platform, @Nullable String identifier, @Nullable String state)
            throws OAuth2Exception {
        Map<String, AbstractOAuth2AuthorizeURLInitializer> initializerGroupByIdentifier =
                AUTHORIZE_URL_INITIALIZERS.get(platform);
        if (initializerGroupByIdentifier == null) {
            throw new IllegalStateException(String.format(
                    "There is no such authorize url initializer. platform: %s", platform
            ));
        }
        identifier = (identifier == null) ? platform : identifier;
        AbstractOAuth2AuthorizeURLInitializer initializer =
                initializerGroupByIdentifier.get(identifier);
        if (initializer != null) {
            return initializer.initializeAuthorizeURL(state);
        } else {
            throw new IllegalStateException(String.format(
                    "There is no such authorize url initializer. platform: %s, identifier: %s",
                    platform, identifier
            ));
        }
    }

    // ##################################################################################

    /**
     * Return exchanger for the given {@code platform}.
     *
     * <ul>
     * <li style="list-style-type:none">########## Notes ###############</li>
     * <li>The method equals to {@link #exchanger(String, String) exchanger}({@code platform},
     * {@code null}).</li>
     * </ul>
     *
     * @param platform platform
     * @return exchanger
     */
    public static @NotNull AbstractOAuth2Exchanger<?, ?> exchanger(@NotNull String platform) {
        return exchanger(platform, null);
    }

    /**
     * Return exchanger for the given {@code platform} and the {@code identifier}.
     *
     * <ul>
     * <li style="list-style-type:none">########## Notes ###############</li>
     * <li>If the {@code identifier} is {@code null}, it will be initialized as the {@code
     * platform}.</li>
     * <li>If there is no such exchanger, an {@link IllegalStateException} will be thrown.</li>
     * </ul>
     *
     * @param platform platform
     * @param identifier identifier, for more details see {@link
     *         OAuth2ExchangerMetadata#identifier()}
     * @return exchanger
     */
    public static @NotNull AbstractOAuth2Exchanger<?, ?> exchanger(
            @NotNull String platform, @Nullable String identifier) {
        Map<String, AbstractOAuth2Exchanger> exchangerGroupByIdentifier = EXCHANGERS.get(platform);
        if (exchangerGroupByIdentifier == null) {
            throw new IllegalStateException(String.format(
                    "There is no such exchanger. platform: %s", platform
            ));
        }
        identifier = (identifier == null) ? platform : identifier;
        AbstractOAuth2Exchanger exchanger = exchangerGroupByIdentifier.get(identifier);
        if (exchanger != null) {
            return exchanger;
        } else {
            throw new IllegalStateException(String.format(
                    "There is no such exchanger. platform: %s, identifier: %s",
                    platform, identifier
            ));
        }
    }

    // ##################################################################################
    // #################### enhanced setter #############################################
    // ##################################################################################

    /**
     * Register the given {@code authorizeURLInitializer}.
     *
     * @param authorizeURLInitializer exchanger
     * @param <I> the actual type of {@link AbstractOAuth2AuthorizeURLInitializer}
     * @param <A> the actual type of {@link AbstractOAuth2ApplicationProperties}
     * @param <O> the actual type of {@link AbstractOAuth2AuthorizationProperties}
     * @return the previous, or {@code null} if not exists
     */
    public static <I extends AbstractOAuth2AuthorizeURLInitializer<A, O>, A extends AbstractOAuth2ApplicationProperties, O extends AbstractOAuth2AuthorizationProperties> @Nullable I register(
            @NotNull I authorizeURLInitializer) {
        String platform = authorizeURLInitializer.platform();
        Map<String, AbstractOAuth2AuthorizeURLInitializer> initializerGroupByIdentifier =
                AUTHORIZE_URL_INITIALIZERS.computeIfAbsent(platform, ignored -> new ConcurrentHashMap<>(4));
        String identifier = authorizeURLInitializer.identifier();
        I previous = (I) initializerGroupByIdentifier.put(identifier, authorizeURLInitializer);
        if (previous == null) {
            log.info(
                    "An authorize url initializer has been registered. platform: {}," +
                            " identifier: {}",
                    platform, identifier
            );
        } else {
            log.warn(
                    "The previous authorize url initializer has been replaced. platform: {}," +
                            " identifier: {}, current.hash: {}, previous.hash: {}",
                    platform, identifier, authorizeURLInitializer.hashCode(), previous.hashCode()
            );
        }
        return previous;
    }

    /**
     * Register the given {@code exchanger}.
     *
     * @param exchanger exchanger
     * @param <E> the actual type of {@link AbstractOAuth2Exchanger}
     * @param <A> the actual type of {@link AbstractOAuth2ApplicationProperties}
     * @param <U> the actual type of {@link AbstractOAuth2User}
     * @return the previous, or {@code null} if not exists
     */
    public static <E extends AbstractOAuth2Exchanger<A, U>, A extends AbstractOAuth2ApplicationProperties, U extends AbstractOAuth2User> @Nullable E register(
            @NotNull E exchanger) {
        String platform = exchanger.platform();
        Map<String, AbstractOAuth2Exchanger> exchangerGroupByIdentifier =
                EXCHANGERS.computeIfAbsent(platform, ignored -> new ConcurrentHashMap<>(4));
        String identifier = exchanger.identifier();
        E previous = (E) exchangerGroupByIdentifier.put(identifier, exchanger);
        if (previous == null) {
            log.info(
                    "An exchanger has been registered. platform: {}, identifier: {}",
                    platform, identifier
            );
        } else {
            log.warn(
                    "The previous exchanger has been replaced. platform: {}, identifier: {}," +
                            " current.hash: {}, previous.hash: {}",
                    platform, identifier, exchanger.hashCode(), previous.hashCode()
            );
        }
        return previous;
    }

    // ##################################################################################

    /** Static Assembly. */
    private OAuth2Client() {}

}
