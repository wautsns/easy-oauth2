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

import com.github.wautsns.easy.oauth2.core.request.executor.AbstractOAuth2RequestExecutor;
import com.github.wautsns.easy.oauth2.core.request.executor.configuration.OAuth2RequestExecutorProperties;
import com.github.wautsns.easy.oauth2.core.request.model.request.AbstractOAuth2RequestEntity;
import com.github.wautsns.easy.oauth2.core.request.model.request.OAuth2RequestMethod;
import com.github.wautsns.easy.oauth2.core.request.model.response.AbstractOAuth2Response;
import com.github.wautsns.easy.oauth2.extension.request.apache.httpclient.model.ApacheHttpclientOAuth2Response;
import com.github.wautsns.easy.oauth2.extension.request.apache.httpclient.plugin.ApacheHttpclientOAuth2ConnectionKeepAliveStrategy;
import com.github.wautsns.easy.oauth2.extension.request.apache.httpclient.plugin.ApacheHttpclientOAuth2RequestRetryHandler;

import org.apache.http.HttpHost;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * Apache httpclient oauth2 request executor.
 *
 * @author wautsns
 * @see <a href="https://hc.apache.org/httpcomponents-client-4.5.x/index.html">apache
 *         httpcomponents-client-4.5.x</a>
 * @since Mar 30, 2021
 */
public final class ApacheHttpclientOAuth2RequestExecutor
        extends AbstractOAuth2RequestExecutor<HttpRequestBase> {

    /** Delegate. */
    private final @NotNull HttpClient delegate;

    // ##################################################################################
    // #################### request delegate related operation ##########################
    // ##################################################################################

    @Override
    protected @NotNull HttpRequestBase initializeRequestDelegate(
            @NotNull OAuth2RequestMethod method, @NotNull String url) {
        switch (method) {
            case GET:
                return new HttpGet(url);
            case POST:
                return new HttpPost(url);
            case PUT:
                return new HttpPut(url);
            case PATCH:
                return new HttpPatch(url);
            case DELETE:
                return new HttpDelete(url);
            case OPTIONS:
                return new HttpOptions(url);
            case HEAD:
                return new HttpHead(url);
            default:
                throw new IllegalStateException();
        }
    }

    @Override
    protected void addRequestDelegateHeader(
            @NotNull HttpRequestBase requestDelegate, @NotNull String name, @NotNull String value) {
        requestDelegate.addHeader(name, value);
    }

    @Override
    protected void setRequestDelegateContentTypeAndEntity(
            @NotNull HttpRequestBase requestDelegate, @NotNull AbstractOAuth2RequestEntity entity) {
        if (requestDelegate instanceof HttpEntityEnclosingRequestBase) {
            ByteArrayEntity entityDelegate = new ByteArrayEntity(entity.writeAsBytes());
            entityDelegate.setContentType(entity.contentType());
            ((HttpEntityEnclosingRequestBase) requestDelegate).setEntity(entityDelegate);
        }
    }

    @Override
    protected @NotNull AbstractOAuth2Response executeRequestDelegate(
            @NotNull HttpRequestBase requestDelegate) throws IOException {
        return new ApacheHttpclientOAuth2Response(delegate.execute(requestDelegate));
    }

    // ##################################################################################
    // #################### constructor #################################################
    // ##################################################################################

    /**
     * Construct an instance.
     *
     * @param properties properties
     */
    public ApacheHttpclientOAuth2RequestExecutor(
            @NotNull OAuth2RequestExecutorProperties properties) {
        HttpClientBuilder builder = HttpClientBuilder.create();
        // Set request config.
        RequestConfig.Builder requestConfigBuilder = RequestConfig.custom();
        Duration connectTimeout = properties.getConnectTimeout();
        if (connectTimeout != null) {
            requestConfigBuilder.setConnectTimeout((int) connectTimeout.toMillis());
        }
        Duration socketTimeout = properties.getSocketTimeout();
        if (socketTimeout != null) {
            requestConfigBuilder.setSocketTimeout((int) socketTimeout.toMillis());
        }
        builder.setDefaultRequestConfig(requestConfigBuilder.build());
        // Set connection manager.
        PoolingHttpClientConnectionManager connectionManager =
                new PoolingHttpClientConnectionManager();
        Integer maxConcurrentRequests = properties.getMaxConcurrentRequests();
        if (maxConcurrentRequests != null) {
            connectionManager.setMaxTotal(maxConcurrentRequests);
            connectionManager.setDefaultMaxPerRoute(maxConcurrentRequests);
        }
        builder.setConnectionManager(connectionManager);
        // Set max idle time.
        Duration maxIdleTime = properties.getMaxIdleTime();
        if (maxIdleTime != null) {
            builder.evictIdleConnections(maxIdleTime.toMillis(), TimeUnit.MILLISECONDS);
        }
        // Set keep alive strategy.
        Duration keepAliveTimeout = properties.getKeepAliveTimeout();
        if (keepAliveTimeout != null) {
            builder.setKeepAliveStrategy(
                    new ApacheHttpclientOAuth2ConnectionKeepAliveStrategy(keepAliveTimeout)
            );
        }
        // Set retry handler.
        Integer retryTimes = properties.getRetryTimes();
        if ((retryTimes != null) && (retryTimes >= 1)) {
            builder.setRetryHandler(new ApacheHttpclientOAuth2RequestRetryHandler(retryTimes));
        }
        // Set proxy.
        String proxy = properties.getProxy();
        if (proxy != null) {
            builder.setProxy(HttpHost.create(proxy));
        }
        // Build apache http client.
        this.delegate = builder.build();
    }

}
