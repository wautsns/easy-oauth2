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
package com.github.wautsns.easy.oauth2.extension.client.builtin;

import com.github.wautsns.easy.oauth2.core.client.configuration.OAuth2PlatformSupplier;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Builtin oauth2 platform.
 *
 * @author wautsns
 * @since May 08, 2021
 */
public enum BuiltinOAuth2Platform {

    /**
     * Gitee.
     *
     * @see <a href="https://gitee.com/">Official Site</a>
     * @see <a href="https://gitee.com/api/v5/oauth_doc">OAuth2 Doc</a>
     */
    GITEE("gitee"),

    /**
     * GitHub.
     *
     * @see <a href="https://github.com/">Official Site</a>
     * @see <a href="https://docs.github.com/en/developers/apps/building-oauth-apps/authorizing-oauth-apps">
     *         OAuth2 Doc</a>
     */
    GITHUB("github"),
    ;

    // ##################################################################################

    /** {@link OAuth2PlatformSupplier#platform() Platform identifier}. */
    private final @NotNull String identifier;

    // ##################################################################################
    // #################### enhanced getter #############################################
    // ##################################################################################

    /**
     * Return platform identifier.
     *
     * @return {@link #identifier platform identifier}
     */
    public @NotNull String identifier() {
        return identifier;
    }

    // ##################################################################################
    // #################### constructor #################################################
    // ##################################################################################

    /**
     * Construct an instance.
     *
     * @param identifier {@link #identifier platform identifier}
     */
    BuiltinOAuth2Platform(@NotNull String identifier) {
        this.identifier = Objects.requireNonNull(identifier);
    }

}
