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
package com.github.wautsns.easy.oauth2.core.client.configuration;

import com.github.wautsns.easy.oauth2.core.request.executor.AbstractOAuth2RequestExecutor;
import org.jetbrains.annotations.NotNull;
import java.util.Objects;

/**
 * OAuth2 client metadata.
 *
 * @param <A> the type of oauth2 application properties
 * @param <O> the type of oauth2 authorization properties
 * @author wautsns
 * @since Apr 02, 2021
 */
public final class OAuth2ClientMetadata<A extends AbstractOAuth2ApplicationProperties, O extends AbstractOAuth2AuthorizationProperties> {

    /** Identifier. */
    private final @NotNull String identifier;
    /** Application properties. */
    private final @NotNull A application;
    /** Authorization properties. */
    private final @NotNull O authorization;
    /** Request executor. */
    private final @NotNull AbstractOAuth2RequestExecutor<?> requestExecutor;

    // ######################################################################################
    // #################### enhanced getter #################################################
    // ######################################################################################

    /**
     * Return identifier.
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
    public @NotNull A application() {
        return application;
    }

    /**
     * Return authorization properties.
     *
     * @return authorization properties
     */
    public @NotNull O authorization() {
        return authorization;
    }

    /**
     * Return request executor.
     *
     * @return request executor
     */
    public @NotNull AbstractOAuth2RequestExecutor<?> requestExecutor() {
        return requestExecutor;
    }

    // ######################################################################################
    // #################### constructor #####################################################
    // ######################################################################################

    /**
     * Construct an instance.
     *
     * @param identifier identifier
     * @param application application properties
     * @param authorization authorization properties
     * @param requestExecutor request executor
     */
    public OAuth2ClientMetadata(
            @NotNull String identifier,
            @NotNull A application,
            @NotNull O authorization,
            @NotNull AbstractOAuth2RequestExecutor<?> requestExecutor) {
        this.identifier = Objects.requireNonNull(identifier);
        this.application = Objects.requireNonNull(application);
        this.authorization = Objects.requireNonNull(authorization);
        this.requestExecutor = Objects.requireNonNull(requestExecutor);
    }

}
