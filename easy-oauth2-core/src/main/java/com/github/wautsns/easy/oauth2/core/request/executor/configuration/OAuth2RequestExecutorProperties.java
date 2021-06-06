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
package com.github.wautsns.easy.oauth2.core.request.executor.configuration;

import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * OAuth2 request executor properties.
 *
 * @author wautsns
 * @since Mar 30, 2021
 */
public final class OAuth2RequestExecutorProperties {

    /**
     * Default request executor properties.
     *
     * <ul>
     * <li style="list-style-type:none">########## Default Properties  ###############</li>
     * <li>connectTimeout: {@link Duration#parse(CharSequence) Duration.parse("PT2S")}</li>
     * <li>socketTimeout: {@link Duration#parse(CharSequence) Duration.parse("PT5S")}</li>
     * <li>maxConcurrentRequests: {@code 64}</li>
     * <li>maxIdleTime: {@link Duration#parse(CharSequence) Duration.parse("PT5M")}</li>
     * <li>keepAliveTimeout: {@link Duration#parse(CharSequence) Duration.parse("PT3M")}</li>
     * <li>retryTimes: {@code 1}</li>
     * <li>proxy: {@code null}</li>
     * <li>custom: {@code null}</li>
     * </ul>
     */
    public static final @NotNull OAuth2RequestExecutorProperties DEFAULT =
            new OAuth2RequestExecutorProperties()
                    .setConnectTimeout(Duration.parse("PT2S"))
                    .setSocketTimeout(Duration.parse("PT5S"))
                    .setMaxConcurrentRequests(64)
                    .setMaxIdleTime(Duration.parse("PT5M"))
                    .setKeepAliveTimeout(Duration.parse("PT3M"))
                    .setRetryTimes(1)
                    .setProxy(null)
                    .setCustom(null);

    // ##################################################################################

    /** Connect timeout. */
    private Duration connectTimeout;
    /** Socket timeout. */
    private Duration socketTimeout;
    /** Max concurrent requests. */
    private Integer maxConcurrentRequests;
    /** Max idle time of connection. */
    private Duration maxIdleTime;
    /** Keep alive timeout of connection. */
    private Duration keepAliveTimeout;
    /** Retry times. */
    private Integer retryTimes;
    /** Proxy. */
    private String proxy;
    /** Custom properties. */
    private Map<String, String> custom;

    // ##################################################################################
    // #################### enhanced setter #############################################
    // ##################################################################################

    /**
     * Rewrite properties which value is {@code null} to {@link #DEFAULT} value.
     *
     * <ul>
     * <li style="list-style-type:none">########## Notes ###############</li>
     * <li>Even after the method is executed, there is no guarantee that properties is certainly
     * non-null (dependent on {@link #DEFAULT}).</li>
     * </ul>
     *
     * @return self reference
     */
    public @NotNull OAuth2RequestExecutorProperties rewriteNullToDefaultValue() {
        if (connectTimeout == null) {
            connectTimeout = DEFAULT.connectTimeout;
        }
        if (socketTimeout == null) {
            socketTimeout = DEFAULT.socketTimeout;
        }
        if (maxConcurrentRequests == null) {
            maxConcurrentRequests = DEFAULT.maxConcurrentRequests;
        }
        if (maxIdleTime == null) {
            maxIdleTime = DEFAULT.maxIdleTime;
        }
        if (keepAliveTimeout == null) {
            keepAliveTimeout = DEFAULT.keepAliveTimeout;
        }
        if (retryTimes == null) {
            retryTimes = DEFAULT.retryTimes;
        }
        if (proxy == null) {
            proxy = DEFAULT.proxy;
        }
        if ((custom == null) && (DEFAULT.custom != null)) {
            custom = new HashMap<>(DEFAULT.custom);
        }
        return this;
    }

    // ##################################################################################
    // #################### stringifier #################################################
    // ##################################################################################

    @Override
    public @NotNull String toString() {
        return "{connectTimeout=" + connectTimeout +
                ", socketTimeout=" + socketTimeout +
                ", maxConcurrentRequests=" + maxConcurrentRequests +
                ", maxIdleTime=" + maxIdleTime +
                ", keepAliveTimeout=" + keepAliveTimeout +
                ", retryTimes=" + retryTimes +
                ", proxy=" + proxy +
                ", custom=" + custom +
                '}';
    }

    // ##################################################################################
    // #################### getter / setter #############################################
    // ##################################################################################

    public Duration getConnectTimeout() {
        return connectTimeout;
    }

    public @NotNull OAuth2RequestExecutorProperties setConnectTimeout(Duration connectTimeout) {
        this.connectTimeout = connectTimeout;
        return this;
    }

    public Duration getSocketTimeout() {
        return socketTimeout;
    }

    public @NotNull OAuth2RequestExecutorProperties setSocketTimeout(Duration socketTimeout) {
        this.socketTimeout = socketTimeout;
        return this;
    }

    public Integer getMaxConcurrentRequests() {
        return maxConcurrentRequests;
    }

    public @NotNull OAuth2RequestExecutorProperties setMaxConcurrentRequests(
            Integer maxConcurrentRequests) {
        this.maxConcurrentRequests = maxConcurrentRequests;
        return this;
    }

    public Duration getMaxIdleTime() {
        return maxIdleTime;
    }

    public @NotNull OAuth2RequestExecutorProperties setMaxIdleTime(Duration maxIdleTime) {
        this.maxIdleTime = maxIdleTime;
        return this;
    }

    public Duration getKeepAliveTimeout() {
        return keepAliveTimeout;
    }

    public @NotNull OAuth2RequestExecutorProperties setKeepAliveTimeout(Duration keepAliveTimeout) {
        this.keepAliveTimeout = keepAliveTimeout;
        return this;
    }

    public Integer getRetryTimes() {
        return retryTimes;
    }

    public @NotNull OAuth2RequestExecutorProperties setRetryTimes(Integer retryTimes) {
        this.retryTimes = retryTimes;
        return this;
    }

    public String getProxy() {
        return proxy;
    }

    public @NotNull OAuth2RequestExecutorProperties setProxy(String proxy) {
        this.proxy = proxy;
        return this;
    }

    public Map<@NotNull String, String> getCustom() {
        return custom;
    }

    public @NotNull OAuth2RequestExecutorProperties setCustom(Map<String, String> custom) {
        this.custom = custom;
        return this;
    }

}
