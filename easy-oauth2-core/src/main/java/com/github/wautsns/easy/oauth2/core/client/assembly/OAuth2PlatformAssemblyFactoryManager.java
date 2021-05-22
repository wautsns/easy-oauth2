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
    private static final @NotNull Logger log = LoggerFactory.getLogger(OAuth2PlatformAssemblyFactoryManager.class);

    // ##################################################################################

    /** Factory group by platform. */
    private static final @NotNull Map<@NotNull String, @NotNull OAuth2PlatformAssemblyFactory<?, ?, ?, ?>> INSTANCES =
            new ConcurrentHashMap<>();

    // Register factories automatically through java spi.
    static {
        log.info("Ready to register factories automatically through java spi.");
        ServiceLoader.load(OAuth2PlatformAssemblyFactory.class)
                .forEach(OAuth2PlatformAssemblyFactoryManager::register);
        log.info("All factories that have implemented java spi have been automatically registered.");
    }

    // ##################################################################################
    // #################### enhanced getter #############################################
    // ##################################################################################

    /**
     * Return an instance for the given {@code platform}.
     *
     * <ul>
     * <li style="list-style-type:none">########## Notes ###############</li>
     * <li>If there is no such factory, an {@link IllegalStateException} will be thrown.</li>
     * </ul>
     *
     * @param platform platform
     * @return instance
     */
    public static @NotNull OAuth2PlatformAssemblyFactory<?, ?, ?, ?> instance(@NotNull String platform) {
        OAuth2PlatformAssemblyFactory<?, ?, ?, ?> factory = INSTANCES.get(platform);
        if (factory != null) {
            return factory;
        } else {
            throw new IllegalStateException(String.format("There is no such instance. platform: %s.", platform));
        }
    }

    // ##################################################################################
    // #################### enhanced setter #############################################
    // ##################################################################################

    /**
     * Register the given {@code instance}.
     *
     * @param instance request executor instance
     * @return previous instance, or {@code null} if not exists
     */
    @SuppressWarnings("rawtypes")
    public static @Nullable OAuth2PlatformAssemblyFactory<?, ?, ?, ?> register(
            @NotNull OAuth2PlatformAssemblyFactory<?, ?, ?, ?> instance) {
        String platform = instance.platform();
        OAuth2PlatformAssemblyFactory previous = INSTANCES.put(platform, instance);
        if (previous == null) {
            log.info("An instance has been registered successfully. platform: {}", platform);
        } else {
            log.warn(
                    "The previous instance has been replaced. platform: {}, previous: {}, current: {}",
                    platform, previous, instance
            );
        }
        return previous;
    }

    // ##################################################################################

    /** Static Manager. */
    private OAuth2PlatformAssemblyFactoryManager() {}

}
