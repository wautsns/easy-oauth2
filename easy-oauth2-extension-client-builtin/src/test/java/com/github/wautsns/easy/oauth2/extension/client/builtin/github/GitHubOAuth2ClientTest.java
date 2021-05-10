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

import com.github.wautsns.easy.oauth2.core.client.AbstractOAuth2Client;
import com.github.wautsns.easy.oauth2.core.client.configuration.OAuth2ClientMetadata;
import com.github.wautsns.easy.oauth2.core.request.executor.OAuth2RequestExecutorFactoryManager;
import com.github.wautsns.easy.oauth2.core.request.executor.configuration.OAuth2RequestExecutorProperties;
import com.github.wautsns.easy.oauth2.extension.client.builtin.AbstractOAuth2ClientTest;
import com.github.wautsns.easy.oauth2.extension.client.builtin.BuiltinOAuth2Platform;
import com.github.wautsns.easy.oauth2.extension.client.builtin.github.configuration.GitHubOAuth2ApplicationProperties;
import com.github.wautsns.easy.oauth2.extension.client.builtin.github.configuration.GitHubOAuth2AuthorizationProperties;
import com.github.wautsns.easy.oauth2.extension.client.builtin.github.configuration.GitHubOAuth2Scope;
import org.jetbrains.annotations.NotNull;
import java.time.Duration;
import java.util.Arrays;

/**
 * Test {@link GitHubOAuth2Client}.
 *
 * <ul>
 * <li style="list-style-type:none">########## VM options ###############</li>
 * <li>-Dclient-id={@link GitHubOAuth2ApplicationProperties#getClientId()}</li>
 * <li>-Dclient-secret={@link GitHubOAuth2ApplicationProperties#getClientSecret()}</li>
 * <li>-Dcallback-url={@link GitHubOAuth2ApplicationProperties#getAuthorizeCallbackURL()}</li>
 * </ul>
 *
 * @author wautsns
 * @since May 04, 2021
 */
@SuppressWarnings("all")
public class GitHubOAuth2ClientTest extends AbstractOAuth2ClientTest {

    @Override
    protected @NotNull String authorizeCode() {
        return "2306ee4895dd109f3f8d";
    }

    @Override
    protected @NotNull AbstractOAuth2Client<?, ?, ?> initializeOAuth2Client(
            @NotNull String clientId, @NotNull String clientSecret, @NotNull String callbackURL) {
        return new GitHubOAuth2Client(new OAuth2ClientMetadata<>(
                BuiltinOAuth2Platform.GITHUB.getIdentifier(),
                new GitHubOAuth2ApplicationProperties()
                        .setClientId(clientId)
                        .setClientSecret(clientSecret)
                        .setAuthorizeCallbackURL(callbackURL),
                new GitHubOAuth2AuthorizationProperties()
                        .setScopes(Arrays.asList(
                                GitHubOAuth2Scope.NOTIFICATIONS,
                                GitHubOAuth2Scope.USER_EMAIL
                        ))
                        .setAllowSignup(Boolean.TRUE),
                OAuth2RequestExecutorFactoryManager.any().create(
                        new OAuth2RequestExecutorProperties()
                                .setConnectTimeout(Duration.ofSeconds(20))
                                .setReadTimeout(Duration.ofSeconds(20))
                )
        ));
    }

}
