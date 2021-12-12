/*
 * MIT License
 *
 * Copyright 2020-2021 noahhusby
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
     * @return True if successfully enabled, false if not
     */
    public boolean enable(Module module) {
        if (modules.get(module)) {
            return false;
        }
        module.onEnable();
        modules.put(module, true);
        Sledgehammer.logger.info(String.format("Enabling Module: %s", module.getModuleName()));
        return true;
    }

    /**
     * Disables a specific module
     *
     * @param module {@link Module}
     * @return True if successfully disabled, false if not
     */
    public boolean disable(Module module) {
        if (!modules.get(module)) {
            return false;
        }
        module.onDisable();
        modules.put(module, false);
        Sledgehammer.logger.info(String.format("Disabling Module: %s", module.getModuleName()));
        return true;
    }

    /**
     * Enables all modules
     */
    public void enableAll() {
        for (Map.Entry<Module, Boolean> e : ImmutableMap.copyOf(modules).entrySet()) {
            if (!e.getValue()) {
                enable(e.getKey());
            }
        }
    }

    /**
     * Disables all modules
     */
    public void disableAll() {
        for (Map.Entry<Module, Boolean> e : ImmutableMap.copyOf(modules).entrySet()) {
            if (e.getValue()) {
                disable(e.getKey());
            }
        }
    }

    /**
     * Reloads all modules
     */
    public void reloadAll() {
        disableAll();
        enableAll();
    }
}
