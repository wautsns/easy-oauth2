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
package com.github.wautsns.easy.oauth2.core.client.function.api;

import com.github.wautsns.easy.oauth2.core.client.model.OAuth2CallbackQuery;
import com.github.wautsns.easy.oauth2.core.client.model.user.AbstractOAuth2User;
import com.github.wautsns.easy.oauth2.core.exception.OAuth2Exception;
import org.jetbrains.annotations.NotNull;

/**
 * OAuth2 api: exchange oauth2 callback query for oauth2 user.
 *
 * @param <U> the type of oauth2 user
 * @author wautsns
 * @since Mar 31, 2021
 */
@FunctionalInterface
public interface OAuth2APIExchangeCallbackQueryForUser<U extends AbstractOAuth2User> {

    /**
     * Exchange callback query for user.
     *
     * @param query callback query
     * @return user
     * @throws OAuth2Exception if an oauth2 related error occurs
     */
    @NotNull U exchangeForUser(@NotNull OAuth2CallbackQuery query) throws OAuth2Exception;

}
