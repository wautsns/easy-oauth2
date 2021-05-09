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
package com.github.wautsns.easy.oauth2.extension.client.builtin.gitee.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.wautsns.easy.oauth2.core.client.model.user.AbstractOAuth2User;
import com.github.wautsns.easy.oauth2.core.client.model.user.property.OAuth2UserAvatarSupplier;
import com.github.wautsns.easy.oauth2.core.client.model.user.property.OAuth2UserEmailSupplier;
import com.github.wautsns.easy.oauth2.core.client.model.user.property.OAuth2UserNicknameSupplier;
import com.github.wautsns.easy.oauth2.core.client.model.user.property.OAuth2UserUsernameSupplier;
import com.github.wautsns.easy.oauth2.extension.client.builtin.BuiltinOAuth2Platform;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Gitee oauth2 user.
 *
 * <pre>
 * {
 *     "id":1937041,
 *     "login":"wautsns",
 *     "name":"独自漫步〃寂静の夜空下",
 *     "avatar_url":"https://portrait.gitee.com/uploads/avatars/user/645/1937041_wautsns_1578962737.png",
 *     "url":"https://gitee.com/api/v5/users/wautsns",
 *     "html_url":"https://gitee.com/wautsns",
 *     "followers_url":"https://gitee.com/api/v5/users/wautsns/followers",
 *     "following_url":"https://gitee.com/api/v5/users/wautsns/following_url{/other_user}",
 *     "gists_url":"https://gitee.com/api/v5/users/wautsns/gists{/gist_id}",
 *     "starred_url":"https://gitee.com/api/v5/users/wautsns/starred{/owner}{/repo}",
 *     "subscriptions_url":"https://gitee.com/api/v5/users/wautsns/subscriptions",
 *     "organizations_url":"https://gitee.com/api/v5/users/wautsns/orgs",
 *     "repos_url":"https://gitee.com/api/v5/users/wautsns/repos",
 *     "events_url":"https://gitee.com/api/v5/users/wautsns/events{/privacy}",
 *     "received_events_url":"https://gitee.com/api/v5/users/wautsns/received_events",
 *     "type":"User",
 *     "blog":null,
 *     "weibo":null,
 *     "bio":"",
 *     "public_repos":1,
 *     "public_gists":0,
 *     "followers":0,
 *     "following":0,
 *     "stared":0,
 *     "watched":5,
 *     "created_at":"2018-05-15T21:27:41+08:00",
 *     "updated_at":"2021-05-05T19:21:12+08:00",
 *     "email":null
 * }
 * </pre>
 *
 * @author wautsns
 * @since Apr 22, 2021
 */
public final class GiteeOAuth2User
        extends AbstractOAuth2User
        implements OAuth2UserUsernameSupplier,
                   OAuth2UserNicknameSupplier,
                   OAuth2UserAvatarSupplier,
                   OAuth2UserEmailSupplier {

    // ######################################################################################
    // #################### enhanced getter #################################################
    // ######################################################################################

    @Override
    public @NotNull String platformIdentifier() {
        return BuiltinOAuth2Platform.GITEE.getIdentifier();
    }

    // ######################################################################################

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

    // ######################################################################################
    // #################### constructor #####################################################
    // ######################################################################################

    /**
     * Construct an instance.
     *
     * @param raw raw
     */
    public GiteeOAuth2User(@NotNull JsonNode raw) {
        super(raw);
    }

}
