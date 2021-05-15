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

import com.noahhusby.sledgehammer.proxy.commands.BorderCommand;
import com.noahhusby.sledgehammer.proxy.commands.CsTpllCommand;
import com.noahhusby.sledgehammer.proxy.commands.SledgehammerAdminCommand;
import com.noahhusby.sledgehammer.proxy.commands.SledgehammerCommand;
import com.noahhusby.sledgehammer.proxy.commands.TpllCommand;
import com.noahhusby.sledgehammer.proxy.commands.TplloCommand;
import com.noahhusby.sledgehammer.proxy.commands.WarpCommand;
import com.noahhusby.sledgehammer.proxy.config.ConfigHandler;
import com.noahhusby.sledgehammer.proxy.datasets.OpenStreetMaps;
import com.noahhusby.sledgehammer.proxy.modules.ModuleHandler;
import com.noahhusby.sledgehammer.proxy.network.NetworkHandler;
import com.noahhusby.sledgehammer.proxy.permissions.PermissionHandler;
import com.noahhusby.sledgehammer.proxy.players.BorderCheckerThread;
import com.noahhusby.sledgehammer.proxy.players.FlaggedBorderCheckerThread;
import com.noahhusby.sledgehammer.proxy.players.PlayerManager;
import com.noahhusby.sledgehammer.proxy.terramap.TerramapAddon;
import lombok.Getter;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class Sledgehammer extends Plugin implements Listener {
    public static Logger logger;
    @Getter
    private static Sledgehammer instance;

    @Override
    public void onEnable() {
        instance = this;
        logger = getLogger();

        ProxyServer.getInstance().registerChannel(Constants.serverChannel);

        ProxyServer.getInstance().getPluginManager().registerCommand(this, new SledgehammerCommand());
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new SledgehammerAdminCommand());

        ConfigHandler.getInstance().init(getDataFolder());
        ModuleHandler.getInstance().registerModules(PlayerManager.getInstance(), NetworkHandler.getInstance(), OpenStreetMaps.getInstance(), PermissionHandler.getInstance());
        ConfigHandler.getInstance().load();

        if (!ConfigHandler.getInstance().isAuthCodeConfigured()) {
            ChatUtil.sendMessageBox(ProxyServer.getInstance().getConsole(), ChatColor.DARK_RED + "WARNING", ChatUtil.combine(ChatColor.RED,
                    "The sledgehammer authentication code is not configured, or is configured incorrectly.\n" +
                    "Please generate a valid authentication code using https://www.uuidgenerator.net/version4\n"
                    + "Most Sledgehammer features will now be disabled."));
            return;
        }

        // Manual module handling
        if (ConfigHandler.terramapEnabled && TerramapAddon.instance == null) {
            ModuleHandler.getInstance().registerModule(new TerramapAddon());
            ModuleHandler.getInstance().enable(TerramapAddon.instance);
        } else if (ConfigHandler.terramapEnabled) {
            ModuleHandler.getInstance().enable(TerramapAddon.instance);
        } else if (TerramapAddon.instance != null && ModuleHandler.getInstance().getModules().containsKey(TerramapAddon.instance)) {
            ModuleHandler.getInstance().disable(TerramapAddon.instance);
            ModuleHandler.getInstance().unregisterModule(TerramapAddon.instance);
        }

        if (!ConfigHandler.warpCommand.equals("")) {
            ProxyServer.getInstance().getPluginManager().registerCommand(this, new WarpCommand(ConfigHandler.warpCommand));
        }

        if (ConfigHandler.globalTpll) {
            ProxyServer.getInstance().getPluginManager().registerCommand(this, new TpllCommand());
            ProxyServer.getInstance().getPluginManager().registerCommand(this, new TplloCommand());
            ProxyServer.getInstance().getPluginManager().registerCommand(this, new CsTpllCommand());
        }

        if (ConfigHandler.borderTeleportation) {
            ProxyServer.getInstance().getPluginManager().registerCommand(this, new BorderCommand());
            ProxyServer.getInstance().getScheduler().schedule(this, new BorderCheckerThread(), 0, 10, TimeUnit.SECONDS);
            ProxyServer.getInstance().getScheduler().schedule(this, new FlaggedBorderCheckerThread(), 0, 10, TimeUnit.SECONDS);
        }

        ModuleHandler.getInstance().enableAll();
    }

    @Override
    public void onDisable() {
        ProxyServer.getInstance().getScheduler().cancel(this);

        ProxyServer.getInstance().unregisterChannel(Constants.serverChannel);
        ProxyServer.getInstance().getPluginManager().unregisterCommands(this);

        ModuleHandler.getInstance().disableAll();
        ModuleHandler.getInstance().unregisterModules();
        ConfigHandler.getInstance().unload();
    }

    public void reload() {
        onDisable();
        ConfigHandler.getInstance().reload();
        onEnable();
        Sledgehammer.logger.warning("Reloaded Sledgehammer!");
    }

    /**
     * Add a new listener to the Sledgehammer plugin
     *
     * @param listener {@link Listener}
     */
    public static void addListener(Listener listener) {
        ProxyServer.getInstance().getPluginManager().registerListener(instance, listener);
    }

    /**
     * Removes a listener
     *
     * @param listener {@link Listener}
     */
    public static void removeListener(Listener listener) {
        ProxyServer.getInstance().getPluginManager().unregisterListener(listener);
    }
}
