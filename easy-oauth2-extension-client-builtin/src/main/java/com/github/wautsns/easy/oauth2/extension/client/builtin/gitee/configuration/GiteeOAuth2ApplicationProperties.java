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
package com.github.wautsns.easy.oauth2.extension.client.builtin.gitee.configuration;

import com.github.wautsns.easy.oauth2.core.assembly.configuration.AbstractOAuth2ApplicationProperties;
import com.github.wautsns.easy.oauth2.extension.client.builtin.BuiltinOAuth2Platform;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import java.util.Objects;

/**
 * Gitee oauth2 application properties.
 *
 * @author wautsns
 * @since Apr 22, 2021
 */
public final class GiteeOAuth2ApplicationProperties extends AbstractOAuth2ApplicationProperties {

    /** The client id you received from gitee for your oauth app. */
    private String clientId;
    /** The client secret you received from gitee for your oauth app. */
    private String clientSecret;
    /** Your application’s callback urls (gitee allows multiple callbacks, but only the last one is active). */
    private List<String> callbacks;

    // ##################################################################################
    // #################### enhanced getter #############################################
    // ##################################################################################

    @Override
    public @NotNull String platform() {
        return BuiltinOAuth2Platform.GITEE.identifier();
    }

    // ##################################################################################
    // #################### validate ####################################################
    // ##################################################################################

    @Override
    public void validate() {
        Objects.requireNonNull(clientId);
        Objects.requireNonNull(clientSecret);
        Objects.requireNonNull(callbacks);
        callbacks.forEach(Objects::requireNonNull);
    }

    // ##################################################################################
    // #################### getter / setter #############################################
    // ##################################################################################

    public String getClientId() {
        return clientId;
    }

    public GiteeOAuth2ApplicationProperties setClientId(String clientId) {
        this.clientId = clientId;
        return this;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public GiteeOAuth2ApplicationProperties setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
        return this;
    }

    public List<String> getCallbacks() {
        return callbacks;
    }

    public GiteeOAuth2ApplicationProperties setCallbacks(List<String> callbacks) {
        this.callbacks = callbacks;
        return this;
    }

}
