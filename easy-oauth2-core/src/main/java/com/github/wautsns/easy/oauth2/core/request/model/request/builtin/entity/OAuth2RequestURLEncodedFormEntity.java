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

/**
 * OAuth2 request url encoded form entity.
 *
 * @author wautsns
 * @since Mar 30, 2021
 */
public final class OAuth2RequestURLEncodedFormEntity extends AbstractOAuth2RequestEntity {

    /** Estimated number of unit names. */
    private final int estimatedNumberOfUnitNames;
    /** Raw oauth2 request url encoded form entity. */
    private final @NotNull LinkedHashMap<@NotNull String, @Nullable Object> raw;

    // ######################################################################################
    // #################### enhanced getter #################################################
    // ######################################################################################

    @Override
    public @NotNull String contentType() {
        return "application/x-www-form-urlencoded";
    }

    @Override
    @SuppressWarnings("unchecked")
    public byte @NotNull [] bytes() {
        if (raw.isEmpty()) { return new byte[0]; }
        StringBuilder content = new StringBuilder();
        for (Map.Entry<String, Object> entry : raw.entrySet()) {
            String name = entry.getKey();
            Object temporary = entry.getValue();
            if (temporary instanceof String) {
                content.append(name).append('=').append(temporary).append('&');
            } else if (temporary == null) {
                content.append(name).append('&');
            } else {
                List<String> values = (List<String>) temporary;
                if (values.isEmpty()) { continue; }
                for (String value : values) {
                    content.append(name);
                    if (value != null) {
                        content.append('=').append(value).append('&');
                    }
                }
            }
        }
        content.deleteCharAt(content.length() - 1);
        return content.toString().getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public @NotNull OAuth2RequestURLEncodedFormEntity copy() {
        return new OAuth2RequestURLEncodedFormEntity(this);
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
     * <li>If the {@code value} is {@code null}, the unit in form text is like {@code "key1&key2"}.</li>
     * <li>The {@code value} will be automatically url encoded.</li>
     * </ul>
     *
     * @param name unit name
     * @param value unit value
     * @return self reference
     */
    public @NotNull OAuth2RequestURLEncodedFormEntity unique(@NotNull String name, @Nullable String value) {
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
     * <li>If the {@code value} is {@code null}, the unit in form text is like {@code "key1&key2"}.</li>
     * <li>The {@code value} will be automatically url encoded.</li>
     * </ul>
     *
     * @param name unit name
     * @param value unit value
     * @return self reference
     */
    @SuppressWarnings("unchecked")
    public @NotNull OAuth2RequestURLEncodedFormEntity repeatable(@NotNull String name, @Nullable String value) {
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

    // ######################################################################################
    // #################### constructor #####################################################
    // ######################################################################################

    /**
     * Construct an instance.
     *
     * @param estimatedNumberOfUnitNames estimated number of unit names
     */
    public OAuth2RequestURLEncodedFormEntity(int estimatedNumberOfUnitNames) {
        this.estimatedNumberOfUnitNames = estimatedNumberOfUnitNames;
        this.raw = new LinkedHashMap<>(estimatedNumberOfUnitNames, 1F);
    }

    /**
     * Construct an instance.
     *
     * @param template template
     */
    @SuppressWarnings("unchecked")
    protected OAuth2RequestURLEncodedFormEntity(@NotNull OAuth2RequestURLEncodedFormEntity template) {
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

}
