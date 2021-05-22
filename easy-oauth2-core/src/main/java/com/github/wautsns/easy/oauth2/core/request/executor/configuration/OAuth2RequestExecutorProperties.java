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

import com.github.wautsns.easy.oauth2.core.request.util.OAuth2DataUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
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
     * <li style="list-style-type:none">########## Notes ###############</li>
     * <li>connectTimeout: {@link Duration#parse(CharSequence) Duration.parse("PT2S")}</li>
     * <li>socketTimeout: {@link Duration#parse(CharSequence) Duration.parse("PT5S")}</li>
     * <li>maxConcurrentRequests: {@code 64}</li>
     * <li>maxIdleTime: {@link Duration#parse(CharSequence) Duration.parse("PT5M")}</li>
     * <li>keepAliveTimeout: {@link Duration#parse(CharSequence) Duration.parse("PT3M")}</li>
     * <li>retryTimes: {@code 1}</li>
     * </ul>
     */
    public static final @NotNull OAuth2RequestExecutorProperties DEFAULT =
            new OAuth2RequestExecutorProperties()
                    .setConnectTimeout(Duration.parse("PT2S"))
                    .setSocketTimeout(Duration.parse("PT5S"))
                    .setMaxConcurrentRequests(64)
                    .setMaxIdleTime(Duration.parse("PT5M"))
                    .setKeepAliveTimeout(Duration.parse("PT3M"))
                    .setRetryTimes(1);

    // ##################################################################################

    /** Connect timeout. */
    private @Nullable Duration connectTimeout;
    /** Socket timeout. */
    private @Nullable Duration socketTimeout;
    /** Max concurrent requests. */
    private @Nullable Integer maxConcurrentRequests;
    /** Max idle time of connection. */
    private @Nullable Duration maxIdleTime;
    /** Keep alive timeout of connection. */
    private @Nullable Duration keepAliveTimeout;
    /** Retry times. */
    private @Nullable Integer retryTimes;
    /** Proxy. */
    private @Nullable String proxy;
    /** Custom properties. */
    private @Nullable Map<@NotNull String, @Nullable String> custom;

    // ##################################################################################
    // #################### enhanced setter #############################################
    // ##################################################################################

    /**
     * Rewrite properties which value is {@code null} to {@link #DEFAULT} value.
     *
     * <ul>
     * <li style="list-style-type:none">########## Notes ###############</li>
     * <li>Even after the method is executed, there is no guarantee that properties is certainly non-null (dependent on
     * {@link #DEFAULT}).</li>
     * </ul>
     *
     * @return self reference
     */
    public @NotNull OAuth2RequestExecutorProperties rewriteNullToDefaultValue() {
        if (connectTimeout == null) { connectTimeout = DEFAULT.connectTimeout; }
        if (socketTimeout == null) { socketTimeout = DEFAULT.socketTimeout; }
        if (maxConcurrentRequests == null) { maxConcurrentRequests = DEFAULT.maxConcurrentRequests; }
        if (maxIdleTime == null) { maxIdleTime = DEFAULT.maxIdleTime; }
        if (keepAliveTimeout == null) { keepAliveTimeout = DEFAULT.keepAliveTimeout; }
        if (retryTimes == null) { retryTimes = DEFAULT.retryTimes; }
        if (proxy == null) { proxy = DEFAULT.proxy; }
        if ((custom == null) && (DEFAULT.custom != null)) { custom = new HashMap<>(DEFAULT.custom); }
        return this;
    }

    // ##################################################################################
    // #################### stringifier #################################################
    // ##################################################################################

    @Override
    public @NotNull String toString() {
        return OAuth2DataUtils.convertObjectToJSON(this);
    }

    // ##################################################################################
    // #################### getter / setter #############################################
    // ##################################################################################

    public @Nullable Duration getConnectTimeout() {
        return connectTimeout;
    }

    public @NotNull OAuth2RequestExecutorProperties setConnectTimeout(@Nullable Duration connectTimeout) {
        this.connectTimeout = connectTimeout;
        return this;
    }

    public @Nullable Duration getSocketTimeout() {
        return socketTimeout;
    }

    public @NotNull OAuth2RequestExecutorProperties setSocketTimeout(@Nullable Duration socketTimeout) {
        this.socketTimeout = socketTimeout;
        return this;
    }

    public @Nullable Integer getMaxConcurrentRequests() {
        return maxConcurrentRequests;
    }

    public @NotNull OAuth2RequestExecutorProperties setMaxConcurrentRequests(@Nullable Integer maxConcurrentRequests) {
        this.maxConcurrentRequests = maxConcurrentRequests;
        return this;
    }

    public @Nullable Duration getMaxIdleTime() {
        return maxIdleTime;
    }

    public @NotNull OAuth2RequestExecutorProperties setMaxIdleTime(@Nullable Duration maxIdleTime) {
        this.maxIdleTime = maxIdleTime;
        return this;
    }

    public @Nullable Duration getKeepAliveTimeout() {
        return keepAliveTimeout;
    }

    public @NotNull OAuth2RequestExecutorProperties setKeepAliveTimeout(@Nullable Duration keepAliveTimeout) {
        this.keepAliveTimeout = keepAliveTimeout;
        return this;
    }

    public @Nullable Integer getRetryTimes() {
        return retryTimes;
    }

    public @NotNull OAuth2RequestExecutorProperties setRetryTimes(@Nullable Integer retryTimes) {
        this.retryTimes = retryTimes;
        return this;
    }

    public @Nullable String getProxy() {
        return proxy;
    }

    public @NotNull OAuth2RequestExecutorProperties setProxy(@Nullable String proxy) {
        this.proxy = proxy;
        return this;
    }

    public @Nullable Map<@NotNull String, @Nullable String> getCustom() {
        return custom;
    }

    public @NotNull OAuth2RequestExecutorProperties setCustom(@Nullable Map<@NotNull String, @Nullable String> custom) {
        this.custom = custom;
        return this;
    }

}
