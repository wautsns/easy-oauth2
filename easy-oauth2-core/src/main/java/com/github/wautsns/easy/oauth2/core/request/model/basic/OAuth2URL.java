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

    // ######################################################################################
    // #################### enhanced getter #################################################
    // ######################################################################################

    /**
     * Return url without query and anchor.
     *
     * @return url without query and anchor
     */
    public @NotNull String urlWithoutQueryAndAnchor() {
        return urlWithoutQueryAndAnchor;
    }

    /**
     * Return query.
     *
     * @return query
     */
    public @NotNull OAuth2URLQuery query() {
        return query;
    }

    /**
     * Return url encoded anchor.
     *
     * @return url encoded anchor, or {@code null} if the anchor does not assign
     */
    public @Nullable String anchor() {
        return anchor;
    }

    // ######################################################################################

    /**
     * Return url in text format.
     *
     * @return url in text format
     */
    public @NotNull String asText() {
        StringBuilder url = new StringBuilder();
        url.append(urlWithoutQueryAndAnchor);
        url.append(query.asText());
        if (anchor != null) { url.append('#').append(anchor); }
        return url.toString();
    }

    /**
     * Return a new instance by deep copying {@code this} object.
     *
     * @return a copy of {@code this} object
     */
    public @NotNull OAuth2URL copy() {
        return new OAuth2URL(this);
    }

    // ######################################################################################
    // #################### enhanced setter #################################################
    // ######################################################################################

    /**
     * Assign anchor.
     *
     * <ul>
     * <li style="list-style-type:none">########## Notes ###############</li>
     * <li>If the {@code anchor} is {@code null}, the anchor will not appear in {@link #toString()}</li>
     * <li>The {@code anchor} will be automatically url encoded.</li>
     * </ul>
     *
     * @param anchor anchor
     * @return self reference
     */
    public @NotNull OAuth2URL anchor(@Nullable String anchor) {
        this.anchor = OAuth2DataUtils.encodeWithURLEncoder(anchor);
        return this;
    }

    // ######################################################################################
    // #################### constructor #####################################################
    // ######################################################################################

    /**
     * Construct an instance.
     *
     * @param urlWithoutQueryAndAnchor url without query and anchor
     * @param estimatedNumberOfQueryUnitNames estimated number of query unit names
     */
    public OAuth2URL(@NotNull String urlWithoutQueryAndAnchor, int estimatedNumberOfQueryUnitNames) {
        this.urlWithoutQueryAndAnchor = Objects.requireNonNull(urlWithoutQueryAndAnchor);
        this.query = new OAuth2URLQuery(estimatedNumberOfQueryUnitNames);
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

    // ######################################################################################
    // #################### stringifier #####################################################
    // ######################################################################################

    @Override
    public @NotNull String toString() {
        return asText();
    }

}
