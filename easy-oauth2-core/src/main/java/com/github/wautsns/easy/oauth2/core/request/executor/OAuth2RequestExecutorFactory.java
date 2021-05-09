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

import com.github.wautsns.easy.oauth2.core.request.executor.configuration.OAuth2RequestExecutorProperties;
import org.jetbrains.annotations.NotNull;

/**
 * OAuth2 request executor factory.
 *
 * @param <Q> the type of actual request
 * @author wautsns
 * @since Mar 30, 2021
 */
public interface OAuth2RequestExecutorFactory<Q> {

    // ######################################################################################
    // #################### enhanced getter #################################################
    // ######################################################################################

    /**
     * Return identifier of {@code this} factory.
     *
     * @return identifier of {@code this} factory
     */
    default @NotNull String identifier() {
        return getClass().getCanonicalName();
    }

    /**
     * Return whether {@code this} factory is enabled.
     *
     * @return {@code true} if {@code this} factory is enabled, otherwise {@code false}
     */
    boolean isEnabled();

    /**
     * Create an oauth2 request executor.
     *
     * @param properties oauth2 request executor properties
     * @return oauth2 request executor
     */
    @NotNull AbstractOAuth2RequestExecutor<Q> create(@NotNull OAuth2RequestExecutorProperties properties);

}
