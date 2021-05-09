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
package com.github.wautsns.easy.oauth2.extension.client.builtin;

/**
 * Builtin oauth2 platform.
 *
 * @author wautsns
 * @since May 08, 2021
 */
public enum BuiltinOAuth2Platform {

    GITEE("gitee"),
    GITHUB("github"),
    ;

    /** Identifier. */
    private final String identifier;

    // ######################################################################################
    // #################### constructor #####################################################
    // ######################################################################################

    /**
     * Construct an instance.
     *
     * @param identifier identifier
     */
    BuiltinOAuth2Platform(String identifier) {
        this.identifier = identifier;
    }

    // ######################################################################################
    // #################### getter / setter #################################################
    // ######################################################################################

    public String getIdentifier() {
        return identifier;
    }

}
