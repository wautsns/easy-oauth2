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

import org.jetbrains.annotations.NotNull;
import java.util.Objects;

/**
 * GitHub oauth2 scopes.
 *
 * @author wautsns
 * @see <a href="https://docs.github.com/en/developers/apps/scopes-for-oauth-apps">Scopes for OAuth Apps - GitHub
 *         Docs</a>
 * @since May 04, 2021
 */
public enum GitHubOAuth2Scope {

    /**
     * Grants full access to repositories, including private repositories. That includes read/write access to code,
     * commit statuses, repository and organization projects, invitations, collaborators, adding team memberships,
     * deployment statuses, and repository webhooks for repositories and organizations. Also grants ability to manage
     * user projects.
     */
    REPO("repo"),

    /**
     * Grants read/write access to public and private repository commit statuses. This scope is only necessary to grant
     * other users or services access to private repository commit statuses <em>without</em> granting access to the
     * code.
     */
    REPO_STATUS("repo:status"),

    /**
     * Grants access to <a href="https://docs.github.com/en/rest/reference/repos#deployments">deployment statuses</a>
     * for public and private repositories. This scope is only necessary to grant other users or services access to
     * deployment statuses,
     * <em>without</em> granting access to the code.
     */
    REPO_DEPLOYMENT("repo_deployment"),

    /**
     * Limits access to public repositories. That includes read/write access to code, commit statuses, repository
     * projects, collaborators, and deployment statuses for public repositories and organizations. Also required for
     * starring public repositories.
     */
    PUBLIC_REPO("public_repo"),

    /**
     * Grants accept/decline abilities for invitations to collaborate on a repository. This scope is only necessary to
     * grant other users or services access to invites <em>without</em> granting access to the code.
     */
    REPO_INVITE("repo:invite"),

    /**
     * Grants: <br> read and write access to security events in the <a href="https://docs.github.com/en/rest/reference/code-scanning">code
     * scanning API</a> <br> read and write access to security events in the <a href="https://docs.github.com/en/rest/reference/secret-scanning">secret
     * scanning API</a> <br> This scope is only necessary to grant other users or services access to security events
     * <em>without</em> granting access to the code.
     */
    SECURITY_EVENTS("security_events"),

    /**
     * Grants read, write, ping, and delete access to repository hooks in public and private repositories. The
     * <code>repo</code> and <code>public_repo</code> scopes grant full access to repositories, including repository
     * hooks. Use the <code>admin:repo_hook</code> scope to limit access to only repository hooks.
     */
    ADMIN_REPO_HOOK("admin:repo_hook"),

    /** Grants read, write, and ping access to hooks in public or private repositories. */
    WRITE_REPO_HOOK("write:repo_hook"),

    /** Grants read and ping access to hooks in public or private repositories. */
    READ_REPO_HOOK("read:repo_hook"),

    /** Fully manage the organization and its teams, projects, and memberships. */
    ADMIN_ORG("admin:org"),

    /** Read and write access to organization membership, organization projects, and team membership. */
    WRITE_ORG("write:org"),

    /** Read-only access to organization membership, organization projects, and team membership. */
    READ_ORG("read:org"),

    /** Fully manage public keys. */
    ADMIN_PUBLIC_KEY("admin:public_key"),

    /** Create, list, and view details for public keys. */
    WRITE_PUBLIC_KEY("write:public_key"),

    /** List and view details for public keys. */
    READ_PUBLIC_KEY("read:public_key"),

    /**
     * Grants read, write, ping, and delete access to organization hooks. <strong>Note:</strong> OAuth tokens will only
     * be able to perform these actions on organization hooks which were created by the OAuth App. Personal access
     * tokens will only be able to perform these actions on organization hooks created by a user.
     */
    ADMIN_ORG_HOOK("admin:org_hook"),

    /** Grants write access to gists. */
    GIST("gist"),

    /**
     * Grants: <br><em> read access to a user's notifications <br></em> mark as read access to threads <br><em> watch
     * and unwatch access to a repository, and <br></em> read, write, and delete access to thread subscriptions.
     */
    NOTIFICATIONS("notifications"),

    /**
     * Grants read/write access to profile info only.  Note that this scope includes <code>user:email</code> and
     * <code>user:follow</code>.
     */
    USER("user"),

    /** Grants access to read a user's profile data. */
    READ_USER("read:user"),

    /** Grants read access to a user's email addresses. */
    USER_EMAIL("user:email"),

    /** Grants access to follow or unfollow other users. */
    USER_FOLLOW("user:follow"),

    /** Grants access to delete adminable repositories. */
    DELETE_REPO("delete_repo"),

    /** Allows read and write access for team discussions. */
    WRITE_DISCUSSION("write:discussion"),

    /** Allows read access for team discussions. */
    READ_DISCUSSION("read:discussion"),

    /**
     * Grants access to upload or publish a package in GitHub Packages. For more information, see "<a
     * href="https://docs.github.com/en/github/managing-packages-with-github-packages/publishing-a-package">Publishing a
     * package</a>".
     */
    WRITE_PACKAGES("write:packages"),

    /**
     * Grants access to download or install packages from GitHub Packages. For more information, see "<a
     * href="https://docs.github.com/en/github/managing-packages-with-github-packages/installing-a-package">Installing a
     * package</a>".
     */
    READ_PACKAGES("read:packages"),

    /**
     * Grants access to delete packages from GitHub Packages. For more information, see "<a
     * href="/en/packages/learn-github-packages/deleting-and-restoring-a-package">Deleting and restoring a
     * package</a>."
     */
    DELETE_PACKAGES("delete:packages"),

    /** Fully manage GPG keys. */
    ADMIN_GPG_KEY("admin:gpg_key"),

    /** Create, list, and view details for GPG keys. */
    WRITE_GPG_KEY("write:gpg_key"),

    /** List and view details for GPG keys. */
    READ_GPG_KEY("read:gpg_key"),

    /**
     * Grants the ability to add and update GitHub Actions workflow files. Workflow files can be committed without this
     * scope if the same file (with both the same path and contents) exists on another branch in the same repository.
     * Workflow files can expose <code>GITHUB_TOKEN</code> which may have a different set of scopes, see <a
     * href="https://docs.github.com/en/free-pro-team@latest/actions/reference/authentication-in-a-workflow#permissions-for-the-github_token">
     * https://docs.github.com/en/free-pro-team@latest/actions/reference/authentication-in-a-workflow#permissions-for-the-github_token</a>
     * for details.
     */
    WORKFLOW("workflow");

    // ######################################################################################

    /** Value. */
    private final @NotNull String value;

    // ######################################################################################
    // #################### enhanced getter #################################################
    // ######################################################################################

    /**
     * Return value.
     *
     * @return value
     */
    public @NotNull String value() {
        return value;
    }

    // ######################################################################################
    // #################### constructor #####################################################
    // ######################################################################################

    /**
     * Construct an instance.
     *
     * @param value value
     */
    GitHubOAuth2Scope(@NotNull String value) {
        this.value = Objects.requireNonNull(value);
    }

}
