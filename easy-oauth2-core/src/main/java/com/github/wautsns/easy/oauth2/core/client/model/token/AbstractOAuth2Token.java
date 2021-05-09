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
package com.github.wautsns.easy.oauth2.core.client.model.token;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.wautsns.easy.oauth2.core.client.configuration.OAuth2PlatformIdentifierSupplier;
import org.jetbrains.annotations.NotNull;
import java.time.Duration;
import java.util.Objects;

/**
 * Abstract oauth2 token.
 *
 * @author wautsns
 * @since Mar 31, 2021
 */
public abstract class AbstractOAuth2Token implements OAuth2PlatformIdentifierSupplier {

    /** Raw oauth2 token. */
    protected final @NotNull JsonNode raw;

    // ######################################################################################
    // #################### enhanced getter #################################################
    // ######################################################################################

    /**
     * Return raw oauth2 token.
     *
     * @return raw oauth2 token
     */
    public final @NotNull JsonNode raw() {
        return raw;
    }

    // ######################################################################################

    /**
     * Return access token.
     *
     * @return access token
     */
    public abstract @NotNull String accessToken();

    /**
     * Return access token valid time.
     *
     * @return access token valid time
     */
    public abstract @NotNull Duration accessTokenValidTime();

    // ######################################################################################
    // #################### constructor #####################################################
    // ######################################################################################

    /**
     * Construct an instance.
     *
     * @param raw raw oauth2 token
     */
    protected AbstractOAuth2Token(@NotNull JsonNode raw) {
        this.raw = Objects.requireNonNull(raw);
    }

}
