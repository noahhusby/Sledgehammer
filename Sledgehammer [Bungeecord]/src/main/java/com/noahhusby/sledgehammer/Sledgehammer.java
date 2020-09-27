/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - Sledgehammer.java
 * All rights reserved.
 */

package com.noahhusby.sledgehammer;

import com.noahhusby.sledgehammer.addons.AddonManager;
import com.noahhusby.sledgehammer.addons.TerramapAddon;
import com.noahhusby.sledgehammer.commands.*;
import com.noahhusby.sledgehammer.config.ConfigHandler;
import com.noahhusby.sledgehammer.config.ServerConfig;
import com.noahhusby.sledgehammer.datasets.OpenStreetMaps;
import com.noahhusby.sledgehammer.handlers.PlayerLocationHandler;
import com.noahhusby.sledgehammer.maps.MapHandler;
import com.noahhusby.sledgehammer.network.SledgehammerNetworkManager;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.event.*;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class Sledgehammer extends Plugin implements Listener {
    public static Logger logger;
    public static Plugin sledgehammer;

    public static AddonManager addonManager;

    @Override
    public void onEnable() {
        this.sledgehammer = this;
        logger = getLogger();

        addonManager.onEnable();

        ConfigHandler.getInstance().init(getDataFolder());

        if(!ConfigHandler.warpCommand.equals("")) {
            ProxyServer.getInstance().getPluginManager().registerCommand(this, new WarpCommand());
        }

        if(ConfigHandler.globalTpll) {
            ProxyServer.getInstance().getPluginManager().registerCommand(this, new TpllCommand());
            ProxyServer.getInstance().getPluginManager().registerCommand(this, new CsTpllCommand());
        }

        if(ConfigHandler.borderTeleportation && !ConfigHandler.useOfflineMode) {
            logger.warning("------------------------------");
            for(int x = 0; x < 2; x++) {
                logger.warning("");
            }
            logger.warning("Automatic border teleportation was enabled without an offline OSM database.");
            logger.warning("This feature will now be disabled.");
            for(int x = 0; x < 2; x++) {
                logger.warning("");
            }
            logger.warning("------------------------------");
            ConfigHandler.borderTeleportation = false;
        }

        if(ConfigHandler.borderTeleportation && !ConfigHandler.doesOfflineExist) {
            logger.warning("------------------------------");
            for(int x = 0; x < 2; x++) {
                logger.warning("");
            }
            logger.warning("The offline OSM database was enabled without a proper database configured.");
            logger.warning("Please follow the guide on https://github.com/noahhusby/sledgehammer to configure an offline database.");
            logger.warning("This feature will now be disabled.");
            for(int x = 0; x < 2; x++) {
                logger.warning("");
            }
            logger.warning("------------------------------");
            ConfigHandler.borderTeleportation = false;
        }

        ProxyServer.getInstance().getPluginManager().registerCommand(this, new SledgehammerCommand());
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new SledgehammerAdminCommand());

        ProxyServer.getInstance().registerChannel("sledgehammer:channel");
        ProxyServer.getInstance().getPluginManager().registerListener(this, this);

        OpenStreetMaps.getInstance();

        MapHandler.getInstance().init();

        getProxy().getScheduler().schedule(this, () -> {

        }, 10,  TimeUnit.SECONDS);
    }

    @Override
    public void onDisable() {
        addonManager.onDisable();
    }

    @Override
    public void onLoad() {
        addonManager = AddonManager.getInstance();
        addonManager.registerAddon(new TerramapAddon());
        addonManager.onLoad();
    }

    @EventHandler
    public void onMessage(PluginMessageEvent e) {
        addonManager.onPluginMessage(e);
        SledgehammerNetworkManager.getInstance().onPluginMessageReceived(e);
    }

    @EventHandler
    public void onPlayerJoin(PostLoginEvent e) {
        PlayerLocationHandler.getInstance().onPlayerJoin(e.getPlayer());
    }

    @EventHandler
    public void onPlayerLeave(PlayerDisconnectEvent e) {
        PlayerLocationHandler.getInstance().onPlayerQuit(e.getPlayer());
    }

    @EventHandler
    public void onServerJoin(ServerConnectedEvent e) {
        ServerConfig.getInstance().onServerJoin(e);
    }

    public static void setupListener(Listener l) {
        ProxyServer.getInstance().getPluginManager().registerListener(sledgehammer, l);
    }
}
