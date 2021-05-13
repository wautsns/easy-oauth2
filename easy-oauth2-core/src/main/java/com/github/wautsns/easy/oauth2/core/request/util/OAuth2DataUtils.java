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
package com.github.wautsns.easy.oauth2.core.request.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.MissingNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.wautsns.easy.oauth2.core.exception.OAuth2IOException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * OAuth2 data utils.
 *
 * @author wautsns
 * @since Apr 19, 2021
 */
public final class OAuth2DataUtils {

    /** Object mapper. */
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    // ##################################################################################
    // #################### read ########################################################
    // ##################################################################################

    /**
     * Read the given {@code inputStream} as text.
     *
     * <ul>
     * <li style="list-style-type:none">########## Notes ###############</li>
     * <li>The content of the {@code inputStream} must be readable text.</li>
     * <li>If the {@code inputStream} is {@code null}, {@code null} will be returned.</li>
     * <li>The {@code inputStream} will be automatically closed through try-with-resource.</li>
     * </ul>
     *
     * @param inputStream input stream
     * @return text, or {@code null} if the {@code inputStream} is {@code null}
     * @throws OAuth2IOException if an I/O error occurs
     */
    public static @Nullable String readInputStreamAsText(@Nullable InputStream inputStream) throws OAuth2IOException {
        if (inputStream == null) { return null; }
        try (ByteArrayOutputStream result = new ByteArrayOutputStream();
             InputStream inputStreamForClosing = inputStream) {
            byte[] buffer = new byte[256];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                result.write(buffer, 0, length);
            }
            return result.toString(StandardCharsets.UTF_8.name());
        } catch (IOException e) {
            throw new OAuth2IOException(e);
        }
    }

    /**
     * Read the given {@code jsonInputStream} as a tree.
     *
     * <ul>
     * <li style="list-style-type:none">########## Notes ###############</li>
     * <li>The content of the {@code jsonInputStream} must be json.</li>
     * <li>If the {@code jsonInputStream} is {@code null} or does not contain any content, {@link
     * MissingNode#getInstance() instance} of {@link MissingNode} will be returned.</li>
     * <li>The {@code jsonInputStream} will be automatically closed through try-with-resource.</li>
     * </ul>
     *
     * @param jsonInputStream json input stream
     * @return root node of the tree
     * @throws OAuth2IOException if an I/O error occurs
     */
    public static @NotNull JsonNode readJSONInputStreamAsTree(@Nullable InputStream jsonInputStream) throws OAuth2IOException {
        if (jsonInputStream == null) { return MissingNode.getInstance(); }
        try (InputStream jsonInputStreamForClosing = jsonInputStream) {
            JsonNode root = OBJECT_MAPPER.readTree(jsonInputStream);
            return (root != null) ? root : MissingNode.getInstance();
        } catch (IOException e) {
            throw new OAuth2IOException(e);
        }
    }

    // ##################################################################################
    // #################### convert #####################################################
    // ##################################################################################

    /**
     * Convert the given {@code object} to json.
     *
     * <ul>
     * <li style="list-style-type:none">########## Notes ###############</li>
     * <li>If the {@code object} is {@code null}, an empty text {@code ""} will be returned.</li>
     * </ul>
     *
     * @param object object
     * @return json
     */
    public static @NotNull String convertObjectToJSON(@Nullable Object object) {
        try {
            return OBJECT_MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Convert the given {@code object} to json bytes.
     *
     * <ul>
     * <li style="list-style-type:none">########## Notes ###############</li>
     * <li>If the {@code object} is {@code null}, bytes of the text {@code "null"} will be returned.</li>
     * </ul>
     *
     * @param object object
     * @return json bytes
     */
    public static byte @NotNull [] convertObjectToJSONBytes(@Nullable Object object) {
        try {
            return OBJECT_MAPPER.writeValueAsBytes(object);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Convert the given {@code object} to a tree.
     *
     * <ul>
     * <li style="list-style-type:none">########## Notes ###############</li>
     * <li>If the {@code object} is {@code null}, an {@link NullNode#getInstance() instance} of {@link NullNode} will
     * be returned.</li>
     * </ul>
     *
     * @param object object
     * @return root node of the tree
     */
    public static @NotNull JsonNode convertObjectToTree(@Nullable Object object) {
        return OBJECT_MAPPER.valueToTree(object);
    }

    // ##################################################################################
    // #################### encode ######################################################
    // ##################################################################################

    /**
     * Encode the given {@code text} with {@link URLEncoder}.
     *
     * <ul>
     * <li style="list-style-type:none">########## Notes ###############</li>
     * <li>If the {@code text} is {@code null}, {@code null} will be returned.</li>
     * </ul>
     *
     * @param text text
     * @return url encoded text, or {@code null} if the {@code value} is {@code null}
     */
    public static @Nullable String encodeWithURLEncoder(@Nullable String text) {
        if (text == null) { return null; }
        try {
            return URLEncoder.encode(text, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException(e);
        }
    }

    // ##################################################################################
    // #################### create ######################################################
    // ##################################################################################

    /**
     * Create a new object node.
     *
     * @return object node
     */
    public static @NotNull ObjectNode createObjectNode() {
        return OBJECT_MAPPER.createObjectNode();
    }

    // ##################################################################################

    /** Utility. */
    private OAuth2DataUtils() {}

}
