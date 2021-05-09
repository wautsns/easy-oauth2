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
     * Default oauth2 request executor properties.
     *
     * <ul>
     * <li style="list-style-type:none">########## Notes ###############</li>
     * <li>connectTimeout: {@code Duration.parse("PT2S")}</li>
     * <li>readTimeout: {@code Duration.parse("PT5S")}</li>
     * <li>maxConcurrentRequests: {@code 64}</li>
     * <li>maxIdleTime: {@code Duration.parse("PT5M")}</li>
     * <li>keepAliveTimeout: {@code Duration.parse("PT3M")}</li>
     * <li>retryTimes: {@code 1}</li>
     * </ul>
     */
    public static final OAuth2RequestExecutorProperties DEFAULT = new OAuth2RequestExecutorProperties()
            .setConnectTimeout(Duration.parse("PT2S"))
            .setReadTimeout(Duration.parse("PT5S"))
            .setMaxConcurrentRequests(64)
            .setMaxIdleTime(Duration.parse("PT5M"))
            .setKeepAliveTimeout(Duration.parse("PT3M"))
            .setRetryTimes(1);

    // ######################################################################################

    /** Connect timeout. */
    private Duration connectTimeout;
    /** Read timeout. */
    private Duration readTimeout;
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

    // ######################################################################################
    // #################### enhanced setter #################################################
    // ######################################################################################

    /**
     * Rewrite properties which value is {@code null} to {@link #DEFAULT} value.
     *
     * @return self reference
     */
    public OAuth2RequestExecutorProperties rewriteNullToDefaultValue() {
        if (connectTimeout == null) { connectTimeout = DEFAULT.connectTimeout; }
        if (readTimeout == null) { readTimeout = DEFAULT.readTimeout; }
        if (maxConcurrentRequests == null) { maxConcurrentRequests = DEFAULT.maxConcurrentRequests; }
        if (maxIdleTime == null) { maxIdleTime = DEFAULT.maxIdleTime; }
        if (keepAliveTimeout == null) { keepAliveTimeout = DEFAULT.keepAliveTimeout; }
        if (retryTimes == null) { retryTimes = DEFAULT.retryTimes; }
        if (proxy == null) { proxy = DEFAULT.proxy; }
        if ((custom == null) && (DEFAULT.custom != null)) { custom = new HashMap<>(DEFAULT.custom); }
        return this;
    }

    // ######################################################################################
    // #################### stringifier #####################################################
    // ######################################################################################

    @Override
    public String toString() {
        return "OAuth2RequestExecutorProperties{" +
                "connectTimeout=" + connectTimeout +
                ", readTimeout=" + readTimeout +
                ", maxConcurrentRequests=" + maxConcurrentRequests +
                ", maxIdleTime=" + maxIdleTime +
                ", keepAliveTimeout=" + keepAliveTimeout +
                ", retryTimes=" + retryTimes +
                ", proxy=" + proxy +
                ", custom=" + custom +
                '}';
    }

    // ######################################################################################
    // #################### getter / setter #################################################
    // ######################################################################################

    public Duration getConnectTimeout() {
        return connectTimeout;
    }

    public OAuth2RequestExecutorProperties setConnectTimeout(Duration connectTimeout) {
        this.connectTimeout = connectTimeout;
        return this;
    }

    public Duration getReadTimeout() {
        return readTimeout;
    }

    public OAuth2RequestExecutorProperties setReadTimeout(Duration readTimeout) {
        this.readTimeout = readTimeout;
        return this;
    }

    public Integer getMaxConcurrentRequests() {
        return maxConcurrentRequests;
    }

    public OAuth2RequestExecutorProperties setMaxConcurrentRequests(Integer maxConcurrentRequests) {
        this.maxConcurrentRequests = maxConcurrentRequests;
        return this;
    }

    public Duration getMaxIdleTime() {
        return maxIdleTime;
    }

    public OAuth2RequestExecutorProperties setMaxIdleTime(Duration maxIdleTime) {
        this.maxIdleTime = maxIdleTime;
        return this;
    }

    public Duration getKeepAliveTimeout() {
        return keepAliveTimeout;
    }

    public OAuth2RequestExecutorProperties setKeepAliveTimeout(Duration keepAliveTimeout) {
        this.keepAliveTimeout = keepAliveTimeout;
        return this;
    }

    public Integer getRetryTimes() {
        return retryTimes;
    }

    public OAuth2RequestExecutorProperties setRetryTimes(Integer retryTimes) {
        this.retryTimes = retryTimes;
        return this;
    }

    public String getProxy() {
        return proxy;
    }

    public OAuth2RequestExecutorProperties setProxy(String proxy) {
        this.proxy = proxy;
        return this;
    }

    public Map<String, String> getCustom() {
        return custom;
    }

    public void setCustom(Map<String, String> custom) {
        this.custom = custom;
    }

}
