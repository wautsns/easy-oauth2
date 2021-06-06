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
package com.github.wautsns.easy.oauth2.extension.client.builtin.gitee.kernel.exchange;

import com.github.wautsns.easy.oauth2.core.client.kernel.exchange.AbstractTokenRefreshableOAuth2Exchanger;
import com.github.wautsns.easy.oauth2.core.client.kernel.exchange.configuration.OAuth2ExchangerMetadata;
import com.github.wautsns.easy.oauth2.core.client.kernel.exchange.function.api.OAuth2APIExchangeCallbackQueryForToken;
import com.github.wautsns.easy.oauth2.core.client.kernel.exchange.function.api.OAuth2APIExchangeCallbackQueryForUser;
import com.github.wautsns.easy.oauth2.core.client.kernel.exchange.function.api.OAuth2APIExchangeCallbackQueryForUserIdentifier;
import com.github.wautsns.easy.oauth2.core.client.kernel.exchange.function.api.OAuth2APIExchangeTokenForUser;
import com.github.wautsns.easy.oauth2.core.client.kernel.exchange.function.api.OAuth2APIExchangeTokenForUserIdentifier;
import com.github.wautsns.easy.oauth2.core.client.kernel.exchange.function.api.OAuth2APIRefreshToken;
import com.github.wautsns.easy.oauth2.core.exception.OAuth2Exception;
import com.github.wautsns.easy.oauth2.core.exception.specific.OAuth2AccessTokenExpiredException;
import com.github.wautsns.easy.oauth2.core.exception.specific.OAuth2RefreshTokenExpiredException;
import com.github.wautsns.easy.oauth2.core.request.model.basic.OAuth2Headers;
import com.github.wautsns.easy.oauth2.core.request.model.basic.OAuth2URL;
import com.github.wautsns.easy.oauth2.core.request.model.request.OAuth2Request;
import com.github.wautsns.easy.oauth2.core.request.model.request.OAuth2RequestMethod;
import com.github.wautsns.easy.oauth2.core.request.model.response.AbstractOAuth2Response;
import com.github.wautsns.easy.oauth2.extension.client.builtin.gitee.configuration.GiteeOAuth2ApplicationProperties;
import com.github.wautsns.easy.oauth2.extension.client.builtin.gitee.kernel.exchange.model.GiteeOAuth2Token;
import com.github.wautsns.easy.oauth2.extension.client.builtin.gitee.kernel.exchange.model.GiteeOAuth2User;

import com.fasterxml.jackson.databind.JsonNode;
import org.jetbrains.annotations.NotNull;

/**
 * Gitee oauth2 exchanger.
 *
 * @author wautsns
 * @since May 11, 2021
 */
public final class GiteeOAuth2Exchanger
        extends AbstractTokenRefreshableOAuth2Exchanger<GiteeOAuth2ApplicationProperties, GiteeOAuth2Token, GiteeOAuth2User> {

    // ##################################################################################
    // #################### initialize oauth2 api #######################################
    // ##################################################################################

    @Override
    protected @NotNull OAuth2APIExchangeCallbackQueryForUserIdentifier initializeAPIExchangeCallbackQueryForUserIdentifier() {
        return query -> exchangeForUser(query).identifier();
    }

    @Override
    protected @NotNull OAuth2APIExchangeCallbackQueryForUser<GiteeOAuth2User> initializeAPIExchangeCallbackQueryForUser() {
        return query -> exchangeForUser(exchangeForToken(query));
    }

    @Override
    protected @NotNull OAuth2APIExchangeCallbackQueryForToken<GiteeOAuth2Token> initializeAPIExchangeCallbackQueryForToken() {
        String urlWithoutQueryAndAnchor = "https://gitee.com/oauth/token";
        OAuth2URL url = new OAuth2URL(urlWithoutQueryAndAnchor, 5);
        OAuth2Request<?> template = new OAuth2Request<>(OAuth2RequestMethod.POST, url);
        template.url().query()
                .unique("grant_type", "authorization_code")
                .unique("client_id", metadata.applicationProperties().getClientId())
                .unique("client_secret", metadata.applicationProperties().getClientSecret())
                .repeatable("redirect_uri", metadata.applicationProperties().getCallbacks());
        template.headers(new OAuth2Headers(1).userAgentEasyOAuth2());
        return query -> {
            OAuth2Request<?> request = template.copy(false, true, true);
            request.url().query().unique("code", query.code());
            AbstractOAuth2Response response = metadata.requestExecutor().execute(request);
            JsonNode root = response.readJSONBodyInputStreamAsTree();
            String error = root.path("error").asText(null);
            if (error != null) {
                throw new OAuth2Exception(root.toString());
            } else {
                return new GiteeOAuth2Token(root);
            }
        };
    }

    @Override
    protected @NotNull OAuth2APIExchangeTokenForUserIdentifier<GiteeOAuth2Token> initializeAPIExchangeTokenForUserIdentifierWithoutRefreshingExpiredToken() {
        return token -> exchangeForUser(token).identifier();
    }

    @Override
    protected @NotNull OAuth2APIExchangeTokenForUser<GiteeOAuth2Token, GiteeOAuth2User> initializeAPIExchangeTokenForUserWithoutRefreshingExpiredToken() {
        String urlWithoutQueryAndAnchor = "https://gitee.com/api/v5/user";
        OAuth2URL url = new OAuth2URL(urlWithoutQueryAndAnchor, 1);
        OAuth2Request<?> template = new OAuth2Request<>(OAuth2RequestMethod.GET, url);
        template.headers(new OAuth2Headers(1).userAgentEasyOAuth2());
        return token -> {
            OAuth2Request<?> request = template.copy(false, true, true);
            request.url().query().unique("access_token", token.accessToken());
            AbstractOAuth2Response response = metadata.requestExecutor().execute(request);
            JsonNode root = response.readJSONBodyInputStreamAsTree();
            if (response.status() < 300) {
                return new GiteeOAuth2User(root);
            } else {
                String message = root.path("message").asText(null);
                if ("401 Unauthorized: Access token is expired".equals(message)) {
                    throw new OAuth2AccessTokenExpiredException(root.toString());
                } else {
                    throw new OAuth2Exception(root.toString());
                }
            }
        };
    }

    @Override
    protected @NotNull OAuth2APIRefreshToken<GiteeOAuth2Token> initializeRefreshToken() {
        String urlWithoutQueryAndAnchor = "https://gitee.com/oauth/token";
        OAuth2URL url = new OAuth2URL(urlWithoutQueryAndAnchor, 2);
        OAuth2Request<?> template = new OAuth2Request<>(OAuth2RequestMethod.POST, url);
        template.url().query().unique("grant_type", "refresh_token");
        template.headers(new OAuth2Headers(1).userAgentEasyOAuth2());
        return token -> {
            OAuth2Request<?> request = template.copy(false, true, true);
            request.url().query().unique("refresh_token", token.refreshToken());
            AbstractOAuth2Response response = metadata.requestExecutor().execute(request);
            JsonNode root = response.readJSONBodyInputStreamAsTree();
            String error = root.path("error").asText(null);
            if (error == null) {
                return new GiteeOAuth2Token(root);
            } else if ("invalid_grant".equals(error)) {
                throw new OAuth2RefreshTokenExpiredException(root.toString());
            } else {
                throw new OAuth2Exception(root.toString());
            }
        };
    }

    // ##################################################################################
    // #################### constructor #################################################
    // ##################################################################################

    /**
     * Construct an instance.
     *
     * @param metadata metadata
     */
    public GiteeOAuth2Exchanger(
            @NotNull OAuth2ExchangerMetadata<GiteeOAuth2ApplicationProperties> metadata) {
        super(metadata);
    }

}
