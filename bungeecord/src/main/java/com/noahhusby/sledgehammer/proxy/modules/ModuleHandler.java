/*
 * MIT License
 *
 * Copyright 2020-2022 noahhusby
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy,
 * modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software
 * is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS
 * BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

package com.noahhusby.sledgehammer.proxy.modules;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.noahhusby.sledgehammer.proxy.Sledgehammer;
import lombok.Getter;

import java.util.Map;

/**
 * @author Noah Husby
 */
public class ModuleHandler {
    @Getter
    private static final ModuleHandler instance = new ModuleHandler();

    private ModuleHandler() {
    }

    @Getter
    private final Map<Module, Boolean> modules = Maps.newLinkedHashMap();

    /**
     * Register a new module
     *
     * @param module {@link Module}
     */
    public void registerModule(Module module) {
        modules.put(module, false);
    }

    /**
     * Registers an array of modules
     *
     * @param modules {@link Module}
     */
    public void registerModules(Module... modules) {
        for (Module m : modules) {
            registerModule(m);
        }
    }

    /**
     * Unregisters a specific module
     *
     * @param module
     */
    public void unregisterModule(Module module) {
        modules.remove(module);
    }

    /**
     * Unregisters all modules
     */
    public void unregisterModules() {
        modules.clear();
    }

    /**
     * Enables a specific module
     *
     * @param module {@link Module}
     */
    public void enable(Module module) {
        if (modules.get(module)) {
            return;
        }
        module.onEnable();
        modules.put(module, true);
        Sledgehammer.logger.info(String.format("Enabling Module: %s", module.getModuleName()));
    }

    /**
     * Disables a specific module
     *
     * @param module {@link Module}
     */
    public void disable(Module module) {
        if (!modules.get(module)) {
            return;
        }
        module.onDisable();
        modules.put(module, false);
        Sledgehammer.logger.info(String.format("Disabling Module: %s", module.getModuleName()));
    }

    /**
     * Enables all modules
     */
    public void enableAll() {
        ImmutableMap.copyOf(modules).entrySet()
                .stream()
                .filter(e -> !e.getValue())
                .forEach(e -> enable(e.getKey()));
    }

    /**
     * Disables all modules
     */
    public void disableAll() {
        ImmutableMap.copyOf(modules).entrySet()
                .stream()
                .filter(Map.Entry::getValue)
                .forEach(e -> disable(e.getKey()));
    }
}
