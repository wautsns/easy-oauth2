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
package com.github.wautsns.easy.oauth2.core.client.assembly;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;

/**
 * OAuth2 platform assembly factory manager.
 *
 * @author wautsns
 * @since May 14, 2021
 */
public final class OAuth2PlatformAssemblyFactoryManager {

    /** Logger. */
    private static final @NotNull Logger log =
            LoggerFactory.getLogger(OAuth2PlatformAssemblyFactoryManager.class);

    // ##################################################################################

    /** Factory group by platform. */
    private static final @NotNull Map<@NotNull String, @NotNull OAuth2PlatformAssemblyFactory<?, ?, ?, ?>> FACTORIES =
            new ConcurrentHashMap<>();

    // Register factories automatically through java spi.
    static {
        ServiceLoader.load(OAuth2PlatformAssemblyFactory.class)
                .forEach(OAuth2PlatformAssemblyFactoryManager::register);
        log.info(
                "All oauth2 platform assembly factories that have implemented java spi have been" +
                        " automatically registered."
        );
    }

    // ##################################################################################
    // #################### enhanced getter #############################################
    // ##################################################################################

    /**
     * Return factory for the given {@code platform}.
     *
     * @param platform platform
     * @return factory
     * @throws IllegalStateException if there is no factory for the given {@code platform}
     */
    public static @NotNull OAuth2PlatformAssemblyFactory<?, ?, ?, ?> factory(
            @NotNull String platform) {
        OAuth2PlatformAssemblyFactory<?, ?, ?, ?> factory = FACTORIES.get(platform);
        if (factory != null) {
            return factory;
        } else {
            throw new IllegalStateException(String.format(
                    "There is no factory for the given platform. platform: %s.", platform
            ));
        }
    }

    // ##################################################################################
    // #################### enhanced setter #############################################
    // ##################################################################################

    /**
     * Register the given {@code factory}.
     *
     * @param factory factory
     * @return previous factory, or {@code null} if not exists
     */
    @SuppressWarnings("rawtypes")
    public static @Nullable OAuth2PlatformAssemblyFactory<?, ?, ?, ?> register(
            @NotNull OAuth2PlatformAssemblyFactory<?, ?, ?, ?> factory) {
        String platform = factory.platform();
        OAuth2PlatformAssemblyFactory previous = FACTORIES.put(platform, factory);
        if (previous == null) {
            log.info("A factory has been registered. platform: {}", platform);
        } else {
            log.warn(
                    "The previous factory has been replaced. platform: {}, current.hash: {}," +
                            " previous.hash: {}",
                    platform, factory.hashCode(), previous.hashCode()
            );
        }
        return previous;
    }

    // ##################################################################################

    /** Static Manager. */
    private OAuth2PlatformAssemblyFactoryManager() {}

}
