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
package com.github.wautsns.easy.oauth2.core.request.model.request.builtin.entity;

import com.github.wautsns.easy.oauth2.core.request.model.request.AbstractOAuth2RequestEntity;
import com.github.wautsns.easy.oauth2.core.request.util.OAuth2DataUtils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * OAuth2 request url encoded form entity.
 *
 * @author wautsns
 * @since Mar 30, 2021
 */
public final class OAuth2RequestURLEncodedFormEntity extends AbstractOAuth2RequestEntity {

    /** Estimated number of names. */
    private final int estimatedNumberOfNames;
    /** Delegate. */
    private final @NotNull LinkedHashMap<@NotNull String, @Nullable Object> delegate;

    // ##################################################################################
    // #################### enhanced getter #############################################
    // ##################################################################################

    @Override
    public @NotNull String contentType() {
        return "application/x-www-form-urlencoded";
    }

    @SuppressWarnings("unchecked")
    public @NotNull String writeAsText() {
        if (delegate.isEmpty()) { return ""; }
        StringBuilder text = new StringBuilder();
        for (Map.Entry<String, Object> entry : delegate.entrySet()) {
            String name = entry.getKey();
            Object temporary = entry.getValue();
            if (temporary instanceof String) {
                text.append(name).append('=').append(temporary).append('&');
            } else if (temporary == null) {
                text.append(name).append('&');
            } else {
                List<String> values = (List<String>) temporary;
                if (values.isEmpty()) { continue; }
                for (String value : values) {
                    text.append(name);
                    if (value != null) {
                        text.append('=').append(value).append('&');
                    }
                }
            }
        }
        text.deleteCharAt(text.length() - 1);
        return text.toString();
    }

    @Override
    public byte @NotNull [] writeAsBytes() {
        return writeAsText().getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public @NotNull OAuth2RequestURLEncodedFormEntity copy() {
        return new OAuth2RequestURLEncodedFormEntity(this);
    }

    // ##################################################################################
    // #################### enhanced setter #############################################
    // ##################################################################################

    /**
     * Add unique form item.
     *
     * <ul>
     * <li style="list-style-type:none">########## Notes ###############</li>
     * <li>If the {@code value} is {@code null}, form text will be {@code "name&name2=123"}.</li>
     * <li>The {@code value} will be automatically url encoded.</li>
     * </ul>
     *
     * @param name form item name
     * @param value form item value
     * @return self reference
     */
    public @NotNull OAuth2RequestURLEncodedFormEntity unique(
            @NotNull String name, @Nullable String value) {
        delegate.put(name, OAuth2DataUtils.encodeWithURLEncoder(value));
        return this;
    }

    /**
     * Add repeatable form item.
     *
     * <ul>
     * <li style="list-style-type:none">########## Notes ###############</li>
     * <li>If the {@code value} is {@code null}, form text will be {@code "name&name2=123"}.</li>
     * <li>The {@code value} will be automatically url encoded.</li>
     * </ul>
     *
     * @param name form item name
     * @param value form item value
     * @return self reference
     */
    @SuppressWarnings("unchecked")
    public @NotNull OAuth2RequestURLEncodedFormEntity repeatable(
            @NotNull String name, @Nullable String value) {
        String urlEncodedValue = OAuth2DataUtils.encodeWithURLEncoder(value);
        Object temporary = delegate.get(name);
        if (temporary == null) {
            delegate.put(name, urlEncodedValue);
        } else if (temporary instanceof List) {
            ((List<String>) temporary).add(urlEncodedValue);
        } else {
            List<String> values = new LinkedList<>();
            values.add((String) temporary);
            values.add(urlEncodedValue);
            delegate.put(name, values);
        }
        return this;
    }

    /**
     * Add repeatable form items.
     *
     * <ul>
     * <li style="list-style-type:none">########## Notes ###############</li>
     * <li>If the {@code values} is {@code null}, the operation will be ignored.</li>
     * <li>If value is {@code null}, form text will be {@code "name&name2=123"}.</li>
     * <li>Value will be automatically url encoded.</li>
     * </ul>
     *
     * @param name form item name
     * @param values form item values
     * @return self reference
     * @see #repeatable(String, String)
     */
    @SuppressWarnings("unchecked")
    public @NotNull OAuth2RequestURLEncodedFormEntity repeatable(
            @NotNull String name, @Nullable Iterable<@Nullable String> values) {
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
    public OAuth2RequestURLEncodedFormEntity(int estimatedNumberOfNames) {
        this.estimatedNumberOfNames = estimatedNumberOfNames;
        this.delegate = new LinkedHashMap<>(estimatedNumberOfNames, 1F);
    }

    /**
     * Construct an instance by deep copying {@code template}.
     *
     * @param template template
     * @see #copy()
     */
    @SuppressWarnings("unchecked")
    protected OAuth2RequestURLEncodedFormEntity(
            @NotNull OAuth2RequestURLEncodedFormEntity template) {
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
        return "{contentType=" + contentType() +
                ", delegate=" + delegate +
                '}';
    }

}
