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
package com.github.wautsns.easy.oauth2.extension.client.builtin.github.assembly;

import com.github.wautsns.easy.oauth2.core.client.assembly.OAuth2PlatformAssemblyFactory;
import com.github.wautsns.easy.oauth2.core.client.kernel.authorize.configuration.OAuth2AuthorizeURLInitializerMetadata;
import com.github.wautsns.easy.oauth2.core.client.kernel.exchange.configuration.OAuth2ExchangerMetadata;
import com.github.wautsns.easy.oauth2.extension.client.builtin.BuiltinOAuth2Platform;
import com.github.wautsns.easy.oauth2.extension.client.builtin.github.configuration.GitHubOAuth2ApplicationProperties;
import com.github.wautsns.easy.oauth2.extension.client.builtin.github.configuration.GitHubOAuth2AuthorizationProperties;
import com.github.wautsns.easy.oauth2.extension.client.builtin.github.kernel.authorize.GitHubOAuth2AuthorizeURLInitializer;
import com.github.wautsns.easy.oauth2.extension.client.builtin.github.kernel.exchange.GitHubOAuth2Exchanger;

import org.jetbrains.annotations.NotNull;

/**
 * GitHub oauth2 assembly factory.
 *
 * @author wautsns
 * @since May 14, 2021
 */
public final class GitHubOAuth2AssemblyFactory
        implements OAuth2PlatformAssemblyFactory<GitHubOAuth2ApplicationProperties, GitHubOAuth2AuthorizationProperties, GitHubOAuth2AuthorizeURLInitializer, GitHubOAuth2Exchanger> {

    // ##################################################################################
    // #################### enhanced getter #############################################
    // ##################################################################################

    @Override
    public @NotNull String platform() {
        return BuiltinOAuth2Platform.GITHUB.identifier();
    }

    // ##################################################################################
    // #################### initialize ##################################################
    // ##################################################################################

    @Override
    public @NotNull GitHubOAuth2AuthorizationProperties initializeDefaultAuthorizationProperties() {
        return new GitHubOAuth2AuthorizationProperties();
    }

    // ##################################################################################
    // #################### create ######################################################
    // ##################################################################################

    @Override
    public @NotNull GitHubOAuth2AuthorizeURLInitializer createAuthorizeURLInitializer(
            @NotNull OAuth2AuthorizeURLInitializerMetadata<GitHubOAuth2ApplicationProperties, GitHubOAuth2AuthorizationProperties> metadata) {
        return new GitHubOAuth2AuthorizeURLInitializer(metadata);
    }

    @Override
    public @NotNull GitHubOAuth2Exchanger createExchanger(
            @NotNull OAuth2ExchangerMetadata<GitHubOAuth2ApplicationProperties> metadata) {
        return new GitHubOAuth2Exchanger(metadata);
    }

}
