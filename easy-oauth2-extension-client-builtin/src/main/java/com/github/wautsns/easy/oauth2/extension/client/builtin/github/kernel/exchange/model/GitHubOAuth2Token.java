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
package com.github.wautsns.easy.oauth2.extension.client.builtin.github.kernel.exchange.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.wautsns.easy.oauth2.core.assembly.kernel.exchange.model.token.AbstractOAuth2Token;
import com.github.wautsns.easy.oauth2.extension.client.builtin.BuiltinOAuth2Platform;
import org.jetbrains.annotations.NotNull;
import java.time.Duration;

/**
 * GitHub oauth2 token.
 *
 * <pre>
 * {
 *     "access_token":"${ACCESS_TOKEN}",
 *     "token_type":"bearer",
 *     "scope":"notifications,user:email"
 * }
 * </pre>
 *
 * @author wautsns
 * @since Apr 24, 2021
 */
public final class GitHubOAuth2Token extends AbstractOAuth2Token {

    /** FIXME Not tested, temporarily assumed to be one day. */
    private static final Duration ACCESS_TOKEN_VALID_TIME = Duration.ofDays(1);

    // ##################################################################################
    // #################### enhanced getter #############################################
    // ##################################################################################

    @Override
    public @NotNull String platform() {
        return BuiltinOAuth2Platform.GITHUB.identifier();
    }

    // ##################################################################################

    @Override
    public @NotNull String accessToken() {
        return raw.required("access_token").asText();
    }

    @Override
    public @NotNull Duration accessTokenValidityDuration() {
        return ACCESS_TOKEN_VALID_TIME;
    }

    // ##################################################################################
    // #################### constructor #################################################
    // ##################################################################################

    /**
     * Construct an instance.
     *
     * @param raw raw
     */
    public GitHubOAuth2Token(@NotNull JsonNode raw) {
        super(raw);
    }

}
