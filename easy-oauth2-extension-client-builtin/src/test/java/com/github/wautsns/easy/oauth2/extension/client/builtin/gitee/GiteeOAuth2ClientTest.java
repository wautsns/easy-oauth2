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
package com.github.wautsns.easy.oauth2.extension.client.builtin.gitee;

import com.github.wautsns.easy.oauth2.core.client.AbstractOAuth2Client;
import com.github.wautsns.easy.oauth2.core.client.configuration.OAuth2ClientMetadata;
import com.github.wautsns.easy.oauth2.core.request.executor.OAuth2RequestExecutorFactoryManager;
import com.github.wautsns.easy.oauth2.core.request.executor.configuration.OAuth2RequestExecutorProperties;
import com.github.wautsns.easy.oauth2.extension.client.builtin.AbstractOAuth2ClientTest;
import com.github.wautsns.easy.oauth2.extension.client.builtin.BuiltinOAuth2Platform;
import com.github.wautsns.easy.oauth2.extension.client.builtin.gitee.configuration.GiteeOAuth2ApplicationProperties;
import com.github.wautsns.easy.oauth2.extension.client.builtin.gitee.configuration.GiteeOAuth2AuthorizationProperties;
import com.github.wautsns.easy.oauth2.extension.client.builtin.gitee.configuration.GiteeOAuth2Permission;
import org.jetbrains.annotations.NotNull;
import java.time.Duration;
import java.util.Arrays;
import java.util.LinkedHashSet;

/**
 * Test {@link GiteeOAuth2Client}.
 *
 * <ul>
 * <li style="list-style-type:none">########## VM options ###############</li>
 * <li>-Dclient-id={@link GiteeOAuth2ApplicationProperties#getClientId()}</li>
 * <li>-Dclient-secret={@link GiteeOAuth2ApplicationProperties#getClientSecret()}</li>
 * <li>-Dcallback-url={@link GiteeOAuth2ApplicationProperties#getCallbacks()} (join with semicolon(;))</li>
 * </ul>
 *
 * @author wautsns
 * @since May 04, 2021
 */
@SuppressWarnings("all")
public class GiteeOAuth2ClientTest extends AbstractOAuth2ClientTest {

    @Override
    protected @NotNull String authorizeCode() {
        return "3a4bf611d2314bd0d7f957d71b02fba26332916f9ac0dd323884b3e79b3291c0";
    }

    @Override
    protected @NotNull AbstractOAuth2Client<?, ?, ?> initializeOAuth2Client(
            @NotNull String clientId, @NotNull String clientSecret, @NotNull String callbackURL) {
        return new GiteeOAuth2Client(new OAuth2ClientMetadata<>(
                BuiltinOAuth2Platform.GITEE.getIdentifier(),
                new GiteeOAuth2ApplicationProperties()
                        .setClientId(clientId)
                        .setClientSecret(clientSecret)
                        .setCallbacks(Arrays.asList(callbackURL.split(";"))),
                new GiteeOAuth2AuthorizationProperties()
                        .setPermissions(new LinkedHashSet<>(Arrays.asList(
                                GiteeOAuth2Permission.USER_INFO,
                                GiteeOAuth2Permission.EMAILS
                        ))),
                OAuth2RequestExecutorFactoryManager.any().create(
                        new OAuth2RequestExecutorProperties()
                                .setConnectTimeout(Duration.ofSeconds(20))
                                .setReadTimeout(Duration.ofSeconds(20))
                )
        ));
    }

}
