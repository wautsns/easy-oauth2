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
package com.github.wautsns.easy.oauth2.extension.client.builtin.github.configuration;

import com.github.wautsns.easy.oauth2.core.client.configuration.AbstractOAuth2AuthorizationProperties;
import com.github.wautsns.easy.oauth2.core.request.model.basic.OAuth2URL;
import com.github.wautsns.easy.oauth2.extension.client.builtin.BuiltinOAuth2Platform;
import org.jetbrains.annotations.NotNull;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * GitHub oauth2 authorization properties.
 *
 * @author wautsns
 * @since May 05, 2021
 */
public final class GitHubOAuth2AuthorizationProperties extends AbstractOAuth2AuthorizationProperties {

    /**
     * A list of <a href="https://docs.github.com/en/apps/building-oauth-apps/understanding-scopes-for-oauth-apps">
     * scopes</a>. If not provided, <code>scope</code> defaults to an empty list for users that have not authorized any
     * scopes for the application. For users who have authorized scopes for the application, the user won't be shown the
     * OAuth authorization page with the list of scopes. Instead, this step of the flow will automatically complete with
     * the set of scopes the user has authorized for the application. For example, if a user has already performed the
     * web flow twice and has authorized one token with <code>user</code> scope and another token with <code>repo</code>
     * scope, a third web flow that does not provide a <code>scope</code> will receive a token with <code>user</code>
     * and <code>repo</code> scope.
     */
    private LinkedHashSet<GitHubOAuth2Scope> scopes;
    /**
     * Whether or not unauthenticated users will be offered an option to sign up for GitHub during the OAuth flow. The
     * default is <code>true</code>. Use <code>false</code> when a policy prohibits signups.
     */
    private Boolean allowSignup;

    // ######################################################################################
    // #################### enhanced getter #################################################
    // ######################################################################################

    @Override
    public @NotNull String platformIdentifier() {
        return BuiltinOAuth2Platform.GITHUB.getIdentifier();
    }

    // ######################################################################################
    // #################### append ##########################################################
    // ######################################################################################

    /**
     * Append `scope` to the {@code url}.
     *
     * @param url url
     * @return self reference
     */
    public @NotNull GitHubOAuth2AuthorizationProperties appendScope(OAuth2URL url) {
        if (scopes == null) { return this; }
        String scope = scopes.stream()
                .map(GitHubOAuth2Scope::value)
                .collect(Collectors.joining(" "));
        url.query().unique("scope", scope);
        return this;
    }

    /**
     * Append `allow_signup` to the {@code url}.
     *
     * @param url url
     * @return self reference
     */
    public @NotNull GitHubOAuth2AuthorizationProperties appendAllowSignup(OAuth2URL url) {
        if (allowSignup != null) { url.query().unique("allow_signup", allowSignup.toString()); }
        return this;
    }

    // ######################################################################################
    // #################### validate ########################################################
    // ######################################################################################

    @Override
    public void validate() {
        if ((scopes != null) && scopes.stream().anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException("Scopes cannot contain null.");
        }
    }

    // ######################################################################################
    // #################### getter / setter #################################################
    // ######################################################################################

    public LinkedHashSet<GitHubOAuth2Scope> getScopes() {
        return scopes;
    }

    public GitHubOAuth2AuthorizationProperties setScopes(LinkedHashSet<GitHubOAuth2Scope> scopes) {
        this.scopes = scopes;
        return this;
    }

    public Boolean getAllowSignup() {
        return allowSignup;
    }

    public GitHubOAuth2AuthorizationProperties setAllowSignup(Boolean allowSignup) {
        this.allowSignup = allowSignup;
        return this;
    }

}
