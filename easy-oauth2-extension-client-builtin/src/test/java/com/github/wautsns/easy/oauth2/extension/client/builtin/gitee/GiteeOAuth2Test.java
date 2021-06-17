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

import com.github.wautsns.easy.oauth2.core.client.configuration.AbstractOAuth2ApplicationProperties;
import com.github.wautsns.easy.oauth2.core.client.kernel.authorize.configuration.AbstractOAuth2AuthorizationProperties;
import com.github.wautsns.easy.oauth2.extension.client.AbstractOAuth2Test;
import com.github.wautsns.easy.oauth2.extension.client.builtin.gitee.configuration.GiteeOAuth2ApplicationProperties;
import com.github.wautsns.easy.oauth2.extension.client.builtin.gitee.configuration.GiteeOAuth2AuthorizationProperties;
import com.github.wautsns.easy.oauth2.extension.client.builtin.gitee.configuration.constant.GiteeOAuth2Permission;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

/**
 * Test {@link GiteeOAuth2Client}.
 *
 * @author wautsns
 * @since May 04, 2021
 */
@SuppressWarnings("all")
public class GiteeOAuth2Test extends AbstractOAuth2Test {

    @Override
    protected @NotNull String authorizeCode() {
        return "07c2dda4990f30b07c3b3e369bc09df79fbf415005871eb28db4d9fdc0bb47e5";
    }

    // ##################################################################################

    @Override
    protected @NotNull AbstractOAuth2ApplicationProperties initializeApplicationProperties(
            @NotNull String clientId, @NotNull String clientSecret,
            @NotNull String authorizeCallback) {
        return new GiteeOAuth2ApplicationProperties()
                .setClientId(clientId)
                .setClientSecret(clientSecret)
                .setCallbacks(Arrays.asList(authorizeCallback.split(";")));
    }

    @Override
    protected @NotNull AbstractOAuth2AuthorizationProperties initializeAuthorizationProperties() {
        return new GiteeOAuth2AuthorizationProperties()
                .setPermissions(Arrays.asList(
                        GiteeOAuth2Permission.USER_INFO,
                        GiteeOAuth2Permission.EMAILS
                ));
    }

}
