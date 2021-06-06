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
package com.github.wautsns.easy.oauth2.core.client.kernel.exchange.model.token;

import com.fasterxml.jackson.databind.JsonNode;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;

/**
 * Abstract refreshable oauth2 token.
 *
 * @author wautsns
 * @implNote Typically, an oauth2 token contains {@code access-token}, {@code
 *         access-token-validity-duration}, {@code refresh-token}, {@code
 *         refresh-token-validity-duration}, etc. However, the names of these properties may vary
 *         greatly from platform to platform, so for ease of understanding, they are obtained from
 *         the {@code raw} by the implementation class.
 * @since Apr 22, 2021
 */
public abstract class AbstractRefreshableOAuth2Token extends AbstractOAuth2Token {

    // ##################################################################################
    // #################### enhanced getter #############################################
    // ##################################################################################

    /**
     * Return refresh token.
     *
     * @return refresh token
     */
    public abstract @NotNull String refreshToken();

    /**
     * Return refresh token validity duration.
     *
     * @return refresh token validity duration
     */
    public abstract @NotNull Duration refreshTokenValidityDuration();

    // ##################################################################################
    // #################### constructor #################################################
    // ##################################################################################

    /**
     * Construct an instance.
     *
     * @param raw {@link #raw}
     */
    protected AbstractRefreshableOAuth2Token(@NotNull JsonNode raw) {
        super(raw);
    }

}
