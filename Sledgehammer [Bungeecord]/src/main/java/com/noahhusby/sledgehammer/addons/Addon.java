package com.noahhusby.sledgehammer.addons;

import net.md_5.bungee.api.event.PluginMessageEvent;

public abstract class Addon implements IAddon {

    @Override
    public void onDisable() {

    }

    @Override
    public void onLoad() {

    }

    @Override
    public void onPluginMessage(PluginMessageEvent e) {

    }

    @Override
    public String[] getMessageChannels() {
        return null;
    }
}


