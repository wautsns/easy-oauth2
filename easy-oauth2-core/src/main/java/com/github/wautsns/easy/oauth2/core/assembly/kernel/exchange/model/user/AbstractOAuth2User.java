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
package com.github.wautsns.easy.oauth2.core.assembly.kernel.exchange.model.user;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.wautsns.easy.oauth2.core.assembly.configuration.OAuth2PlatformSupplier;
import org.jetbrains.annotations.NotNull;

/**
 * Abstract oauth2 user.
 *
 * @author wautsns
 * @implNote Typically, an oauth2 user contains {@code identifier} and other properties. However, these
 *         properties may vary greatly from platform to platform, so for ease of understanding, they are obtained from
 *         the {@code raw} by the implementation class.
 * @since Mar 31, 2021
 */
public abstract class AbstractOAuth2User implements OAuth2PlatformSupplier {

    /** Raw user. */
    protected final @NotNull JsonNode raw;

    // ##################################################################################
    // #################### enhanced getter #############################################
    // ##################################################################################

    /**
     * Return raw user.
     *
     * @return raw user
     */
    public final @NotNull JsonNode raw() {
        return raw;
    }

    // ##################################################################################

    /**
     * Return identifier.
     *
     * @return identifier
     */
    public abstract @NotNull String identifier();

    // ##################################################################################
    // #################### constructor #################################################
    // ##################################################################################

    /**
     * Construct an instance.
     *
     * @param raw raw user
     */
    protected AbstractOAuth2User(@NotNull JsonNode raw) {
        this.raw = raw;
    }

}
