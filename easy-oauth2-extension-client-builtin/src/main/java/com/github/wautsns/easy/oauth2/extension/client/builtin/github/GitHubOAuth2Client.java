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
package com.github.wautsns.easy.oauth2.extension.client.builtin.github;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.wautsns.easy.oauth2.core.client.AbstractTokenAvailableOAuth2Client;
import com.github.wautsns.easy.oauth2.core.client.configuration.OAuth2ClientMetadata;
import com.github.wautsns.easy.oauth2.core.client.function.api.OAuth2APIExchangeCallbackQueryForToken;
import com.github.wautsns.easy.oauth2.core.client.function.api.OAuth2APIExchangeCallbackQueryForUser;
import com.github.wautsns.easy.oauth2.core.client.function.api.OAuth2APIExchangeCallbackQueryForUserIdentifier;
import com.github.wautsns.easy.oauth2.core.client.function.api.OAuth2APIExchangeTokenForUser;
import com.github.wautsns.easy.oauth2.core.client.function.api.OAuth2APIExchangeTokenForUserIdentifier;
import com.github.wautsns.easy.oauth2.core.client.function.operation.OAuth2OperationInitializeAuthorizeURL;
import com.github.wautsns.easy.oauth2.core.exception.OAuth2Exception;
import com.github.wautsns.easy.oauth2.core.exception.specific.OAuth2AccessTokenExpiredException;
import com.github.wautsns.easy.oauth2.core.exception.specific.OAuth2UserDeniedAuthorizationException;
import com.github.wautsns.easy.oauth2.core.request.model.basic.OAuth2Headers;
import com.github.wautsns.easy.oauth2.core.request.model.basic.OAuth2URL;
import com.github.wautsns.easy.oauth2.core.request.model.request.OAuth2Request;
import com.github.wautsns.easy.oauth2.core.request.model.request.OAuth2RequestMethod;
import com.github.wautsns.easy.oauth2.core.request.model.response.AbstractOAuth2Response;
import com.github.wautsns.easy.oauth2.core.request.util.OAuth2DataUtils;
import com.github.wautsns.easy.oauth2.extension.client.builtin.github.configuration.GitHubOAuth2ApplicationProperties;
import com.github.wautsns.easy.oauth2.extension.client.builtin.github.configuration.GitHubOAuth2AuthorizationProperties;
import com.github.wautsns.easy.oauth2.extension.client.builtin.github.model.GitHubOAuth2Token;
import com.github.wautsns.easy.oauth2.extension.client.builtin.github.model.GitHubOAuth2User;
import org.jetbrains.annotations.NotNull;

/**
 * GitHub oauth2 client.
 *
 * @author wautsns
 * @see <a href="https://docs.github.com/en/developers/apps/authorizing-oauth-apps">Authorizing OAuth Apps - GitHub
 *         Docs</a>
 * @since Apr 24, 2021
 */
public final class GitHubOAuth2Client extends AbstractTokenAvailableOAuth2Client<GitHubOAuth2ApplicationProperties, GitHubOAuth2AuthorizationProperties, GitHubOAuth2Token, GitHubOAuth2User> {

    // #########################################################################################
    // #################### oauth2 function ####################################################
    // #########################################################################################

    @Override
    protected @NotNull OAuth2OperationInitializeAuthorizeURL initializeOAuth2OperationInitializeAuthorizeURL() {
        String urlWithoutQueryAndAnchor = "https://github.com/login/oauth/authorize";
        OAuth2URL template = new OAuth2URL(urlWithoutQueryAndAnchor, 5);
        template.query()
                .unique("client_id", metadata.application().getClientId())
                .unique("redirect_uri", metadata.application().getAuthorizeCallbackURL());
        metadata.authorization()
                .appendScope(template)
                .appendAllowSignup(template);
        return state -> {
            OAuth2URL url = template.copy();
            url.query().unique("state", state);
            return url;
        };
    }

    // ######################################################################################

    @Override
    protected @NotNull OAuth2APIExchangeCallbackQueryForToken<GitHubOAuth2Token> initializeOAuth2APIExchangeCallbackQueryForToken() {
        String urlWithoutQueryAndAnchor = "https://github.com/login/oauth/access_token";
        OAuth2URL url = new OAuth2URL(urlWithoutQueryAndAnchor, 3);
        OAuth2Request<?> template = new OAuth2Request<>(OAuth2RequestMethod.POST, url);
        template.url().query()
                .unique("client_id", metadata.application().getClientId())
                .unique("client_secret", metadata.application().getClientSecret());
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
                JsonNode root = OAuth2DataUtils.readJSONAsTree(response.bodyInputStream());
                String error = root.path("error").asText(null);
                if (error == null) {
                    return new GitHubOAuth2Token(root);
                } else {
                    throw new OAuth2Exception(root.toString());
                }
            }
        };
    }

    // ######################################################################################

    @Override
    protected @NotNull OAuth2APIExchangeCallbackQueryForUserIdentifier initializeOAuth2APIExchangeCallbackQueryForUserIdentifier() {
        return query -> exchangeForUser(query).identifier();
    }

    // ######################################################################################

    @Override
    protected @NotNull OAuth2APIExchangeCallbackQueryForUser<GitHubOAuth2User> initializeOAuth2APIExchangeCallbackQueryForUser() {
        return query -> exchangeForUser(exchangeForToken(query));
    }

    // ######################################################################################

    @Override
    protected @NotNull OAuth2APIExchangeTokenForUserIdentifier<GitHubOAuth2Token> initializeOAuth2APIExchangeTokenForUserIdentifier() {
        return token -> exchangeForUser(token).identifier();
    }

    // ######################################################################################

    @Override
    protected @NotNull OAuth2APIExchangeTokenForUser<GitHubOAuth2Token, GitHubOAuth2User> initializeOAuth2APIExchangeTokenForUser() {
        String urlWithoutQueryAndAnchor = "https://api.github.com/user";
        OAuth2URL url = new OAuth2URL(urlWithoutQueryAndAnchor, 0);
        OAuth2Request<?> template = new OAuth2Request<>(OAuth2RequestMethod.GET, url);
        return token -> {
            OAuth2Request<?> request = template.copy(true, false, true);
            request.headers(new OAuth2Headers(1).authorization("token", token.accessToken()));
            AbstractOAuth2Response response = metadata.requestExecutor().execute(request);
            JsonNode root = OAuth2DataUtils.readJSONAsTree(response.bodyInputStream());
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

    // ######################################################################################
    // #################### constructor #####################################################
    // ######################################################################################

    /**
     * Construct an instance.
     *
     * @param metadata metadata
     */
    public GitHubOAuth2Client(@NotNull OAuth2ClientMetadata<GitHubOAuth2ApplicationProperties, GitHubOAuth2AuthorizationProperties> metadata) {
        super(metadata);
    }

}
