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
package com.github.wautsns.easy.oauth2.extension.request.apache.httpclient;

import com.github.wautsns.easy.oauth2.core.request.executor.OAuth2RequestExecutorFactory;
import com.github.wautsns.easy.oauth2.core.request.executor.configuration.OAuth2RequestExecutorProperties;

import org.apache.http.client.methods.HttpRequestBase;
import org.jetbrains.annotations.NotNull;

/**
 * Apache httpclient oauth2 request executor factory.
 *
 * @author wautsns
 * @since Mar 30, 2021
 */
public final class ApacheHttpclientOAuth2RequestExecutorFactory
        implements OAuth2RequestExecutorFactory<HttpRequestBase> {

    @Override
    public boolean enabled() {
        try {
            Class.forName("org.apache.http.client.HttpClient");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    @Override
    public @NotNull ApacheHttpclientOAuth2RequestExecutor create(
            @NotNull OAuth2RequestExecutorProperties properties) {
        return new ApacheHttpclientOAuth2RequestExecutor(properties);
    }

}
