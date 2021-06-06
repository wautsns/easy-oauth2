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
package com.github.wautsns.easy.oauth2.core.exception.specific;

import com.github.wautsns.easy.oauth2.core.exception.OAuth2Exception;

import org.jetbrains.annotations.Nullable;

/**
 * OAuth2 refresh token expired exception.
 *
 * @author wautsns
 * @since Apr 21, 2021
 */
public final class OAuth2RefreshTokenExpiredException extends OAuth2Exception {

    private static final long serialVersionUID = -4789877569575329104L;

    // ##################################################################################
    // #################### constructor #################################################
    // ##################################################################################

    /** Construct an instance. */
    public OAuth2RefreshTokenExpiredException() {
        super("Refresh token has expired.");
    }

    /**
     * Construct an instance.
     *
     * @param message message
     */
    public OAuth2RefreshTokenExpiredException(@Nullable String message) {
        super((message != null) ? message : "Refresh token has expired.");
    }

}
