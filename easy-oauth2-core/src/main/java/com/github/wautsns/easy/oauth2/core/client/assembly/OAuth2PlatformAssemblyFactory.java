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
package com.github.wautsns.easy.oauth2.core.client.assembly;

import com.github.wautsns.easy.oauth2.core.client.configuration.AbstractOAuth2ApplicationProperties;
import com.github.wautsns.easy.oauth2.core.client.configuration.OAuth2PlatformSupplier;
import com.github.wautsns.easy.oauth2.core.client.kernel.authorize.AbstractOAuth2AuthorizeURLInitializer;
import com.github.wautsns.easy.oauth2.core.client.kernel.authorize.configuration.AbstractOAuth2AuthorizationProperties;
import com.github.wautsns.easy.oauth2.core.client.kernel.authorize.configuration.OAuth2AuthorizeURLInitializerMetadata;
import com.github.wautsns.easy.oauth2.core.client.kernel.exchange.AbstractOAuth2Exchanger;
import com.github.wautsns.easy.oauth2.core.client.kernel.exchange.configuration.OAuth2ExchangerMetadata;
import org.jetbrains.annotations.NotNull;

/**
 * OAuth2 platform assembly factory.
 *
 * @param <A> the actual type of {@link AbstractOAuth2ApplicationProperties}
 * @param <O> the actual type of {@link AbstractOAuth2AuthorizationProperties}
 * @param <I> the actual type of {@link AbstractOAuth2AuthorizeURLInitializer}
 * @param <E> the actual type of {@link AbstractOAuth2Exchanger}
 * @author wautsns
 * @since May 14, 2021
 */
public interface OAuth2PlatformAssemblyFactory<A extends AbstractOAuth2ApplicationProperties, O extends AbstractOAuth2AuthorizationProperties, I extends AbstractOAuth2AuthorizeURLInitializer<A, O>, E extends AbstractOAuth2Exchanger<A, ?>>
        extends OAuth2PlatformSupplier {

    // ##################################################################################
    // #################### initialize ##################################################
    // ##################################################################################

    /**
     * Initialize default authorization properties.
     *
     * @return default authorization properties
     */
    @NotNull O initializeDefaultAuthorizationProperties();

    // ##################################################################################
    // #################### create ######################################################
    // ##################################################################################

    /**
     * Initialize authorize url initializer.
     *
     * @param metadata metadata
     * @return authorize url initializer
     */
    @NotNull I createAuthorizeURLInitializer(@NotNull OAuth2AuthorizeURLInitializerMetadata<A, O> metadata);

    /**
     * Initialize exchanger.
     *
     * @param metadata metadata
     * @return exchanger
     */
    @NotNull E createExchanger(@NotNull OAuth2ExchangerMetadata<A> metadata);

}
