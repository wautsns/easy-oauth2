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

import com.github.wautsns.easy.oauth2.core.client.configuration.AbstractOAuth2ApplicationProperties;
import com.github.wautsns.easy.oauth2.extension.client.builtin.BuiltinOAuth2Platform;
import org.jetbrains.annotations.NotNull;
import java.util.Objects;

/**
 * GitHub oauth2 application properties.
 *
 * @author wautsns
 * @since Apr 22, 2021
 */
public final class GitHubOAuth2ApplicationProperties extends AbstractOAuth2ApplicationProperties {

    /** The client id you received from github for your oauth app. */
    private String clientId;
    /** The client secret you received from github for your oauth app. */
    private String clientSecret;
    /**
     * Your application’s callback url. Read our <a href="https://docs.github.com/v3/oauth/">OAuth documentation</a> for
     * more information.
     */
    private String authorizeCallbackURL;

    // ######################################################################################
    // #################### enhanced getter #################################################
    // ######################################################################################

    @Override
    public @NotNull String platformIdentifier() {
        return BuiltinOAuth2Platform.GITHUB.getIdentifier();
    }

    // ######################################################################################
    // #################### validate ########################################################
    // ######################################################################################

    @Override
    public void validate() {
        Objects.requireNonNull(clientId, "Client id cannot be null");
        Objects.requireNonNull(clientSecret, "Client secret cannot be null");
        Objects.requireNonNull(authorizeCallbackURL, "Authorize callback url cannot be null");
    }

    // ######################################################################################
    // #################### getter / setter #################################################
    // ######################################################################################

    public String getClientId() {
        return clientId;
    }

    public GitHubOAuth2ApplicationProperties setClientId(String clientId) {
        this.clientId = clientId;
        return this;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public GitHubOAuth2ApplicationProperties setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
        return this;
    }

    public String getAuthorizeCallbackURL() {
        return authorizeCallbackURL;
    }

    public GitHubOAuth2ApplicationProperties setAuthorizeCallbackURL(String authorizeCallbackURL) {
        this.authorizeCallbackURL = authorizeCallbackURL;
        return this;
    }

}
