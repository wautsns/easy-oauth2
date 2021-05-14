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
package com.github.wautsns.easy.oauth2.core.client.kernel.authorize.configuration;

import com.github.wautsns.easy.oauth2.core.client.configuration.OAuth2PlatformSupplier;

/**
 * Abstract oauth2 authorization properties.
 *
 * @author wautsns
 * @implNote Typically, an oauth2 authorization properties include {@code scopes} and other extra authorize url
 *         query parameters. However, the names of these properties may vary greatly from platform to platform, they are
 *         all defined by the implementation class for ease of understanding.
 * @since May 05, 2021
 */
public abstract class AbstractOAuth2AuthorizationProperties implements OAuth2PlatformSupplier {

    // ##################################################################################
    // #################### validate ####################################################
    // ##################################################################################

    /**
     * Validate properties.
     *
     * @implNote Usually, you only need to check whether the value of a property is {@code null}, and whether
     *         the collection contains {@code null} elements, etc.
     */
    public abstract void validate();

}
