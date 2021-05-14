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
package com.github.wautsns.easy.oauth2.core.client.kernel.authorize.configuration;

import com.github.wautsns.easy.oauth2.core.client.configuration.AbstractOAuth2ApplicationProperties;
import com.github.wautsns.easy.oauth2.core.client.configuration.OAuth2PlatformSupplier;
import com.github.wautsns.easy.oauth2.core.client.kernel.authorize.AbstractOAuth2AuthorizeURLInitializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.Objects;

/**
 * OAuth2 authorize url initializer metadata.
 *
 * @param <A> the actual type of {@link AbstractOAuth2ApplicationProperties}
 * @param <O> the actual type of {@link AbstractOAuth2AuthorizationProperties}
 * @author wautsns
 * @since May 10, 2021
 */
public final class OAuth2AuthorizeURLInitializerMetadata<A extends AbstractOAuth2ApplicationProperties, O extends AbstractOAuth2AuthorizationProperties>
        implements OAuth2PlatformSupplier {

    /**
     * Identifier that is used to distinguish between different functional {@link AbstractOAuth2AuthorizeURLInitializer
     * authorizeURLInitializer} in the same platform (repeatable between different platforms).
     */
    private final @NotNull String identifier;
    /** Application properties. */
    private final @NotNull A applicationProperties;
    /** Authorization properties. */
    private final @NotNull O authorizationProperties;

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
     * <li>The identifier is used to distinguish between different functional {@link
     * AbstractOAuth2AuthorizeURLInitializer authorizeURLInitializer} in the same platform.</li>
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
     * Return authorization properties.
     *
     * @return authorization properties
     */
    public @NotNull O authorizationProperties() {
        return authorizationProperties;
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
     * #OAuth2AuthorizeURLInitializerMetadata(String, AbstractOAuth2ApplicationProperties, AbstractOAuth2AuthorizationProperties)
     * new OAuth2AuthorizeURLInitializerMetadata(null, applicationProperties, authorizationProperties)}.</li>
     * </ul>
     *
     * @param applicationProperties application properties
     * @param authorizationProperties authorization properties
     */
    public OAuth2AuthorizeURLInitializerMetadata(@NotNull A applicationProperties, @NotNull O authorizationProperties) {
        this(null, applicationProperties, authorizationProperties);
    }

    /**
     * Construct an instance.
     *
     * <ul>
     * <li style="list-style-type:none">########## Notes ###############</li>
     * <li>If the {@code identifier} is {@code null}, it will be initialized as platform.</li>
     * <li>The {@code applicationProperties} and {@code authorizationProperties} must be suitable for the same platform,
     * and they will be automatically validated through method {@code validate()}.</li>
     * </ul>
     *
     * @param identifier identifier, for more details see {@link #identifier()}
     * @param applicationProperties application properties
     * @param authorizationProperties authorization properties
     */
    public OAuth2AuthorizeURLInitializerMetadata(
            @Nullable String identifier, @NotNull A applicationProperties, @NotNull O authorizationProperties) {
        this.identifier = (identifier != null) ? identifier : applicationProperties.platform();
        this.applicationProperties = Objects.requireNonNull(applicationProperties);
        this.authorizationProperties = Objects.requireNonNull(authorizationProperties);
        Objects.requireNonNull(applicationProperties.platform());
        Objects.requireNonNull(authorizationProperties.platform());
        if (!applicationProperties.platform().equals(authorizationProperties.platform())) {
            throw new IllegalArgumentException(
                    String.format(
                            "Application properties and authorization properties must be suitable for the same" +
                                    " platform. identifier: `%s`, application.platform: `%s`," +
                                    " authorization.platform: `%s`",
                            identifier, applicationProperties.platform(), authorizationProperties.platform()
                    )
            );
        }
        this.applicationProperties.validate();
        this.authorizationProperties.validate();
    }

}
