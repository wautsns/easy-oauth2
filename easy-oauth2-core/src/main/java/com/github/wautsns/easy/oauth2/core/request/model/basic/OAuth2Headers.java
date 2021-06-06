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
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * OAuth2 headers.
 *
 * @author wautsns
 * @since Mar 29, 2021
 */
public final class OAuth2Headers {

    /** Estimated number of names. */
    private final int estimatedNumberOfNames;
    /** Delegate. */
    private final @NotNull LinkedHashMap<@NotNull String, @NotNull Object> delegate;

    // ##################################################################################
    // #################### enhanced getter #############################################
    // ##################################################################################

    /**
     * For each header name and value.
     *
     * <ul>
     * <li style="list-style-type:none">########## Notes ###############</li>
     * <li>If a header is repeatable, all values will be processed separately.</li>
     * <li>The order of traversal is the same as the order in which the headers are added.</li>
     * </ul>
     *
     * @param action action for header name and value
     */
    @SuppressWarnings("unchecked")
    public void forEach(@NotNull BiConsumer<@NotNull String, @NotNull String> action) {
        for (Map.Entry<String, Object> entry : delegate.entrySet()) {
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
     * Return new instance by deep copying {@code this} object.
     *
     * @return copy of {@code this} object
     */
    public @NotNull OAuth2Headers copy() {
        return new OAuth2Headers(this);
    }

    // ##################################################################################
    // #################### enhanced setter #############################################
    // ##################################################################################

    /**
     * Add unique header.
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
            delegate.put(name, value);
        } else {
            delegate.remove(name);
        }
        return this;
    }

    /**
     * Add repeatable header.
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
        Object temporary = delegate.get(name);
        if (temporary == null) {
            delegate.put(name, value);
        } else if (temporary instanceof List) {
            ((List<String>) temporary).add(value);
        } else {
            List<String> values = new LinkedList<>();
            values.add((String) temporary);
            values.add(value);
            delegate.put(name, values);
        }
        return this;
    }

    /**
     * Add repeatable headers.
     *
     * <ul>
     * <li style="list-style-type:none">########## Notes ###############</li>
     * <li>If the {@code values} is {@code null}, the operation will be ignored.</li>
     * <li>If value is {@code null}, the value will be ignored.</li>
     * <li>Value will be automatically url encoded.</li>
     * </ul>
     *
     * @param name header name
     * @param values header values
     * @return self reference
     * @see #repeatable(String, String)
     */
    @SuppressWarnings("unchecked")
    public @NotNull OAuth2Headers repeatable(
            @NotNull String name, @Nullable Iterable<@Nullable String> values) {
        if (values == null) { return this; }
        values = StreamSupport.stream(values.spliterator(), false)
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(LinkedList::new));
        Object temporary = delegate.get(name);
        if (temporary == null) {
            delegate.put(name, values);
        } else if (temporary instanceof List) {
            ((List<String>) temporary).addAll((List<String>) values);
        } else {
            ((LinkedList<String>) values).addFirst((String) temporary);
            delegate.put(name, values);
        }
        return this;
    }

    // ##################################################################################

    /**
     * Add unique header `Accept`.
     *
     * <ul>
     * <li style="list-style-type:none">########## Notes ###############</li>
     * <li>The method equals to {@link #unique(String, String) unique}({@code "Accept"}, {@code
     * value}).</li>
     * </ul>
     *
     * @param value header value
     * @return self reference
     */
    public @NotNull OAuth2Headers accept(@NotNull String value) {
        return unique("Accept", value);
    }

    /**
     * Add unique header `Accept` with value {@code "application/json"}.
     *
     * <ul>
     * <li style="list-style-type:none">########## Notes ###############</li>
     * <li>The method equals to {@link #accept(String) accept}({@code "application/json"}).</li>
     * </ul>
     *
     * @return self reference
     */
    public @NotNull OAuth2Headers acceptJSON() {
        return accept("application/json");
    }

    // ##################################################################################

    /**
     * Add unique header `Authorization`.
     *
     * <ul>
     * <li style="list-style-type:none">########## Notes ###############</li>
     * <li>If the {@code value} is {@code null}, the operation will be ignored.</li>
     * <li>The method equals to {@link #unique(String, String) unique}({@code "Authorization"},
     * {@code type + ' ' + value}).</li>
     * </ul>
     *
     * @param type authorization type
     * @param value authorization value
     * @return self reference
     */
    public @NotNull OAuth2Headers authorization(@NotNull String type, @Nullable String value) {
        if (value == null) { return this; }
        unique("Authorization", type + ' ' + value);
        return this;
    }

    // ##################################################################################

    /**
     * Add unique header `Content-Type`.
     *
     * <ul>
     * <li style="list-style-type:none">########## Notes ###############</li>
     * <li>The method equals to {@link #unique(String, String) unique}({@code "Content-Type"},
     * {@code value}).</li>
     * </ul>
     *
     * @param value header value
     * @return self reference
     */
    public @NotNull OAuth2Headers contentType(@NotNull String value) {
        return unique("Content-Type", value);
    }

    // ##################################################################################

    /**
     * Add unique header `User-Agent` with value {@code "Mozilla/5.0 (X11; Linux x86_64)
     * JavaBasedWebKit/98.6.27 EasyOAuth2/98.2.5"}.
     *
     * <ul>
     * <li style="list-style-type:none">########## Notes ###############</li>
     * <li>The method equals to {@link #unique(String, String) unique}({@code "User-Agent"},
     * {@code "Mozilla/5.0 (X11; Linux x86_64) JavaBasedWebKit/98.6.27 EasyOAuth2/98.2.5"}).</li>
     * </ul>
     *
     * @return self reference
     */
    public @NotNull OAuth2Headers userAgentEasyOAuth2() {
        return unique(
                "User-Agent",
                "Mozilla/5.0 (X11; Linux x86_64) JavaBasedWebKit/98.6.27 EasyOAuth2/98.2.5"
        );
    }

    // ##################################################################################
    // #################### constructor #################################################
    // ##################################################################################

    /**
     * Construct an instance.
     *
     * @param estimatedNumberOfNames {@link #estimatedNumberOfNames}
     */
    public OAuth2Headers(int estimatedNumberOfNames) {
        this.estimatedNumberOfNames = estimatedNumberOfNames;
        this.delegate = new LinkedHashMap<>(this.estimatedNumberOfNames, 1F);
    }

    /**
     * Construct an instance by deep copying {@code template}.
     *
     * @param template template
     * @see #copy()
     */
    @SuppressWarnings("unchecked")
    protected OAuth2Headers(@NotNull OAuth2Headers template) {
        this.estimatedNumberOfNames = template.estimatedNumberOfNames;
        this.delegate = new LinkedHashMap<>(this.estimatedNumberOfNames, 1F);
        for (Map.Entry<String, Object> entry : template.delegate.entrySet()) {
            Object value = entry.getValue();
            if (value instanceof List) {
                value = new LinkedList<>((List<String>) value);
            }
            this.delegate.put(entry.getKey(), value);
        }
    }

    // ##################################################################################
    // #################### stringifier #################################################
    // ##################################################################################

    @Override
    public @NotNull String toString() {
        return delegate.toString();
    }

}
