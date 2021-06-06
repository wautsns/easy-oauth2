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
package com.github.wautsns.easy.oauth2.extension.client.builtin.gitee.kernel.exchange.model;

import com.github.wautsns.easy.oauth2.core.client.kernel.exchange.model.token.AbstractRefreshableOAuth2Token;
import com.github.wautsns.easy.oauth2.extension.client.builtin.BuiltinOAuth2Platform;

import com.fasterxml.jackson.databind.JsonNode;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;

/**
 * Gitee oauth2 token.
 *
 * <pre>
 * {
 *     "access_token":"${ACCESS_TOKEN}",
 *     "token_type":"bearer",
 *     "expires_in":86400,
 *     "refresh_token":"${REFRESH_TOKEN}",
 *     "scope":"user_info emails",
 *     "created_at":1620221329
 * }
 * </pre>
 *
 * @author wautsns
 * @since Apr 24, 2021
 */
public final class GiteeOAuth2Token extends AbstractRefreshableOAuth2Token {

    /** FIXME Not tested, temporarily assumed to be one week. */
    private static final @NotNull Duration REFRESH_TOKEN_VALID_TIME = Duration.ofDays(7);

    // ##################################################################################
    // #################### enhanced getter #############################################
    // ##################################################################################

    @Override
    public @NotNull String platform() {
        return BuiltinOAuth2Platform.GITEE.identifier();
    }

    // ##################################################################################

    @Override
    public @NotNull String accessToken() {
        return raw.required("access_token").asText();
    }

    @Override
    public @NotNull Duration accessTokenValidityDuration() {
        return Duration.ofSeconds(raw.required("expires_in").asInt());
    }

    @Override
    public @NotNull String refreshToken() {
        return raw.required("refresh_token").asText();
    }

    @Override
    public @NotNull Duration refreshTokenValidityDuration() {
        return REFRESH_TOKEN_VALID_TIME;
    }

    // ##################################################################################
    // #################### constructor #################################################
    // ##################################################################################

    /**
     * Construct an instance.
     *
     * @param raw {@link #raw}
     */
    public GiteeOAuth2Token(@NotNull JsonNode raw) {
        super(raw);
    }

}
