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

import com.github.wautsns.easy.oauth2.core.assembly.kernel.authorize.configuration.OAuth2AuthorizeURLInitializerMetadata;
import com.github.wautsns.easy.oauth2.core.assembly.kernel.exchange.configuration.OAuth2ExchangerMetadata;
import com.github.wautsns.easy.oauth2.extension.client.AbstractOAuth2Test;
import com.github.wautsns.easy.oauth2.extension.client.builtin.github.configuration.GitHubOAuth2ApplicationProperties;
import com.github.wautsns.easy.oauth2.extension.client.builtin.github.configuration.GitHubOAuth2AuthorizationProperties;
import com.github.wautsns.easy.oauth2.extension.client.builtin.github.configuration.GitHubOAuth2Scope;
import com.github.wautsns.easy.oauth2.extension.client.builtin.github.kernel.authorize.GitHubOAuth2AuthorizeURLInitializer;
import com.github.wautsns.easy.oauth2.extension.client.builtin.github.kernel.exchange.GitHubOAuth2Exchanger;
import org.jetbrains.annotations.NotNull;
import java.util.Arrays;

/**
 * Test {@link GitHubOAuth2Client}.
 *
 * @author wautsns
 * @since May 04, 2021
 */
@SuppressWarnings("all")
public class GitHubOAuth2Test extends AbstractOAuth2Test<GitHubOAuth2ApplicationProperties, GitHubOAuth2AuthorizationProperties, GitHubOAuth2AuthorizeURLInitializer, GitHubOAuth2Exchanger> {

    @Override
    protected @NotNull String authorizeCode() {
        return "d65795837bd0c6175033";
    }

    // ##################################################################################

    @Override
    protected @NotNull GitHubOAuth2ApplicationProperties initializeApplicationProperties(
            @NotNull String clientId, @NotNull String clientSecret, @NotNull String authorizeCallback) {
        return new GitHubOAuth2ApplicationProperties()
                .setClientId(clientId)
                .setClientSecret(clientSecret)
                .setAuthorizeCallback(authorizeCallback);
    }

    @Override
    protected @NotNull GitHubOAuth2AuthorizationProperties initializeAuthorizationProperties() {
        return new GitHubOAuth2AuthorizationProperties()
                .setScopes(Arrays.asList(
                        GitHubOAuth2Scope.NOTIFICATIONS,
                        GitHubOAuth2Scope.USER_EMAIL
                ))
                .setAllowSignup(Boolean.TRUE);
    }

    @Override
    protected @NotNull GitHubOAuth2AuthorizeURLInitializer initializeAuthorizeURLInitializer(
            @NotNull OAuth2AuthorizeURLInitializerMetadata<GitHubOAuth2ApplicationProperties, GitHubOAuth2AuthorizationProperties> metadata) {
        return new GitHubOAuth2AuthorizeURLInitializer(metadata);
    }

    @Override
    protected @NotNull GitHubOAuth2Exchanger initializeExchanger(
            @NotNull OAuth2ExchangerMetadata<GitHubOAuth2ApplicationProperties> metadata) {
        return new GitHubOAuth2Exchanger(metadata);
    }

}
