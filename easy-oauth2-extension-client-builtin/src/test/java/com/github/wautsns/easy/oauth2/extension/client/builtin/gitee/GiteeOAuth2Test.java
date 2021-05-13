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

import com.github.wautsns.easy.oauth2.core.assembly.kernel.authorize.configuration.OAuth2AuthorizeURLInitializerMetadata;
import com.github.wautsns.easy.oauth2.core.assembly.kernel.exchange.configuration.OAuth2ExchangerMetadata;
import com.github.wautsns.easy.oauth2.extension.client.AbstractOAuth2Test;
import com.github.wautsns.easy.oauth2.extension.client.builtin.gitee.configuration.GiteeOAuth2ApplicationProperties;
import com.github.wautsns.easy.oauth2.extension.client.builtin.gitee.configuration.GiteeOAuth2AuthorizationProperties;
import com.github.wautsns.easy.oauth2.extension.client.builtin.gitee.configuration.GiteeOAuth2Permission;
import com.github.wautsns.easy.oauth2.extension.client.builtin.gitee.kernel.authorize.GiteeOAuth2AuthorizeURLInitializer;
import com.github.wautsns.easy.oauth2.extension.client.builtin.gitee.kernel.exchange.GiteeOAuth2Exchanger;
import org.jetbrains.annotations.NotNull;
import java.util.Arrays;

/**
 * Test {@link GiteeOAuth2Client}.
 *
 * @author wautsns
 * @since May 04, 2021
 */
@SuppressWarnings("all")
public class GiteeOAuth2Test extends AbstractOAuth2Test<GiteeOAuth2ApplicationProperties, GiteeOAuth2AuthorizationProperties, GiteeOAuth2AuthorizeURLInitializer, GiteeOAuth2Exchanger> {

    @Override
    protected @NotNull String authorizeCode() {
        return "f1ed7d4e4c78d0b2dc01";
    }

    // ##################################################################################

    @Override
    protected @NotNull GiteeOAuth2ApplicationProperties initializeApplicationProperties(
            @NotNull String clientId, @NotNull String clientSecret, @NotNull String authorizeCallback) {
        return new GiteeOAuth2ApplicationProperties()
                .setClientId(clientId)
                .setClientSecret(clientSecret)
                .setCallbacks(Arrays.asList(authorizeCallback.split(";")));
    }

    @Override
    protected @NotNull GiteeOAuth2AuthorizationProperties initializeAuthorizationProperties() {
        return new GiteeOAuth2AuthorizationProperties()
                .setPermissions(Arrays.asList(
                        GiteeOAuth2Permission.USER_INFO,
                        GiteeOAuth2Permission.EMAILS
                ));
    }

    @Override
    protected @NotNull GiteeOAuth2AuthorizeURLInitializer initializeAuthorizeURLInitializer(
            @NotNull OAuth2AuthorizeURLInitializerMetadata<GiteeOAuth2ApplicationProperties, GiteeOAuth2AuthorizationProperties> metadata) {
        return new GiteeOAuth2AuthorizeURLInitializer(metadata);
    }

    @Override
    protected @NotNull GiteeOAuth2Exchanger initializeExchanger(
            @NotNull OAuth2ExchangerMetadata<GiteeOAuth2ApplicationProperties> metadata) {
        return new GiteeOAuth2Exchanger(metadata);
    }

}
