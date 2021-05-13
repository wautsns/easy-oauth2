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
package com.github.wautsns.easy.oauth2.core.exception;

import org.jetbrains.annotations.Nullable;

/**
 * OAuth2 related exception.
 *
 * @author wautsns
 * @since Mar 27, 2021
 */
public class OAuth2Exception extends Exception {

    private static final long serialVersionUID = -5300684909446266523L;

    // ##################################################################################
    // #################### constructor #################################################
    // ##################################################################################

    /**
     * Construct an instance.
     *
     * @param message message
     */
    public OAuth2Exception(@Nullable String message) {
        super(message);
    }

    /**
     * Construct an instance.
     *
     * @param cause cause
     */
    public OAuth2Exception(@Nullable Throwable cause) {
        super(cause);
    }

    /**
     * Construct an instance.
     *
     * @param cause cause
     * @param message message
     */
    public OAuth2Exception(@Nullable Throwable cause, @Nullable String message) {
        super(message, cause);
    }

}
