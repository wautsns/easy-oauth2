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
package com.github.wautsns.easy.oauth2.core.request.model.basic;

import com.github.wautsns.easy.oauth2.core.request.util.OAuth2DataUtils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * OAuth2 url.
 *
 * @author wautsns
 * @since Mar 30, 2021
 */
public final class OAuth2URL {

    /** URL without query and anchor. */
    private final @NotNull String urlWithoutQueryAndAnchor;
    /** Query. */
    private final @NotNull OAuth2URLQuery query;
    /** URL encoded anchor. */
    private @Nullable String anchor;

    // ##################################################################################
    // #################### enhanced getter #############################################
    // ##################################################################################

    /**
     * Return url without query and anchor.
     *
     * @return {@link #urlWithoutQueryAndAnchor}
     */
    public @NotNull String urlWithoutQueryAndAnchor() {
        return urlWithoutQueryAndAnchor;
    }

    /**
     * Return query.
     *
     * @return {@link #query}
     */
    public @NotNull OAuth2URLQuery query() {
        return query;
    }

    /**
     * Return anchor.
     *
     * <ul>
     * <li style="list-style-type:none">########## Notes ###############</li>
     * <li>The anchor has been url encoded.</li>
     * </ul>
     *
     * @return {@link #anchor}, or {@code null} if the anchor does not assign
     */
    public @Nullable String anchor() {
        return anchor;
    }

    // ##################################################################################

    /**
     * Write {@code this} object as text.
     *
     * @return text
     */
    public @NotNull String writeAsText() {
        StringBuilder url = new StringBuilder();
        url.append(urlWithoutQueryAndAnchor);
        url.append(query.writeAsText());
        if (anchor != null) {
            url.append('#').append(anchor);
        }
        return url.toString();
    }

    /**
     * Return new instance by deep copying {@code this} object.
     *
     * @return copy of {@code this} object
     */
    public @NotNull OAuth2URL copy() {
        return new OAuth2URL(this);
    }

    // ##################################################################################
    // #################### enhanced setter #############################################
    // ##################################################################################

    /**
     * Assign anchor.
     *
     * <ul>
     * <li style="list-style-type:none">########## Notes ###############</li>
     * <li>The {@code anchor} will be automatically url encoded.</li>
     * </ul>
     *
     * @param anchor {@link #anchor}
     * @return self reference
     */
    public @NotNull OAuth2URL anchor(@Nullable String anchor) {
        this.anchor = OAuth2DataUtils.encodeWithURLEncoder(anchor);
        return this;
    }

    // ##################################################################################
    // #################### constructor #################################################
    // ##################################################################################

    /**
     * Construct an instance.
     *
     * @param urlWithoutQueryAndAnchor {@link #urlWithoutQueryAndAnchor}
     * @param estimatedNumberOfQueryParameterNames estimated number of query parameter names
     *         names
     */
    public OAuth2URL(
            @NotNull String urlWithoutQueryAndAnchor, int estimatedNumberOfQueryParameterNames) {
        this.urlWithoutQueryAndAnchor = Objects.requireNonNull(urlWithoutQueryAndAnchor);
        this.query = new OAuth2URLQuery(estimatedNumberOfQueryParameterNames);
    }

    /**
     * Construct an instance by deep copying {@code template}.
     *
     * @param template template
     * @see #copy()
     */
    protected OAuth2URL(@NotNull OAuth2URL template) {
        this.urlWithoutQueryAndAnchor = template.urlWithoutQueryAndAnchor;
        this.query = template.query.copy();
        this.anchor = template.anchor;
    }

    // ##################################################################################
    // #################### stringifier #################################################
    // ##################################################################################

    @Override
    public @NotNull String toString() {
        return writeAsText();
    }

}
