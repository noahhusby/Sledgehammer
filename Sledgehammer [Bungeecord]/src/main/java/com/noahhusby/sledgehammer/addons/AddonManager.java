package com.noahhusby.sledgehammer.addons;

import com.noahhusby.sledgehammer.Sledgehammer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddonManager implements Listener {
    private static AddonManager mInstance = null;

    public static AddonManager getInstance() {
        if(mInstance == null) mInstance = new AddonManager();
        return mInstance;
    }

    private AddonManager() {
        Sledgehammer.setupListener(this);
    }

    List<IAddon> addons = new ArrayList<>();

    public void registerAddon(IAddon addon) {
        addons.add(addon);
    }

    public void onEnable() {
        addons.forEach(IAddon::onEnable);
    }

    public void onDisable() {
        addons.forEach(IAddon::onDisable);
    }

    public void onLoad() {
        addons.forEach(IAddon::onLoad);
    }

    @EventHandler
    public void onPluginMessage(PluginMessageEvent e) {
        IAddon addon = getServerByChannel(e.getTag());
        if(addon != null) addon.onPluginMessage(e);
    }

    private IAddon getServerByChannel(String channel) {
        for(IAddon a : addons) {
            if(a.getMessageChannels() != null) {
                if(Arrays.asList(a.getMessageChannels()).contains(channel)) return a;
            }
        }

        return null;
    }


}
