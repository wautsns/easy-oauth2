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
package com.github.wautsns.easy.oauth2.extension.request.apache.httpclient.model;

import com.github.wautsns.easy.oauth2.core.exception.OAuth2IOException;
import com.github.wautsns.easy.oauth2.core.request.model.response.AbstractOAuth2Response;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Apache httpclient oauth2 response.
 *
 * @author wautsns
 * @since Mar 30, 2021
 */
public final class ApacheHttpclientOAuth2Response extends AbstractOAuth2Response {

    /** Raw response. */
    private final @NotNull HttpResponse raw;

    // ##################################################################################
    // #################### enhanced getter #############################################
    // ##################################################################################

    @Override
    public int status() {
        return raw.getStatusLine().getStatusCode();
    }

    @Override
    public @Nullable String firstHeader(@NotNull String name) {
        Header header = raw.getFirstHeader(name);
        return (header == null) ? null : header.getValue();
    }

    @Override
    public @Nullable String lastHeader(@NotNull String name) {
        Header header = raw.getLastHeader(name);
        return (header == null) ? null : header.getValue();
    }

    @Override
    public @NotNull List<@NotNull String> headers(@NotNull String name) {
        return Arrays.stream(raw.getHeaders(name))
                .map(Header::getValue)
                .collect(Collectors.toList());
    }

    @Override
    public @Nullable InputStream bodyInputStream() throws OAuth2IOException {
        try {
            HttpEntity entity = raw.getEntity();
            return (entity == null) ? null : entity.getContent();
        } catch (IOException e) {
            throw new OAuth2IOException(e);
        }
    }

    // ##################################################################################
    // #################### constructor #################################################
    // ##################################################################################

    /**
     * Construct an instance.
     *
     * @param raw {@link #raw}
     */
    public ApacheHttpclientOAuth2Response(@NotNull HttpResponse raw) {
        this.raw = Objects.requireNonNull(raw);
    }

}
