package com.noahhusby.sledgehammer.addons;

import net.md_5.bungee.api.event.PluginMessageEvent;

public class TerramapAddon extends Addon {

    @Override
    public void onEnable() {}

    @Override
    public void onPluginMessage(PluginMessageEvent e) {
        super.onPluginMessage(e);
    }

    @Override
    public String[] getMessageChannels() {
        return new String[]{"terramap:sledgehammer", "terramap:mapsync"};
    }
}
