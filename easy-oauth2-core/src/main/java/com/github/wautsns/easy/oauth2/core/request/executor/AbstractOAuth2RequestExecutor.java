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
package com.github.wautsns.easy.oauth2.core.request.executor;

import com.github.wautsns.easy.oauth2.core.exception.OAuth2Exception;
import com.github.wautsns.easy.oauth2.core.exception.OAuth2IOException;
import com.github.wautsns.easy.oauth2.core.request.model.basic.OAuth2Headers;
import com.github.wautsns.easy.oauth2.core.request.model.request.AbstractOAuth2RequestEntity;
import com.github.wautsns.easy.oauth2.core.request.model.request.OAuth2Request;
import com.github.wautsns.easy.oauth2.core.request.model.request.OAuth2RequestMethod;
import com.github.wautsns.easy.oauth2.core.request.model.response.AbstractOAuth2Response;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Abstract oauth2 request executor.
 *
 * @param <Q> the actual type of request delegate
 * @author wautsns
 * @since Mar 27, 2021
 */
public abstract class AbstractOAuth2RequestExecutor<Q> {

    /** Logger. */
    protected final @NotNull Logger log = LoggerFactory.getLogger(getClass());

    // ##################################################################################

    /**
     * Execute the given {@code request}.
     *
     * @param request request
     * @return response
     * @throws OAuth2Exception if an oauth2 related error occurs
     */
    public final @NotNull AbstractOAuth2Response execute(@NotNull OAuth2Request<?> request)
            throws OAuth2Exception {
        OAuth2RequestMethod method = request.method();
        String url = request.url().writeAsText();
        Q requestDelegate = initializeRequestDelegate(method, url);
        OAuth2Headers headers = request.headers();
        if (headers != null) {
            headers.forEach((name, value) -> {
                addRequestDelegateHeader(requestDelegate, name, value);
            });
        }
        AbstractOAuth2RequestEntity entity = request.entity();
        if (entity != null) {
            setRequestDelegateContentTypeAndEntity(requestDelegate, entity);
        }
        try {
            log.debug("Ready to execute request. request: {}", request);
            AbstractOAuth2Response response = executeRequestDelegate(requestDelegate);
            log.debug(
                    "Request has been executed. status: {}, request: {}",
                    response.status(), request
            );
            return response;
        } catch (IOException e) {
            log.error("Failed to execute request due to IOException. request: {}", request, e);
            throw new OAuth2IOException(e);
        } catch (Exception e) {
            log.error("Failed to execute request due to Exception. request: {}", request, e);
            throw new OAuth2Exception(e);
        }
    }

    // ##################################################################################
    // #################### request delegate related operation ##########################
    // ##################################################################################

    /**
     * Initialize request delegate.
     *
     * @param method method
     * @param url url
     * @return request delegate
     */
    protected abstract @NotNull Q initializeRequestDelegate(
            @NotNull OAuth2RequestMethod method, @NotNull String url);

    /**
     * Add request delegate header.
     *
     * @param requestDelegate request delegate
     * @param name header name
     * @param value header value
     */
    protected abstract void addRequestDelegateHeader(
            @NotNull Q requestDelegate, @NotNull String name, @NotNull String value);

    /**
     * Set request delegate content type and entity.
     *
     * @param requestDelegate request delegate
     * @param entity entity
     * @see AbstractOAuth2RequestEntity#contentType()
     * @see AbstractOAuth2RequestEntity#writeAsBytes()
     */
    protected abstract void setRequestDelegateContentTypeAndEntity(
            @NotNull Q requestDelegate, @NotNull AbstractOAuth2RequestEntity entity);

    /**
     * Execute the given {@code requestDelegate}.
     *
     * @param requestDelegate request delegate
     * @return response
     * @throws IOException if an I/O error occurs
     */
    protected abstract @NotNull AbstractOAuth2Response executeRequestDelegate(
            @NotNull Q requestDelegate) throws IOException;

}
