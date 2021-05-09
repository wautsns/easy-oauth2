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

import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import java.util.Objects;

/**
 * OAuth2 I/O exception.
 *
 * @author wautsns
 * @since Mar 27, 2021
 */
public final class OAuth2IOException extends OAuth2Exception {

    private static final long serialVersionUID = -7948206779339470578L;

    // ######################################################################################
    // #################### constructor #####################################################
    // ######################################################################################

    /**
     * Construct an instance.
     *
     * @param cause cause
     */
    public OAuth2IOException(@NotNull IOException cause) {
        super(Objects.requireNonNull(cause));
    }

    // ######################################################################################
    // #################### getter / setter #################################################
    // ######################################################################################

    @Override
    public synchronized @NotNull IOException getCause() {
        return (IOException) super.getCause();
    }

}
