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
package com.github.wautsns.easy.oauth2.extension.client.builtin.gitee.assembly;

import com.github.wautsns.easy.oauth2.core.client.assembly.OAuth2PlatformAssemblyFactory;
import com.github.wautsns.easy.oauth2.core.client.kernel.authorize.configuration.OAuth2AuthorizeURLInitializerMetadata;
import com.github.wautsns.easy.oauth2.core.client.kernel.exchange.configuration.OAuth2ExchangerMetadata;
import com.github.wautsns.easy.oauth2.extension.client.builtin.BuiltinOAuth2Platform;
import com.github.wautsns.easy.oauth2.extension.client.builtin.gitee.configuration.GiteeOAuth2ApplicationProperties;
import com.github.wautsns.easy.oauth2.extension.client.builtin.gitee.configuration.GiteeOAuth2AuthorizationProperties;
import com.github.wautsns.easy.oauth2.extension.client.builtin.gitee.kernel.authorize.GiteeOAuth2AuthorizeURLInitializer;
import com.github.wautsns.easy.oauth2.extension.client.builtin.gitee.kernel.exchange.GiteeOAuth2Exchanger;
import org.jetbrains.annotations.NotNull;

/**
 * Gitee oauth2 assembly factory.
 *
 * @author wautsns
 * @since May 14, 2021
 */
public final class GiteeOAuth2AssemblyFactory implements OAuth2PlatformAssemblyFactory<GiteeOAuth2ApplicationProperties, GiteeOAuth2AuthorizationProperties, GiteeOAuth2AuthorizeURLInitializer, GiteeOAuth2Exchanger> {

    // ##################################################################################
    // #################### enhanced getter #############################################
    // ##################################################################################

    @Override
    public @NotNull String platform() {
        return BuiltinOAuth2Platform.GITEE.identifier();
    }

    // ##################################################################################
    // #################### initialize ##################################################
    // ##################################################################################

    @Override
    public @NotNull GiteeOAuth2AuthorizationProperties initializeDefaultAuthorizationProperties() {
        return new GiteeOAuth2AuthorizationProperties();
    }

    // ##################################################################################
    // #################### create ######################################################
    // ##################################################################################

    @Override
    public @NotNull GiteeOAuth2AuthorizeURLInitializer createAuthorizeURLInitializer(
            @NotNull OAuth2AuthorizeURLInitializerMetadata<GiteeOAuth2ApplicationProperties, GiteeOAuth2AuthorizationProperties> metadata) {
        return new GiteeOAuth2AuthorizeURLInitializer(metadata);
    }

    @Override
    public @NotNull GiteeOAuth2Exchanger createExchanger(
            @NotNull OAuth2ExchangerMetadata<GiteeOAuth2ApplicationProperties> metadata) {
        return new GiteeOAuth2Exchanger(metadata);
    }

}
