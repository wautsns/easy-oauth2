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
package com.github.wautsns.easy.oauth2.core.client.model;

import com.fasterxml.jackson.databind.JsonNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.Objects;

/**
 * OAuth2 callback query.
 *
 * @author wautsns
 * @since Mar 31, 2021
 */
public final class OAuth2CallbackQuery {

    /** Raw oauth2 callback query. */
    private final @NotNull JsonNode raw;

    // ######################################################################################
    // #################### enhanced getter #################################################
    // ######################################################################################

    /**
     * Return raw oauth2 callback query.
     *
     * @return raw oauth2 callback query
     */
    public @NotNull JsonNode raw() {
        return raw;
    }

    // ######################################################################################

    /**
     * Return authorization code through field `code`.
     *
     * @return authorization code
     */
    public @Nullable String code() {
        return raw.path("code").asText(null);
    }

    /**
     * Return state through field `state`.
     *
     * @return state
     */
    public @Nullable String state() {
        return raw.path("state").asText(null);
    }

    // ######################################################################################
    // #################### constructor #####################################################
    // ######################################################################################

    /**
     * Construct an instance.
     *
     * @param raw raw oauth2 callback query
     */
    public OAuth2CallbackQuery(@NotNull JsonNode raw) {
        this.raw = Objects.requireNonNull(raw);
    }

}
