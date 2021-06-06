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
package com.github.wautsns.easy.oauth2.core.request.executor;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;

/**
 * OAuth2 request executor factory manager.
 *
 * <ul>
 * <li style="list-style-type:none">########## Notes ###############</li>
 * <li>{@link OAuth2RequestExecutorFactory} that has implemented java spi will be automatically
 * registered.</li>
 * </ul>
 *
 * @author wautsns
 * @since Mar 31, 2021
 */
public final class OAuth2RequestExecutorFactoryManager {

    /** Logger. */
    private static final @NotNull Logger log =
            LoggerFactory.getLogger(OAuth2RequestExecutorFactoryManager.class);

    // ##################################################################################

    /** Factory group by its type. */
    private static final @NotNull Map<@NotNull Class<?>, @NotNull OAuth2RequestExecutorFactory<?>> FACTORIES =
            new ConcurrentHashMap<>();

    // Register factories automatically through java spi.
    static {
        ServiceLoader.load(OAuth2RequestExecutorFactory.class)
                .forEach(OAuth2RequestExecutorFactoryManager::register);
        log.info(
                "All oauth2 request executor factories that have implemented java spi have been" +
                        " automatically registered."
        );
    }

    // ##################################################################################
    // #################### enhanced getter #############################################
    // ##################################################################################

    /**
     * Return any of the enabled factory.
     *
     * @return instance
     * @throws IllegalStateException if there is no enabled factory
     */
    public static @NotNull OAuth2RequestExecutorFactory<?> factory() {
        return FACTORIES.values().stream()
                .filter(OAuth2RequestExecutorFactory::enabled)
                .findAny()
                .orElseThrow(() -> new IllegalStateException("There is no enabled factory."));
    }

    /**
     * Return enabled factory of the given {@code type}.
     *
     * <ul>
     * <li style="list-style-type:none">########## Notes ###############</li>
     * <li>If the {@code type} is {@link OAuth2RequestExecutorFactory}, the method equals to {@link
     * #factory()}.</li>
     * </ul>
     *
     * @param type type
     * @param <F> the type of factory
     * @return factory
     * @throws IllegalStateException if there is no enabled factory of the {@code type}
     */
    @SuppressWarnings("unchecked")
    public static <F extends OAuth2RequestExecutorFactory<?>> @NotNull F factory(
            @NotNull Class<F> type) {
        F factory = (F) FACTORIES.get(type);
        if (factory == null) {
            if (OAuth2RequestExecutorFactory.class.equals(type)) {
                return (F) factory();
            } else {
                throw new IllegalStateException(String.format(
                        "There is no factory of the given type. type: %s", type
                ));
            }
        } else if (factory.enabled()) {
            return factory;
        } else {
            throw new IllegalStateException(String.format(
                    "The factory of the given type is not enabled. type: %s", type
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
    public static @Nullable OAuth2RequestExecutorFactory<?> register(
            @NotNull OAuth2RequestExecutorFactory<?> factory) {
        Class<? extends OAuth2RequestExecutorFactory> type = factory.getClass();
        OAuth2RequestExecutorFactory previous = FACTORIES.put(type, factory);
        if (previous == null) {
            log.info("A factory has been registered. type: {}", type);
        } else {
            log.warn(
                    "The previous factory has been replaced. type: {}, current.hash: {}," +
                            " previous.hash: {}",
                    type, factory.hashCode(), previous.hashCode()
            );
        }
        return previous;
    }

    // ##################################################################################

    /** Static Manager. */
    private OAuth2RequestExecutorFactoryManager() {}

}
