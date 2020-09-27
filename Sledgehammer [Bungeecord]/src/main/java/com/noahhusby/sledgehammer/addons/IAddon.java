/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - IAddon.java
 * All rights reserved.
 */

package com.noahhusby.sledgehammer.addons;

import net.md_5.bungee.api.event.PluginMessageEvent;

public interface IAddon {
    void onEnable();

    void onDisable();

    void onLoad();

    void onPluginMessage(PluginMessageEvent e);

    String[] getMessageChannels();
}
