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
package com.github.wautsns.easy.oauth2.core.client.kernel.authorize;

import com.github.wautsns.easy.oauth2.core.client.configuration.AbstractOAuth2ApplicationProperties;
import com.github.wautsns.easy.oauth2.core.client.configuration.OAuth2PlatformSupplier;
import com.github.wautsns.easy.oauth2.core.client.kernel.authorize.configuration.AbstractOAuth2AuthorizationProperties;
import com.github.wautsns.easy.oauth2.core.client.kernel.authorize.configuration.OAuth2AuthorizeURLInitializerMetadata;
import com.github.wautsns.easy.oauth2.core.client.kernel.authorize.function.operation.OAuth2OperationInitializeAuthorizeURL;
import com.github.wautsns.easy.oauth2.core.exception.OAuth2Exception;
import com.github.wautsns.easy.oauth2.core.request.model.basic.OAuth2URL;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * Abstract oauth2 authorize url initializer.
 *
 * @param <A> the actual type of {@link AbstractOAuth2ApplicationProperties}
 * @param <O> the actual type of {@link AbstractOAuth2AuthorizationProperties}
 * @author wautsns
 * @since May 10, 2021
 */
public abstract class AbstractOAuth2AuthorizeURLInitializer<A extends AbstractOAuth2ApplicationProperties, O extends AbstractOAuth2AuthorizationProperties>
        implements OAuth2PlatformSupplier,
                   OAuth2OperationInitializeAuthorizeURL {

    /** Logger. */
    protected final @NotNull Logger log = LoggerFactory.getLogger(getClass());

    // ##################################################################################

    /** Metadata. */
    protected final @NotNull OAuth2AuthorizeURLInitializerMetadata<A, O> metadata;

    /** OAuth2 operation: initialize authorize url. */
    private final @NotNull OAuth2OperationInitializeAuthorizeURL initializeAuthorizeURL;

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
     * @return {@link OAuth2AuthorizeURLInitializerMetadata#identifier() identifier}
     */
    public final @NotNull String identifier() {
        return metadata.identifier();
    }

    // ##################################################################################
    // #################### oauth2 operation ############################################
    // ##################################################################################

    @Override
    public final @NotNull OAuth2URL initializeAuthorizeURL(@Nullable String state)
            throws OAuth2Exception {
        log.debug("Ready to initialize authorize url. state: {}", state);
        try {
            OAuth2URL url = initializeAuthorizeURL.initializeAuthorizeURL(state);
            log.debug("Authorize url has been initialized. state: {}, url: {}", state, url);
            return url;
        } catch (RuntimeException | OAuth2Exception e) {
            log.error("Failed to initialize authorize url. state: {}", state, e);
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
    protected AbstractOAuth2AuthorizeURLInitializer(
            @NotNull OAuth2AuthorizeURLInitializerMetadata<A, O> metadata) {
        this.metadata = Objects.requireNonNull(metadata);
        this.initializeAuthorizeURL =
                Objects.requireNonNull(initializeOperationInitializeAuthorizeURL());
    }

    // ##################################################################################
    // #################### initialize oauth2 operation #################################
    // ##################################################################################

    /**
     * Initialize oauth2 operation: initialize authorize url.
     *
     * @return oauth2 operation: initialize authorize url
     * @see #initializeAuthorizeURL(String)
     */
    protected abstract @NotNull OAuth2OperationInitializeAuthorizeURL initializeOperationInitializeAuthorizeURL();

}
