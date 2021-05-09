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
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * OAuth2 request executor based on apache httpclient.
 *
 * @author wautsns
 * @since Mar 30, 2021
 */
public final class OAuth2RequestExecutorBasedOnApacheHttpclient extends AbstractOAuth2RequestExecutor<HttpRequestBase> {

    /** Raw http client. */
    private final @NotNull HttpClient raw;

    // #########################################################################################
    // #################### implement protected abstract method ################################
    // #########################################################################################

    @Override
    protected @NotNull HttpRequestBase initializeActualRequest(
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
                throw new IllegalStateException(String.format("Illegal method: %s", method));
        }
    }

    @Override
    protected void addHeader(@NotNull HttpRequestBase actualRequest, @NotNull String name, @NotNull String value) {
        actualRequest.addHeader(name, value);
    }

    @Override
    protected void setContentTypeAndEntity(
            @NotNull HttpRequestBase actualRequest, @NotNull AbstractOAuth2RequestEntity entity) {
        if (actualRequest instanceof HttpEntityEnclosingRequestBase) {
            ByteArrayEntity actualEntity = new ByteArrayEntity(entity.bytes());
            actualEntity.setContentType(entity.contentType());
            ((HttpEntityEnclosingRequestBase) actualRequest).setEntity(actualEntity);
        }
    }

    @Override
    protected @NotNull AbstractOAuth2Response executeActualRequest(@NotNull HttpRequestBase actualRequest) throws IOException {
        return new OAuth2ResponseBasedOnApacheHttpclient(raw.execute(actualRequest));
    }

    // ######################################################################################
    // #################### constructor #####################################################
    // ######################################################################################

    /**
     * Construct an instance.
     *
     * @param properties request executor properties
     */
    public OAuth2RequestExecutorBasedOnApacheHttpclient(@NotNull OAuth2RequestExecutorProperties properties) {
        HttpClientBuilder builder = HttpClientBuilder.create();
        // Set request config.
        RequestConfig.Builder requestConfigBuilder = RequestConfig.custom();
        if (properties.getConnectTimeout() != null) {
            requestConfigBuilder.setConnectTimeout((int) properties.getConnectTimeout().toMillis());
        }
        if (properties.getReadTimeout() != null) {
            requestConfigBuilder.setSocketTimeout((int) properties.getReadTimeout().toMillis());
        }
        builder.setDefaultRequestConfig(requestConfigBuilder.build());
        // Set connect manager.
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        if (properties.getMaxConcurrentRequests() != null) {
            connectionManager.setMaxTotal(properties.getMaxConcurrentRequests());
            connectionManager.setDefaultMaxPerRoute(properties.getMaxConcurrentRequests());
        }
        builder.setConnectionManager(connectionManager);
        // Set max idle time.
        if (properties.getMaxIdleTime() != null) {
            builder.evictIdleConnections(properties.getMaxIdleTime().toMillis(), TimeUnit.MILLISECONDS);
        }
        // Set keep alive.
        if (properties.getKeepAliveTimeout() != null) {
            long keepAliveTimeoutMillis = properties.getKeepAliveTimeout().toMillis();
            ConnectionKeepAliveStrategy keepAliveStrategy = (resp, ctx) -> keepAliveTimeoutMillis;
            builder.setKeepAliveStrategy(keepAliveStrategy);
        }
        // Set retry handler.
        if ((properties.getRetryTimes() != null) && (properties.getRetryTimes() >= 1)) {
            builder.setRetryHandler(new OAuth2RequestRetryHandlerBasedOnApacheHttpclient(properties.getRetryTimes()));
        }
        // Set proxy.
        if (properties.getProxy() != null) {
            builder.setProxy(HttpHost.create(properties.getProxy()));
        }
        // Build apache http client.
        this.raw = builder.build();
        log.info("Request executor has been initialized with properties: {}", properties);
    }

}
