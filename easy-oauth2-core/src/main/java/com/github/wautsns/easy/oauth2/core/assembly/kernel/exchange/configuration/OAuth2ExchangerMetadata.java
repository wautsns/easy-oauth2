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
package com.github.wautsns.easy.oauth2.core.assembly.kernel.exchange.configuration;

import com.github.wautsns.easy.oauth2.core.assembly.configuration.AbstractOAuth2ApplicationProperties;
import com.github.wautsns.easy.oauth2.core.assembly.configuration.OAuth2PlatformSupplier;
import com.github.wautsns.easy.oauth2.core.request.executor.AbstractOAuth2RequestExecutor;
import com.github.wautsns.easy.oauth2.core.request.executor.OAuth2RequestExecutorFactory;
import com.github.wautsns.easy.oauth2.core.request.executor.OAuth2RequestExecutorFactoryManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.Objects;

/**
 * OAuth2 exchanger metadata.
 *
 * @param <A> the actual type of {@link AbstractOAuth2ApplicationProperties}
 * @author wautsns
 * @since May 10, 2021
 */
public final class OAuth2ExchangerMetadata<A extends AbstractOAuth2ApplicationProperties>
        implements OAuth2PlatformSupplier {

    /**
     * Identifier that is used to distinguish between different {@link AbstractOAuth2ApplicationProperties
     * applicationProperties} in the same platform (repeatable between different platforms).
     */
    private final @NotNull String identifier;
    /** Application properties. */
    private final @NotNull A applicationProperties;
    /** Request executor. */
    private final @NotNull AbstractOAuth2RequestExecutor<?> requestExecutor;

    // ##################################################################################
    // #################### enhanced getter #############################################
    // ##################################################################################

    @Override
    public @NotNull String platform() {
        return applicationProperties.platform();
    }

    /**
     * Return identifier.
     *
     * <ul>
     * <li style="list-style-type:none">########## Notes ###############</li>
     * <li>The identifier is used to distinguish between different {@link AbstractOAuth2ApplicationProperties
     * applicationProperties} in the same platform.</li>
     * <li>The identifier is repeatable between different platforms.</li>
     * </ul>
     *
     * @return identifier
     */
    public @NotNull String identifier() {
        return identifier;
    }

    /**
     * Return application properties.
     *
     * @return application properties
     */
    public @NotNull A applicationProperties() {
        return applicationProperties;
    }

    /**
     * Return request executor.
     *
     * @return request executor
     */
    public @NotNull AbstractOAuth2RequestExecutor<?> requestExecutor() {
        return requestExecutor;
    }

    // ##################################################################################
    // #################### constructor #################################################
    // ##################################################################################

    /**
     * Construct an instance.
     *
     * <ul>
     * <li style="list-style-type:none">########## Notes ###############</li>
     * <li>The constructor equals to {@link
     * #OAuth2ExchangerMetadata(String, AbstractOAuth2ApplicationProperties, AbstractOAuth2RequestExecutor)
     * new OAuth2ExchangerMetadata(null, applicationProperties, null)}.</li>
     * </ul>
     *
     * @param applicationProperties application properties
     */
    public OAuth2ExchangerMetadata(@NotNull A applicationProperties) {
        this(null, applicationProperties, null);
    }

    /**
     * Construct an instance.
     *
     * <ul>
     * <li style="list-style-type:none">########## Notes ###############</li>
     * <li>If the {@code identifier} is {@code null}, it will be initialized as platform.</li>
     * <li>If the {@code requestExecutor} is {@code null}, it will be initialized as {@link
     * OAuth2RequestExecutorFactoryManager#any()}{@link OAuth2RequestExecutorFactory#create() .create()}.</li>
     * <li>The {@code applicationProperties} will be automatically validated through method {@code validate()}.</li>
     * </ul>
     *
     * @param applicationProperties application properties
     * @param requestExecutor request executor
     */
    public OAuth2ExchangerMetadata(
            @Nullable String identifier, @NotNull A applicationProperties,
            @Nullable AbstractOAuth2RequestExecutor<?> requestExecutor) {
        this.identifier = (identifier != null) ? identifier : applicationProperties.platform();
        this.applicationProperties = Objects.requireNonNull(applicationProperties);
        if (requestExecutor != null) {
            this.requestExecutor = requestExecutor;
        } else {
            this.requestExecutor = OAuth2RequestExecutorFactoryManager.any().create();
        }
        this.applicationProperties.validate();
    }

}
