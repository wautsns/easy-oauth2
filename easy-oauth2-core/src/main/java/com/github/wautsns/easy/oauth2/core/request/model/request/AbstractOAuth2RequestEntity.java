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

import org.jetbrains.annotations.NotNull;

/**
 * OAuth2 request entity.
 *
 * @author wautsns
 * @since Mar 29, 2021
 */
public abstract class AbstractOAuth2RequestEntity {

    // ######################################################################################
    // #################### enhanced getter #################################################
    // ######################################################################################

    /**
     * Return content type.
     *
     * @return content type
     */
    public abstract @NotNull String contentType();

    /**
     * Return bytes of {@code this} entity.
     *
     * @return bytes of {@code this} entity
     */
    public abstract byte @NotNull [] bytes();

    /**
     * Return a new instance by deep copying {@code this} object.
     *
     * @return a copy of {@code this} object
     */
    public abstract @NotNull AbstractOAuth2RequestEntity copy();

}
