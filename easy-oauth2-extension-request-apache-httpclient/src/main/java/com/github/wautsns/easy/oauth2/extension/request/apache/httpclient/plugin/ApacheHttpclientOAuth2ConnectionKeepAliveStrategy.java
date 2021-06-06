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
package com.github.wautsns.easy.oauth2.extension.request.apache.httpclient.plugin;

import org.apache.http.HttpResponse;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.protocol.HttpContext;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;

/**
 * Apache httpclient oauth2 connection keep alive strategy.
 *
 * @author wautsns
 * @since May 26, 2021
 */
public final class ApacheHttpclientOAuth2ConnectionKeepAliveStrategy
        extends DefaultConnectionKeepAliveStrategy {

    /** Keep alive timeout millis. */
    private final long keepAliveTimeoutMillis;

    // ##################################################################################
    // #################### enhanced getter #############################################
    // ##################################################################################

    @Override
    public long getKeepAliveDuration(HttpResponse response, HttpContext context) {
        long value = super.getKeepAliveDuration(response, context);
        return (value < 0) ? keepAliveTimeoutMillis : value;
    }

    // ##################################################################################
    // #################### constructor #################################################
    // ##################################################################################

    /**
     * Construct an instance.
     *
     * @param keepAliveTimeout keep alive timeout
     */
    public ApacheHttpclientOAuth2ConnectionKeepAliveStrategy(@NotNull Duration keepAliveTimeout) {
        this.keepAliveTimeoutMillis = keepAliveTimeout.toMillis();
    }

}
