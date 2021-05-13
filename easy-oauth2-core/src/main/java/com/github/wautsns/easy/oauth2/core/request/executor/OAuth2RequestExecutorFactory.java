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

    // ##################################################################################
    // #################### enhanced getter #############################################
    // ##################################################################################

    /**
     * Return whether {@code this} factory is enabled.
     *
     * @return {@code true} if {@code this} factory is enabled, otherwise {@code false}
     * @implNote The default implementation always returns {@code true}.
     */
    default boolean enabled() {
        return true;
    }

    /**
     * Return identifier of {@code this} factory.
     *
     * <ul>
     * <li style="list-style-type:none">########## Notes ###############</li>
     * <li>The default implementation is {@code getClass().getCanonicalName()}.</li>
     * </ul>
     *
     * @return identifier of {@code this} factory
     */
    default @NotNull String identifier() {
        return getClass().getCanonicalName();
    }

    // ##################################################################################
    // #################### create ######################################################
    // ##################################################################################

    /**
     * Create a default request executor.
     *
     * <ul>
     * <li style="list-style-type:none">########## Notes ###############</li>
     * <li>The request executor is created with {@link OAuth2RequestExecutorProperties#DEFAULT}.</li>
     * </ul>
     *
     * @return request executor
     * @implNote Under normal circumstances, the method should not be overridden.
     */
    default @NotNull AbstractOAuth2RequestExecutor<Q> create() {
        return create(OAuth2RequestExecutorProperties.DEFAULT);
    }

    /**
     * Create a request executor.
     *
     * @param properties request executor properties
     * @return request executor
     */
    @NotNull AbstractOAuth2RequestExecutor<Q> create(@NotNull OAuth2RequestExecutorProperties properties);

}
