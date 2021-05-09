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
package com.github.wautsns.easy.oauth2.core.request.model.basic;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * OAuth2 request headers.
 *
 * @author wautsns
 * @since Mar 29, 2021
 */
public final class OAuth2Headers {

    /** Estimated number of unit names. */
    private final int estimatedNumberOfUnitNames;
    /** Raw oauth2 headers. */
    private final @NotNull LinkedHashMap<@NotNull String, @NotNull Object> raw;

    // ######################################################################################
    // #################### enhanced getter #################################################
    // ######################################################################################

    /**
     * For each header name and header value.
     *
     * <ul>
     * <li style="list-style-type:none">########## Notes ###############</li>
     * <li>If a header is repeatable, all values will be processed separately.</li>
     * </ul>
     *
     * @param action action for header name and header value
     */
    @SuppressWarnings("unchecked")
    public void forEach(@NotNull BiConsumer<@NotNull String, @NotNull String> action) {
        for (Map.Entry<String, Object> entry : raw.entrySet()) {
            String name = entry.getKey();
            Object temporary = entry.getValue();
            if (temporary instanceof String) {
                action.accept(name, (String) temporary);
            } else if (temporary instanceof List) {
                for (String value : (List<String>) temporary) {
                    action.accept(name, value);
                }
            }
        }
    }

    /**
     * Return a new instance by deep copying {@code this} object.
     *
     * @return a copy of {@code this} object
     */
    public @NotNull OAuth2Headers copy() {
        return new OAuth2Headers(this);
    }

    // ######################################################################################
    // #################### enhanced setter #################################################
    // ######################################################################################

    /**
     * Add a header which the name is unique.
     *
     * <ul>
     * <li style="list-style-type:none">########## Notes ###############</li>
     * <li>If the {@code value} is {@code null}, the header will be removed.</li>
     * </ul>
     *
     * @param name header name
     * @param value header value
     * @return self reference
     */
    public @NotNull OAuth2Headers unique(@NotNull String name, @Nullable String value) {
        if (value != null) {
            raw.put(name, value);
        } else {
            raw.remove(name);
        }
        return this;
    }

    /**
     * Add a header which the name is repeatable.
     *
     * <ul>
     * <li style="list-style-type:none">########## Notes ###############</li>
     * <li>If the {@code value} is {@code null}, the operation will be ignored.</li>
     * </ul>
     *
     * @param name header name
     * @param value header value
     * @return self reference
     */
    @SuppressWarnings("unchecked")
    public @NotNull OAuth2Headers repeatable(@NotNull String name, @Nullable String value) {
        if (value == null) { return this; }
        Object temporary = raw.get(name);
        if (temporary == null) {
            raw.put(name, value);
        } else if (temporary instanceof List) {
            ((List<String>) temporary).add(value);
        } else {
            List<String> values = new LinkedList<>();
            values.add((String) temporary);
            values.add(value);
            raw.put(name, values);
        }
        return this;
    }

    // ######################################################################################

    /**
     * Add header `Accept` through {@link #unique(String, String)}.
     *
     * @param value value
     * @return self reference
     */
    public @NotNull OAuth2Headers accept(@NotNull String value) {
        return unique("Accept", value);
    }

    /**
     * Add header `Accept` with value {@code "application/json"} through {@link #accept(String)}.
     *
     * @return self reference
     */
    public @NotNull OAuth2Headers acceptJSON() {
        return accept("application/json");
    }

    // ######################################################################################

    /**
     * Add header `Authorization` through {@link #unique(String, String)}.
     *
     * @param value value
     * @return self reference
     */
    public @NotNull OAuth2Headers authorization(@NotNull String type, @Nullable String value) {
        if (value != null) { unique("Authorization", type + ' ' + value); }
        return this;
    }

    // ######################################################################################

    /**
     * Add header `Content-Type` through {@link #unique(String, String)}.
     *
     * @param value value
     * @return self reference
     */
    public @NotNull OAuth2Headers contentType(@NotNull String value) {
        return unique("Content-Type", value);
    }

    // ######################################################################################

    /**
     * Add header `User-Agent` with value {@code "EasyOAuth2"} through {@link #unique(String, String)}.
     *
     * @return self reference
     */
    public @NotNull OAuth2Headers userAgentEasyOAuth2() {
        return unique("User-Agent", "EasyOAuth2");
    }

    // ######################################################################################
    // #################### constructor #####################################################
    // ######################################################################################

    /**
     * Construct an instance.
     *
     * @param estimatedNumberOfUnitNames estimated number of unit names
     */
    public OAuth2Headers(int estimatedNumberOfUnitNames) {
        this.estimatedNumberOfUnitNames = estimatedNumberOfUnitNames;
        this.raw = new LinkedHashMap<>(this.estimatedNumberOfUnitNames, 1F);
    }

    /**
     * Construct an instance by deep copying {@code template}.
     *
     * @param template template
     * @see #copy()
     */
    @SuppressWarnings("unchecked")
    protected OAuth2Headers(@NotNull OAuth2Headers template) {
        this.estimatedNumberOfUnitNames = template.estimatedNumberOfUnitNames;
        this.raw = new LinkedHashMap<>(this.estimatedNumberOfUnitNames, 1F);
        for (Map.Entry<String, Object> entry : template.raw.entrySet()) {
            if (entry instanceof List) {
                this.raw.put(entry.getKey(), new LinkedList<>((List<String>) entry.getValue()));
            } else {
                this.raw.put(entry.getKey(), entry.getValue());
            }
        }
    }

    // ######################################################################################
    // #################### stringifier #####################################################
    // ######################################################################################

    @Override
    public @NotNull String toString() {
        return raw.toString();
    }

}
