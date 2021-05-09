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

/**
 * OAuth2 url query.
 *
 * @author wautsns
 * @since May 04, 2021
 */
public final class OAuth2URLQuery {

    /** Estimated number of unit names. */
    private final int estimatedNumberOfUnitNames;
    /** Raw oauth2 url query. */
    private final @NotNull LinkedHashMap<@NotNull String, @Nullable Object> raw;

    // ######################################################################################
    // #################### enhanced getter #################################################
    // ######################################################################################

    /**
     * Return query in text format.
     *
     * <ul>
     * <li style="list-style-type:none">########## Notes ###############</li>
     * <li>If {@code this} query has no unit, an empty string will be returned, otherwise the text like {@code
     * "?key1=urlEncodedValue1&key2=urlEncodedValue2"} will be returned.</li>
     * <li>If a unit has more than one values, the text like {@code "?key=urlEncodedValue1&key=urlEncodedValue2"} will
     * be returned.</li>
     * </ul>
     *
     * @return query in text format
     */
    @SuppressWarnings("unchecked")
    public @NotNull String asText() {
        if (raw.isEmpty()) { return ""; }
        StringBuilder query = new StringBuilder();
        query.append('?');
        for (Map.Entry<String, Object> entry : raw.entrySet()) {
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
     * Return a new instance by deep copying {@code this} object.
     *
     * @return a copy of {@code this} object
     */
    public @NotNull OAuth2URLQuery copy() {
        return new OAuth2URLQuery(this);
    }

    // ######################################################################################
    // #################### enhanced setter #################################################
    // ######################################################################################

    /**
     * Add a unit which the name is unique.
     *
     * <ul>
     * <li style="list-style-type:none">########## Notes ###############</li>
     * <li>The unit will appear only once in {@link #toString()}.</li>
     * <li>If the {@code value} is {@code null}, the unit in {@link #toString()} is like {@code "key1&key2"}.</li>
     * <li>The {@code value} will be automatically url encoded.</li>
     * </ul>
     *
     * @param name unit name
     * @param value unit value
     * @return self reference
     */
    public @NotNull OAuth2URLQuery unique(@NotNull String name, @Nullable String value) {
        raw.put(name, OAuth2DataUtils.encodeWithURLEncoder(value));
        return this;
    }

    /**
     * Add a unit which the name is repeatable.
     *
     * <ul>
     * <li style="list-style-type:none">########## Notes ###############</li>
     * <li>If the unit is added more than once, the unit will appear several times in {@link #toString()}. And it is
     * like {@code "key=value1&key=value2"}</li>
     * <li>If the {@code value} is {@code null}, the unit in {@link #toString()} is like {@code "key1&key2"}.</li>
     * <li>The {@code value} will be automatically url encoded.</li>
     * </ul>
     *
     * @param name unit name
     * @param value unit value
     * @return self reference
     */
    @SuppressWarnings("unchecked")
    public @NotNull OAuth2URLQuery repeatable(@NotNull String name, @Nullable String value) {
        value = OAuth2DataUtils.encodeWithURLEncoder(value);
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

    /**
     * Add units which the name is repeatable.
     *
     * <ul>
     * <li style="list-style-type:none">########## Notes ###############</li>
     * <li>If the {@code values} is {@code null}, the operation will be ignored.</li>
     * </ul>
     *
     * @param name unit name
     * @param values unit value
     * @return self reference
     * @see #repeatable(String, String)
     */
    public @NotNull OAuth2URLQuery repeatable(@NotNull String name, @Nullable Iterable<String> values) {
        if (values == null) { return this; }
        for (String value : values) {
            repeatable(name, value);
        }
        return this;
    }

    // ######################################################################################
    // #################### constructor #####################################################
    // ######################################################################################

    /**
     * Construct an instance.
     *
     * @param estimatedNumberOfUnitNames estimated number of unit names
     */
    public OAuth2URLQuery(int estimatedNumberOfUnitNames) {
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
    protected OAuth2URLQuery(@NotNull OAuth2URLQuery template) {
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
        return asText();
    }

}
