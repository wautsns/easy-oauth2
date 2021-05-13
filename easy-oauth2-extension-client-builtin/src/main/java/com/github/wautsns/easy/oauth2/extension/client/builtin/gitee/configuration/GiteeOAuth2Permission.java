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

import org.jetbrains.annotations.NotNull;
import java.util.Objects;

/**
 * Gitee oauth2 permission.
 *
 * @author wautsns
 * @see <a href="https://gitee.com/api/v5/oauth_doc">Gitee OAuth Docs</a>
 * @since May 04, 2021
 */
public enum GiteeOAuth2Permission {

    /** Access and update user data, activities, etc. */
    USER_INFO("user_info"),

    /** Full control of user projects. */
    PROJECTS("projects"),

    /** Full control of user pull requests. */
    PULL_REQUESTS("pull_requests"),

    /** Full control of user issues. */
    ISSUES("issues"),

    /** Access, create and edit user comments. */
    NOTES("notes"),

    /** Full control of user public keys. */
    KEYS("keys"),

    /** Full control of user webhook. */
    HOOK("hook"),

    /** Full control of user orgs and teams. */
    GROUPS("groups"),

    /** Access, create and update user gists. */
    GISTS("gists"),

    /** Full control of user enterprises and teams. */
    ENTERPRISES("enterprises"),

    /** Access user emails data. */
    EMAILS("emails");

    // ##################################################################################

    /** Value. */
    private final @NotNull String value;

    // ##################################################################################
    // #################### enhanced getter #############################################
    // ##################################################################################

    /**
     * Return value.
     *
     * @return value
     */
    public @NotNull String value() {
        return value;
    }

    // ##################################################################################
    // #################### constructor #################################################
    // ##################################################################################

    /**
     * Construct an instance.
     *
     * @param value value
     */
    GiteeOAuth2Permission(@NotNull String value) {
        this.value = Objects.requireNonNull(value);
    }

}
