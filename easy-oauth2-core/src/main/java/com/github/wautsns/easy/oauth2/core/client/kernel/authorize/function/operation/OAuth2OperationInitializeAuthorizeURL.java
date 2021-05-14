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
package com.github.wautsns.easy.oauth2.core.client.kernel.authorize.function.operation;

import com.github.wautsns.easy.oauth2.core.exception.OAuth2Exception;
import com.github.wautsns.easy.oauth2.core.request.model.basic.OAuth2URL;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * OAuth2 operation: initialize authorize url.
 *
 * @author wautsns
 * @since Apr 01, 2021
 */
@FunctionalInterface
public interface OAuth2OperationInitializeAuthorizeURL {

    // ##################################################################################
    // #################### oauth2 operation ############################################
    // ##################################################################################

    /**
     * Initialize authorize url.
     *
     * <ul>
     * <li style="list-style-type:none">########## Notes ###############</li>
     * <li>state: The application generates a random string and includes it in the request. It should then check that
     * the same value is returned after the user authorizes the app. This is used to prevent CSRF attacks.</li>
     * </ul>
     *
     * @param state state
     * @return authorize url
     * @throws OAuth2Exception if an oauth2 related error occurs
     */
    @NotNull OAuth2URL initializeAuthorizeURL(@Nullable String state) throws OAuth2Exception;

}
