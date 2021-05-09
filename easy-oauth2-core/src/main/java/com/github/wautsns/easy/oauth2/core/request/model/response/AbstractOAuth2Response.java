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
package com.github.wautsns.easy.oauth2.core.request.model.response;

import com.github.wautsns.easy.oauth2.core.exception.OAuth2IOException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.io.InputStream;
import java.util.List;

/**
 * Abstract oauth2 response.
 *
 * @author wautsns
 * @since Mar 27, 2021
 */
public abstract class AbstractOAuth2Response {

    // ######################################################################################
    // #################### enhanced getter #################################################
    // ######################################################################################

    /**
     * Return status.
     *
     * @return status
     */
    public abstract int status();

    // ######################################################################################

    /**
     * Return the value of the first header which the name is the given {@code name}.
     *
     * <ul>
     * <li style="list-style-type:none">########## Notes ###############</li>
     * <li>If there is no such header, {@code null} will be returned. If there are more than one matching headers, the
     * first header of {@link #headers(String)} is returned.</li>
     * </ul>
     *
     * @param name header name
     * @return value of the first header which the name is the given {@code name}, or {@code null} if not found
     */
    public abstract @Nullable String firstHeader(@NotNull String name);

    /**
     * Return the value of the last header which the name is the given {@code name}.
     *
     * <ul>
     * <li style="list-style-type:none">########## Notes ###############</li>
     * <li>If there is no such header, {@code null} will be returned. If there are more than one matching headers, the
     * last header of {@link #headers(String)} is returned.</li>
     * </ul>
     *
     * @param name header name
     * @return value of the last header which the name is the given {@code name}, or {@code null} if not found
     */
    public abstract @Nullable String lastHeader(@NotNull String name);

    /**
     * Return the values of the header which the name is the given {@code name}.
     *
     * <ul>
     * <li style="list-style-type:none">########## Notes ###############</li>
     * <li>If there is no such header, an empty list will be returned. In any case, the method will not return {@code
     * null}.</li>
     * </ul>
     *
     * @param name header name
     * @return values of the header which the name is the given {@code name}
     */
    public abstract @NotNull List<@NotNull String> headers(@NotNull String name);

    // ######################################################################################

    /**
     * Return body input stream.
     *
     * <ul>
     * <li style="list-style-type:none">########## Notes ###############</li>
     * <li>If the body is {@code null}, {@code null} will be returned.</li>
     * </ul>
     *
     * @return body input stream, or {@code null} if not exists
     * @throws OAuth2IOException if an I/O error occurs
     */
    public abstract @Nullable InputStream bodyInputStream() throws OAuth2IOException;

}
