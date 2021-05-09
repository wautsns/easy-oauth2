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
package com.github.wautsns.easy.oauth2.core.client.function.callback;

import com.github.wautsns.easy.oauth2.core.client.model.token.AbstractRefreshableOAuth2Token;
import com.github.wautsns.easy.oauth2.core.exception.OAuth2Exception;
import org.jetbrains.annotations.NotNull;

/**
 * OAuth2 callback: after refreshing oauth2 token.
 *
 * @param <T> the type of refreshable oauth2 token
 * @author wautsns
 * @since Apr 21, 2021
 */
@FunctionalInterface
public interface OAuth2CallbackAfterRefreshingToken<T extends AbstractRefreshableOAuth2Token> {

    /**
     * Do something after refreshing token.
     *
     * @param token token
     * @param refreshedToken refreshed token
     * @throws OAuth2Exception if an oauth2 related error occurs
     */
    void afterRefreshingToken(@NotNull T token, @NotNull T refreshedToken) throws OAuth2Exception;

}
