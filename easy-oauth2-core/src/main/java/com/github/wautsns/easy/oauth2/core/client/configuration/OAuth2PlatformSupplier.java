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
package com.github.wautsns.easy.oauth2.core.client.configuration;

import org.jetbrains.annotations.NotNull;

/**
 * OAuth2 platform supplier.
 *
 * @author wautsns
 * @since Apr 22, 2021
 */
@FunctionalInterface
public interface OAuth2PlatformSupplier {

    // ##################################################################################
    // #################### enhanced getter #############################################
    // ##################################################################################

    /**
     * Return platform identifier like {@code "github"}, etc.
     *
     * @return platform identifier
     * @implNote Usually, the platform identifier is extracted from the domain name. For
     *         example, {@code "github"} is extracted from <a href="https://github.com/">
     *         https://github.com/</a>.
     */
    @NotNull String platform();

}
