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
package com.github.wautsns.easy.oauth2.extension.client.builtin.github.kernel.exchange.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.wautsns.easy.oauth2.core.client.kernel.exchange.model.user.AbstractOAuth2User;
import com.github.wautsns.easy.oauth2.core.client.kernel.exchange.model.user.property.OAuth2UserAvatarSupplier;
import com.github.wautsns.easy.oauth2.core.client.kernel.exchange.model.user.property.OAuth2UserEmailSupplier;
import com.github.wautsns.easy.oauth2.core.client.kernel.exchange.model.user.property.OAuth2UserNicknameSupplier;
import com.github.wautsns.easy.oauth2.core.client.kernel.exchange.model.user.property.OAuth2UserUsernameSupplier;
import com.github.wautsns.easy.oauth2.extension.client.builtin.BuiltinOAuth2Platform;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * GitHub oauth2 user.
 *
 * <pre>
 * {
 *     "login":"wautsns",
 *     "id":39336604,
 *     "node_id":"MDQ6VXNlcjM5MzM2NjA0",
 *     "avatar_url":"https://avatars.githubusercontent.com/u/39336604?v=4",
 *     "gravatar_id":"",
 *     "url":"https://api.github.com/users/wautsns",
 *     "html_url":"https://github.com/wautsns",
 *     "followers_url":"https://api.github.com/users/wautsns/followers",
 *     "following_url":"https://api.github.com/users/wautsns/following{/other_user}",
 *     "gists_url":"https://api.github.com/users/wautsns/gists{/gist_id}",
 *     "starred_url":"https://api.github.com/users/wautsns/starred{/owner}{/repo}",
 *     "subscriptions_url":"https://api.github.com/users/wautsns/subscriptions",
 *     "organizations_url":"https://api.github.com/users/wautsns/orgs",
 *     "repos_url":"https://api.github.com/users/wautsns/repos",
 *     "events_url":"https://api.github.com/users/wautsns/events{/privacy}",
 *     "received_events_url":"https://api.github.com/users/wautsns/received_events",
 *     "type":"User",
 *     "site_admin":false,
 *     "name":"wautsns",
 *     "company":null,
 *     "blog":"",
 *     "location":null,
 *     "email":null,
 *     "hireable":null,
 *     "bio":null,
 *     "twitter_username":null,
 *     "public_repos":8,
 *     "public_gists":0,
 *     "followers":0,
 *     "following":0,
 *     "created_at":"2018-05-16T12:17:46Z",
 *     "updated_at":"2021-05-03T16:15:39Z"
 * }
 * </pre>
 *
 * @author wautsns
 * @since Apr 22, 2021
 */
public final class GitHubOAuth2User
        extends AbstractOAuth2User
        implements OAuth2UserUsernameSupplier,
                   OAuth2UserNicknameSupplier,
                   OAuth2UserAvatarSupplier,
                   OAuth2UserEmailSupplier {

    // ##################################################################################
    // #################### enhanced getter #############################################
    // ##################################################################################

    @Override
    public @NotNull String platform() {
        return BuiltinOAuth2Platform.GITHUB.identifier();
    }

    // ##################################################################################

    @Override
    public @NotNull String identifier() {
        return raw.required("id").asText();
    }

    @Override
    public @Nullable String username() {
        return raw.path("login").asText(null);
    }

    @Override
    public @Nullable String nickname() {
        return raw.path("name").asText(null);
    }

    @Override
    public @Nullable String avatar() {
        return raw.path("avatar_url").asText(null);
    }

    @Override
    public @Nullable String email() {
        return raw.path("email").asText(null);
    }

    // ##################################################################################
    // #################### constructor #################################################
    // ##################################################################################

    /**
     * Construct an instance.
     *
     * @param raw raw
     */
    public GitHubOAuth2User(@NotNull JsonNode raw) {
        super(raw);
    }

}
