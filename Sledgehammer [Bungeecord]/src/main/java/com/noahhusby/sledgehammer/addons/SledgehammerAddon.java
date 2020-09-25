package com.noahhusby.sledgehammer.addons;

import net.md_5.bungee.api.event.PluginMessageEvent;

public abstract class SledgehammerAddon implements ISledgehammerAddon {

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


