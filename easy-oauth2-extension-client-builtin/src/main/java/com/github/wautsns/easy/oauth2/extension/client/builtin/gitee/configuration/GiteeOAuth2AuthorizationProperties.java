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

import com.github.wautsns.easy.oauth2.core.client.kernel.authorize.configuration.AbstractOAuth2AuthorizationProperties;
import com.github.wautsns.easy.oauth2.core.request.model.basic.OAuth2URL;
import com.github.wautsns.easy.oauth2.extension.client.builtin.BuiltinOAuth2Platform;
import com.github.wautsns.easy.oauth2.extension.client.builtin.gitee.configuration.constant.GiteeOAuth2Permission;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Gitee oauth2 authorization properties.
 *
 * @author wautsns
 * @since May 05, 2021
 */
public final class GiteeOAuth2AuthorizationProperties
        extends AbstractOAuth2AuthorizationProperties {

    /** Permissions. */
    private List<GiteeOAuth2Permission> permissions;

    // ##################################################################################
    // #################### enhanced getter #############################################
    // ##################################################################################

    @Override
    public @NotNull String platform() {
        return BuiltinOAuth2Platform.GITEE.identifier();
    }

    // ##################################################################################
    // #################### add to query ################################################
    // ##################################################################################

    /**
     * Add properties to the given {@code url} query.
     *
     * @param url url
     * @return the {@code url}
     */
    public @NotNull OAuth2URL addToQuery(@NotNull OAuth2URL url) {
        if (permissions != null) {
            String scope = permissions.stream()
                    .map(GiteeOAuth2Permission::value)
                    .collect(Collectors.joining(" "));
            url.query().unique("scope", scope);
        }
        return url;
    }

    // ##################################################################################
    // #################### validate ####################################################
    // ##################################################################################

    @Override
    public void validate() {
        if (permissions != null) {
            permissions.forEach(Objects::requireNonNull);
        }
    }

    // ##################################################################################
    // #################### getter / setter #############################################
    // ##################################################################################

    public List<GiteeOAuth2Permission> getPermissions() {
        return permissions;
    }

    public GiteeOAuth2AuthorizationProperties setPermissions(
            List<GiteeOAuth2Permission> permissions) {
        this.permissions = permissions;
        return this;
    }

}
