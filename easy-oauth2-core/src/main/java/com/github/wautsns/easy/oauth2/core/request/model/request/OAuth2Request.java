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
package com.github.wautsns.easy.oauth2.core.request.model.request;

import com.github.wautsns.easy.oauth2.core.request.model.basic.OAuth2Headers;
import com.github.wautsns.easy.oauth2.core.request.model.basic.OAuth2URL;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.Objects;

/**
 * OAuth2 request.
 *
 * @param <E> the type of entity
 * @author wautsns
 * @since Mar 27, 2021
 */
public final class OAuth2Request<E extends AbstractOAuth2RequestEntity> {

    /** Method. */
    private final @NotNull OAuth2RequestMethod method;
    /** URL. */
    private final @NotNull OAuth2URL url;
    /** Headers. */
    private @Nullable OAuth2Headers headers;
    /** Entity. */
    private @Nullable E entity;

    // ######################################################################################
    // #################### enhanced getter #################################################
    // ######################################################################################

    /**
     * Return method.
     *
     * @return method
     */
    public @NotNull OAuth2RequestMethod method() {
        return method;
    }

    /**
     * Return url.
     *
     * @return url
     */
    public @NotNull OAuth2URL url() {
        return url;
    }

    /**
     * Return headers.
     *
     * @return headers, or {@code null} if headers do not assign
     */
    public @Nullable OAuth2Headers headers() {
        return headers;
    }

    /**
     * Return entity.
     *
     * @return entity, or {@code null} if entity does not assign
     */
    public @Nullable E entity() {
        return entity;
    }

    // ######################################################################################

    /**
     * Return a new instance by deep copying {@code this} object.
     *
     * @param shareURL whether to share url
     * @param shareHeaders whether to share headers
     * @param shareEntity whether to share entity
     * @return a copy of {@code this} object
     */
    @SuppressWarnings("unchecked")
    public @NotNull OAuth2Request<E> copy(boolean shareURL, boolean shareHeaders, boolean shareEntity) {
        return new OAuth2Request<>(
                method, shareURL ? this.url : this.url.copy(),
                (shareHeaders || (this.headers == null)) ? this.headers : this.headers.copy(),
                (E) ((shareEntity || (this.entity == null)) ? this.entity : this.entity.copy())
        );
    }

    // ######################################################################################
    // #################### enhanced setter #################################################
    // ######################################################################################

    /**
     * Assign headers.
     *
     * @param headers headers
     * @return self reference
     */
    public @NotNull OAuth2Request<E> headers(@Nullable OAuth2Headers headers) {
        this.headers = headers;
        return this;
    }

    /**
     * Assign entity.
     *
     * @param entity entity
     * @return self reference
     */
    public @NotNull OAuth2Request<E> entity(@Nullable E entity) {
        this.entity = entity;
        return this;
    }

    // ######################################################################################
    // #################### constructor #####################################################
    // ######################################################################################

    /**
     * Construct an instance.
     *
     * @param method method
     * @param url url
     */
    public OAuth2Request(@NotNull OAuth2RequestMethod method, @NotNull OAuth2URL url) {
        this.method = Objects.requireNonNull(method);
        this.url = Objects.requireNonNull(url);
    }

    /**
     * Construct an instance.
     *
     * @param method method
     * @param url url
     * @param headers headers
     * @param entity request entity
     * @see #copy(boolean, boolean, boolean)
     */
    protected OAuth2Request(
            @NotNull OAuth2RequestMethod method, @NotNull OAuth2URL url,
            @Nullable OAuth2Headers headers, @Nullable E entity) {
        this.method = method;
        this.url = url;
        this.headers = headers;
        this.entity = entity;
    }

    // ######################################################################################
    // #################### stringifier #####################################################
    // ######################################################################################

    @Override
    public @NotNull String toString() {
        return "{method=" + method +
                ", url=" + url +
                ", headers=" + headers +
                ", entity=" + entity +
                '}';
    }

}
