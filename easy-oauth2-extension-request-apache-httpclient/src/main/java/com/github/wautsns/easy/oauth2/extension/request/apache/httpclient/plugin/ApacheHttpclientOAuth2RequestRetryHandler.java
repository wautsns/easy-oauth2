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

import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;

import javax.net.ssl.SSLException;

/**
 * Apache httpclient oauth2 request retry handler.
 *
 * @author wautsns
 * @since Mar 30, 2021
 */
public final class ApacheHttpclientOAuth2RequestRetryHandler
        extends DefaultHttpRequestRetryHandler {

    /** The IOException types that should not be retried. */
    private static final @NotNull List<@NotNull Class<? extends IOException>> NON_RETRYABLE_CLASS_LIST =
            Arrays.asList(
                    UnknownHostException.class,
                    HttpHostConnectException.class,
                    SSLException.class
            );

    // ##################################################################################
    // #################### constructor #################################################
    // ##################################################################################

    /**
     * Construct an instance.
     *
     * @param retryTimes retry times
     */
    public ApacheHttpclientOAuth2RequestRetryHandler(int retryTimes) {
        super(retryTimes, false, NON_RETRYABLE_CLASS_LIST);
    }

}
