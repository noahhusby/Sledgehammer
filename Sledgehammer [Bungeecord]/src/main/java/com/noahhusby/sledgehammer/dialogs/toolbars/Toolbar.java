/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - Toolbar.java
 * All rights reserved.
 */

package com.noahhusby.sledgehammer.dialogs.toolbars;

import com.google.common.collect.Maps;

import java.util.Map;

public abstract class Toolbar implements IToolbar {
    Map<String, String> tools = Maps.newHashMap();

    public void addTool(String symbol, String action) {
        tools.put(symbol, action);
    }

    @Override
    public Map<String, String> getTools() {
        return tools;
    }
}
