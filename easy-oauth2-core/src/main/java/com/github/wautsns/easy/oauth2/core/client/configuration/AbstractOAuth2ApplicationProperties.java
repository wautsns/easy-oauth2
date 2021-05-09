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

/**
 * Abstract oauth2 application properties.
 *
 * @author wautsns
 * @since Apr 01, 2021
 */
public abstract class AbstractOAuth2ApplicationProperties implements OAuth2PlatformIdentifierSupplier {

    // ######################################################################################
    // #################### validate ########################################################
    // ######################################################################################

    /** Validate {@code this} properties. */
    public abstract void validate();

}
