/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - Sledgehammer.java
 *
 * Sledgehammer is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Sledgehammer is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Sledgehammer.  If not, see <https://github.com/noahhusby/Sledgehammer/blob/master/LICENSE/>.
 */

package com.noahhusby.sledgehammer.proxy;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.noahhusby.sledgehammer.proxy.addons.AddonManager;
import com.noahhusby.sledgehammer.proxy.addons.terramap.TerramapAddon;
import com.noahhusby.sledgehammer.proxy.commands.BorderCommand;
import com.noahhusby.sledgehammer.proxy.commands.CsTpllCommand;
import com.noahhusby.sledgehammer.proxy.commands.SledgehammerAdminCommand;
import com.noahhusby.sledgehammer.proxy.commands.SledgehammerCommand;
import com.noahhusby.sledgehammer.proxy.commands.TpllCommand;
import com.noahhusby.sledgehammer.proxy.commands.TplloCommand;
import com.noahhusby.sledgehammer.proxy.commands.WarpCommand;
import com.noahhusby.sledgehammer.proxy.config.ConfigHandler;
import com.noahhusby.sledgehammer.proxy.config.ServerHandler;
import com.noahhusby.sledgehammer.proxy.datasets.OpenStreetMaps;
import com.noahhusby.sledgehammer.proxy.players.FlaggedBorderCheckerThread;
import com.noahhusby.sledgehammer.proxy.players.BorderCheckerThread;

import lombok.Getter;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class Sledgehammer extends Plugin implements Listener {
    public static Logger logger;
    public static Sledgehammer sledgehammer;
    @Getter private final ScheduledThreadPoolExecutor generalThreads = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(16, new ThreadFactoryBuilder().setNameFormat("sledgehammer-general-%d").build());

    @Getter private static AddonManager addonManager;

    @Override
    public void onEnable() {
        sledgehammer = this;
        logger = getLogger();
        generalThreads.setRemoveOnCancelPolicy(true);

        addListener(this);
        ConfigHandler.getInstance().init(getDataFolder());
        registerFromConfig();
    }

    @Override
    public void onDisable() {
        addonManager.onDisable();
    }

    /**
     * Called upon startup or reload. These are settings that can be changed without a restart
     */
    public void registerFromConfig() {
        generalThreads.getQueue().removeIf(r -> true);

        ProxyServer.getInstance().getPluginManager().unregisterCommands(this);

        ProxyServer.getInstance().getPluginManager().registerCommand(this, new SledgehammerCommand());
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new SledgehammerAdminCommand());

        ServerHandler.getInstance();

        if(!ConfigHandler.getInstance().isAuthCodeConfigured()) {
            logger.severe("------------------------------");
            for(int x = 0; x < 2; x++) {
                logger.severe("");
            }
            logger.severe("The authentication code is not configured, or configured incorrectly.");
            logger.severe("Please generate a valid authentication code using https://www.uuidgenerator.net/version4");
            logger.severe("Most Sledgehammer features will now be disabled.");
            for(int x = 0; x < 2; x++) {
                logger.severe("");
            }
            logger.severe("------------------------------");
            return;
        }

        addonManager = AddonManager.getInstance();
        addonManager.onDisable();
        
        if(ConfigHandler.terramapEnabled) addonManager.registerAddon(new TerramapAddon());

        addonManager.onEnable();

        if(!ConfigHandler.warpCommand.equals("")) {
            ProxyServer.getInstance().getPluginManager().registerCommand(this, new WarpCommand(ConfigHandler.warpCommand));
        }

        if(ConfigHandler.globalTpll) {
            ProxyServer.getInstance().getPluginManager().registerCommand(this, new TpllCommand());
            ProxyServer.getInstance().getPluginManager().registerCommand(this, new TplloCommand());
            ProxyServer.getInstance().getPluginManager().registerCommand(this, new CsTpllCommand());
        }

        if(ConfigHandler.borderTeleportation && !ConfigHandler.doesOfflineExist) {
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

        if(ConfigHandler.useOfflineMode && !ConfigHandler.doesOfflineExist) {
            logger.warning("------------------------------");
            for(int x = 0; x < 2; x++) {
                logger.warning("");
            }
            logger.warning("The offline OSM database was enabled without a proper database configured.");
            logger.warning("Please follow the guide on https://github.com/noahhusby/Sledgehammer/wiki/Border-Offline-Database to configure an offline database.");
            logger.warning("This feature will now be disabled.");
            for(int x = 0; x < 2; x++) {
                logger.warning("");
            }
            logger.warning("------------------------------");
            ConfigHandler.useOfflineMode = false;
        }

        ProxyServer.getInstance().registerChannel("sledgehammer:channel");

        if(ConfigHandler.borderTeleportation) {
            ProxyServer.getInstance().getPluginManager().registerCommand(this, new BorderCommand());
            generalThreads.scheduleAtFixedRate(new BorderCheckerThread(), 0, 10, TimeUnit.SECONDS);
            generalThreads.scheduleAtFixedRate(new FlaggedBorderCheckerThread(), 0, 5, TimeUnit.SECONDS);
        }

        OpenStreetMaps.getInstance().init();
    }

    /**
     * Add a new listener to the Sledgehammer plugin
     * @param listener The Bungeecord listener
     */
    public static void addListener(Listener listener) {
        ProxyServer.getInstance().getPluginManager().registerListener(sledgehammer, listener);
    }

    /**
     * Print a message on the debug logger. Only outputs with debug mode enabled
     * @param m The debug message
     */
    public static void debug(String m) {
        if(ConfigHandler.debug) logger.info(m);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(PostLoginEvent e) {
        if(e.getPlayer().hasPermission("sledgehammer.admin") && !ConfigHandler.getInstance().isAuthCodeConfigured()) {
            ChatUtil.sendAuthCodeWarning(e.getPlayer());
        }
    }

    /**
     * @author SmylerMC
     * @param l
     */
    public static void terminateListener(Listener l) {
        ProxyServer.getInstance().getPluginManager().unregisterListener(l);
    }
}
