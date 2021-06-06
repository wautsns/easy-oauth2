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

import com.github.wautsns.easy.oauth2.core.request.util.OAuth2DataUtils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * OAuth2 url query.
 *
 * @author wautsns
 * @since May 04, 2021
 */
public final class OAuth2URLQuery {

    /** Estimated number of names. */
    private final int estimatedNumberOfNames;
    /** Delegate. */
    private final @NotNull LinkedHashMap<@NotNull String, @Nullable Object> delegate;

    // ##################################################################################
    // #################### enhanced getter #############################################
    // ##################################################################################

    /**
     * Write {@code this} object as text.
     *
     * @return text
     */
    @SuppressWarnings("unchecked")
    public @NotNull String writeAsText() {
        if (delegate.isEmpty()) { return ""; }
        StringBuilder query = new StringBuilder();
        query.append('?');
        for (Map.Entry<String, Object> entry : delegate.entrySet()) {
            String name = entry.getKey();
            Object temporary = entry.getValue();
            if (temporary instanceof String) {
                query.append(name).append('=').append(temporary).append('&');
            } else if (temporary == null) {
                query.append(name).append('&');
            } else {
                List<String> values = (List<String>) temporary;
                if (values.isEmpty()) { continue; }
                for (String value : values) {
                    query.append(name);
                    if (value != null) {
                        query.append('=').append(value).append('&');
                    }
                }
            }
        }
        query.deleteCharAt(query.length() - 1);
        return query.toString();
    }

    /**
     * Return new instance by deep copying {@code this} object.
     *
     * @return copy of {@code this} object
     */
    public @NotNull OAuth2URLQuery copy() {
        return new OAuth2URLQuery(this);
    }

    // ##################################################################################
    // #################### enhanced setter #############################################
    // ##################################################################################

    /**
     * Add unique query item.
     *
     * <ul>
     * <li style="list-style-type:none">########## Notes ###############</li>
     * <li>If the {@code value} is {@code null}, query text will be {@code "name&name2=123"}.</li>
     * <li>The {@code value} will be automatically url encoded.</li>
     * </ul>
     *
     * @param name query item name
     * @param value query item value
     * @return self reference
     */
    public @NotNull OAuth2URLQuery unique(@NotNull String name, @Nullable String value) {
        delegate.put(name, OAuth2DataUtils.encodeWithURLEncoder(value));
        return this;
    }

    /**
     * Add repeatable query item.
     *
     * <ul>
     * <li style="list-style-type:none">########## Notes ###############</li>
     * <li>If the {@code value} is {@code null}, query text will be {@code "name&name2=123"}.</li>
     * <li>The {@code value} will be automatically url encoded.</li>
     * </ul>
     *
     * @param name query item name
     * @param value query item value
     * @return self reference
     */
    @SuppressWarnings("unchecked")
    public @NotNull OAuth2URLQuery repeatable(@NotNull String name, @Nullable String value) {
        value = OAuth2DataUtils.encodeWithURLEncoder(value);
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
     * Add repeatable query items.
     *
     * <ul>
     * <li style="list-style-type:none">########## Notes ###############</li>
     * <li>If the {@code values} is {@code null}, the operation will be ignored.</li>
     * <li>If value is {@code null}, query text will be {@code "name&name2=123"}.</li>
     * <li>Value will be automatically url encoded.</li>
     * </ul>
     *
     * @param name query item name
     * @param values query item values
     * @return self reference
     * @see #repeatable(String, String)
     */
    @SuppressWarnings("unchecked")
    public @NotNull OAuth2URLQuery repeatable(
            @NotNull String name, @Nullable Iterable<String> values) {
        if (values == null) { return this; }
        LinkedList<String> urlEncodedValues = StreamSupport.stream(values.spliterator(), false)
                .map(OAuth2DataUtils::encodeWithURLEncoder)
                .collect(Collectors.toCollection(LinkedList::new));
        Object temporary = delegate.get(name);
        if (temporary == null) {
            delegate.put(name, urlEncodedValues);
        } else if (temporary instanceof List) {
            ((List<String>) temporary).addAll(urlEncodedValues);
        } else {
            urlEncodedValues.addFirst((String) temporary);
            delegate.put(name, urlEncodedValues);
        }
        return this;
    }

    // ##################################################################################
    // #################### constructor #################################################
    // ##################################################################################

    /**
     * Construct an instance.
     *
     * @param estimatedNumberOfNames {@link #estimatedNumberOfNames}
     */
    public OAuth2URLQuery(int estimatedNumberOfNames) {
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
    protected OAuth2URLQuery(@NotNull OAuth2URLQuery template) {
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
        return writeAsText();
    }

}
