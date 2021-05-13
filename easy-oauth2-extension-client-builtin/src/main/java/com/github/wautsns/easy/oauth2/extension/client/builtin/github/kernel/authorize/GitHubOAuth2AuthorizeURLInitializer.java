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
package com.github.wautsns.easy.oauth2.extension.client.builtin.github.kernel.authorize;

import com.github.wautsns.easy.oauth2.core.assembly.kernel.authorize.AbstractOAuth2AuthorizeURLInitializer;
import com.github.wautsns.easy.oauth2.core.assembly.kernel.authorize.configuration.OAuth2AuthorizeURLInitializerMetadata;
import com.github.wautsns.easy.oauth2.core.assembly.kernel.authorize.function.operation.OAuth2OperationInitializeAuthorizeURL;
import com.github.wautsns.easy.oauth2.core.request.model.basic.OAuth2URL;
import com.github.wautsns.easy.oauth2.extension.client.builtin.github.configuration.GitHubOAuth2ApplicationProperties;
import com.github.wautsns.easy.oauth2.extension.client.builtin.github.configuration.GitHubOAuth2AuthorizationProperties;
import org.jetbrains.annotations.NotNull;

/**
 * GitHub oauth2 authorize url initializer.
 *
 * @author wautsns
 * @since May 11, 2021
 */
public final class GitHubOAuth2AuthorizeURLInitializer extends AbstractOAuth2AuthorizeURLInitializer<GitHubOAuth2ApplicationProperties, GitHubOAuth2AuthorizationProperties> {

    // ##################################################################################
    // #################### initialize oauth2 operation #################################
    // ##################################################################################

    @Override
    protected @NotNull OAuth2OperationInitializeAuthorizeURL initializeOperationInitializeAuthorizeURL() {
        String urlWithoutQueryAndAnchor = "https://github.com/login/oauth/authorize";
        OAuth2URL template = new OAuth2URL(urlWithoutQueryAndAnchor, 5);
        template.query()
                .unique("client_id", metadata.applicationProperties().getClientId())
                .unique("redirect_uri", metadata.applicationProperties().getAuthorizeCallback());
        metadata.authorizationProperties()
                .addScopeToQuery(template)
                .addAllowSignupToQuery(template);
        return state -> {
            OAuth2URL url = template.copy();
            url.query().unique("state", state);
            return url;
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
    public GitHubOAuth2AuthorizeURLInitializer(
            @NotNull OAuth2AuthorizeURLInitializerMetadata<GitHubOAuth2ApplicationProperties, GitHubOAuth2AuthorizationProperties> metadata) {
        super(metadata);
    }

}
