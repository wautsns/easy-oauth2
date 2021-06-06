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
package com.github.wautsns.easy.oauth2.extension.client.builtin.github.kernel.exchange;

import com.github.wautsns.easy.oauth2.core.client.kernel.exchange.AbstractTokenAvailableOAuth2Exchanger;
import com.github.wautsns.easy.oauth2.core.client.kernel.exchange.configuration.OAuth2ExchangerMetadata;
import com.github.wautsns.easy.oauth2.core.client.kernel.exchange.function.api.OAuth2APIExchangeCallbackQueryForToken;
import com.github.wautsns.easy.oauth2.core.client.kernel.exchange.function.api.OAuth2APIExchangeCallbackQueryForUser;
import com.github.wautsns.easy.oauth2.core.client.kernel.exchange.function.api.OAuth2APIExchangeCallbackQueryForUserIdentifier;
import com.github.wautsns.easy.oauth2.core.client.kernel.exchange.function.api.OAuth2APIExchangeTokenForUser;
import com.github.wautsns.easy.oauth2.core.client.kernel.exchange.function.api.OAuth2APIExchangeTokenForUserIdentifier;
import com.github.wautsns.easy.oauth2.core.exception.OAuth2Exception;
import com.github.wautsns.easy.oauth2.core.exception.specific.OAuth2AccessTokenExpiredException;
import com.github.wautsns.easy.oauth2.core.exception.specific.OAuth2UserDeniedAuthorizationException;
import com.github.wautsns.easy.oauth2.core.request.model.basic.OAuth2Headers;
import com.github.wautsns.easy.oauth2.core.request.model.basic.OAuth2URL;
import com.github.wautsns.easy.oauth2.core.request.model.request.OAuth2Request;
import com.github.wautsns.easy.oauth2.core.request.model.request.OAuth2RequestMethod;
import com.github.wautsns.easy.oauth2.core.request.model.response.AbstractOAuth2Response;
import com.github.wautsns.easy.oauth2.extension.client.builtin.github.configuration.GitHubOAuth2ApplicationProperties;
import com.github.wautsns.easy.oauth2.extension.client.builtin.github.kernel.exchange.model.GitHubOAuth2Token;
import com.github.wautsns.easy.oauth2.extension.client.builtin.github.kernel.exchange.model.GitHubOAuth2User;

import com.fasterxml.jackson.databind.JsonNode;
import org.jetbrains.annotations.NotNull;

/**
 * GitHub oauth2 exchanger.
 *
 * @author wautsns
 * @since May 11, 2021
 */
public final class GitHubOAuth2Exchanger
        extends AbstractTokenAvailableOAuth2Exchanger<GitHubOAuth2ApplicationProperties, GitHubOAuth2Token, GitHubOAuth2User> {

    // ##################################################################################
    // #################### initialize oauth2 api #######################################
    // ##################################################################################

    @Override
    protected @NotNull OAuth2APIExchangeCallbackQueryForUserIdentifier initializeAPIExchangeCallbackQueryForUserIdentifier() {
        return query -> exchangeForUser(query).identifier();
    }

    @Override
    protected @NotNull OAuth2APIExchangeCallbackQueryForUser<GitHubOAuth2User> initializeAPIExchangeCallbackQueryForUser() {
        return query -> exchangeForUser(exchangeForToken(query));
    }

    @Override
    protected @NotNull OAuth2APIExchangeCallbackQueryForToken<GitHubOAuth2Token> initializeAPIExchangeCallbackQueryForToken() {
        String urlWithoutQueryAndAnchor = "https://github.com/login/oauth/access_token";
        OAuth2URL url = new OAuth2URL(urlWithoutQueryAndAnchor, 3);
        OAuth2Request<?> template = new OAuth2Request<>(OAuth2RequestMethod.POST, url);
        template.url().query()
                .unique("client_id", metadata.applicationProperties().getClientId())
                .unique("client_secret", metadata.applicationProperties().getClientSecret());
        template.headers(new OAuth2Headers(1).acceptJSON());
        return query -> {
            String code = query.code();
            if (code == null) {
                String error = query.raw().path("error").asText(null);
                if ("access_denied".equals(error)) {
                    throw new OAuth2UserDeniedAuthorizationException(query.raw().toString());
                } else {
                    throw new OAuth2Exception("Authorize code does not exist.");
                }
            } else {
                OAuth2Request<?> request = template.copy(false, true, true);
                request.url().query().unique("code", code);
                AbstractOAuth2Response response = metadata.requestExecutor().execute(request);
                JsonNode root = response.readJSONBodyInputStreamAsTree();
                String error = root.path("error").asText(null);
                if (error == null) {
                    return new GitHubOAuth2Token(root);
                } else {
                    throw new OAuth2Exception(root.toString());
                }
            }
        };
    }

    /**
     * Initialize oauth2 api: exchange token for user identifier.
     *
     * @return oauth2 api: exchange token for user identifier
     */
    @Override
    protected @NotNull OAuth2APIExchangeTokenForUserIdentifier<GitHubOAuth2Token> initializeAPIExchangeTokenForUserIdentifier() {
        return token -> exchangeForUser(token).identifier();
    }

    /**
     * Initialize oauth2 api: exchange token for user.
     *
     * @return oauth2 api: exchange token for user
     */
    @Override
    protected @NotNull OAuth2APIExchangeTokenForUser<GitHubOAuth2Token, GitHubOAuth2User> initializeAPIExchangeTokenForUser() {
        String urlWithoutQueryAndAnchor = "https://api.github.com/user";
        OAuth2URL url = new OAuth2URL(urlWithoutQueryAndAnchor, 0);
        OAuth2Request<?> template = new OAuth2Request<>(OAuth2RequestMethod.GET, url);
        return token -> {
            OAuth2Request<?> request = template.copy(true, false, true);
            request.headers(new OAuth2Headers(1).authorization("token", token.accessToken()));
            AbstractOAuth2Response response = metadata.requestExecutor().execute(request);
            JsonNode root = response.readJSONBodyInputStreamAsTree();
            if (response.status() < 300) {
                return new GitHubOAuth2User(root);
            } else {
                String message = root.path("message").asText(null);
                if ("Bad credentials".equals(message)) {
                    throw new OAuth2AccessTokenExpiredException(root.toString());
                } else {
                    throw new OAuth2Exception(root.toString());
                }
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
    public GitHubOAuth2Exchanger(
            @NotNull OAuth2ExchangerMetadata<GitHubOAuth2ApplicationProperties> metadata) {
        super(metadata);
    }

}
