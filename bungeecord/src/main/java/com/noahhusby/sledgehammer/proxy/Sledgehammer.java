/*
 * MIT License
 *
 * Copyright 2020-2022 noahhusby
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

package com.noahhusby.sledgehammer.proxy;

import com.noahhusby.sledgehammer.proxy.commands.BorderCommand;
import com.noahhusby.sledgehammer.proxy.commands.CsTpllCommand;
import com.noahhusby.sledgehammer.proxy.commands.SledgehammerAdminCommand;
import com.noahhusby.sledgehammer.proxy.commands.SledgehammerCommand;
import com.noahhusby.sledgehammer.proxy.commands.TpllCommand;
import com.noahhusby.sledgehammer.proxy.commands.TplloCommand;
import com.noahhusby.sledgehammer.proxy.commands.WarpCommand;
import com.noahhusby.sledgehammer.proxy.config.ConfigHandler;
import com.noahhusby.sledgehammer.proxy.config.SledgehammerConfig;
import com.noahhusby.sledgehammer.proxy.datasets.OpenStreetMaps;
import com.noahhusby.sledgehammer.proxy.modules.ModuleHandler;
import com.noahhusby.sledgehammer.proxy.network.NetworkHandler;
import com.noahhusby.sledgehammer.proxy.players.BorderCheckerThread;
import com.noahhusby.sledgehammer.proxy.players.FlaggedBorderCheckerThread;
import com.noahhusby.sledgehammer.proxy.players.PlayerHandler;
import com.noahhusby.sledgehammer.proxy.terramap.TerramapModule;
import lombok.Getter;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class Sledgehammer extends Plugin implements Listener {
    public static Logger logger;
    @Getter
    private static Sledgehammer instance;

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

    @Override
    public void onEnable() {
        instance = this;
        logger = getLogger();
        // Manually load version
        Constants.loadVersion(getDescription().getVersion());
        Configurator.setLevel("org.mongodb.driver.cluster", Level.FATAL);
        Configurator.setLevel("org.mongodb.driver.connection", Level.FATAL);

        ProxyServer.getInstance().registerChannel(Constants.serverChannel);
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new SledgehammerCommand());
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new SledgehammerAdminCommand());

        ConfigHandler.getInstance().init(getDataFolder());
        ModuleHandler.getInstance().registerModules(PlayerHandler.getInstance(), NetworkHandler.getInstance(), OpenStreetMaps.getInstance());
        ConfigHandler.getInstance().load();

        // Manual module handling
        if (SledgehammerConfig.terramap.terramapEnabled && (TerramapModule.instance == null || !ModuleHandler.getInstance().getModules().containsKey(TerramapModule.instance))) {
            ModuleHandler.getInstance().registerModule(new TerramapModule());
            ModuleHandler.getInstance().enable(TerramapModule.instance);
        } else if (SledgehammerConfig.terramap.terramapEnabled) {
            ModuleHandler.getInstance().enable(TerramapModule.instance);
        } else if (TerramapModule.instance != null && ModuleHandler.getInstance().getModules().containsKey(TerramapModule.instance)) {
            ModuleHandler.getInstance().disable(TerramapModule.instance);
            ModuleHandler.getInstance().unregisterModule(TerramapModule.instance);
        }

        if (!SledgehammerConfig.warps.warpCommand.equals("")) {
            ProxyServer.getInstance().getPluginManager().registerCommand(this, new WarpCommand(SledgehammerConfig.warps.warpCommand));
        }

        if (SledgehammerConfig.general.globalTpll) {
            ProxyServer.getInstance().getPluginManager().registerCommand(this, new TpllCommand());
            ProxyServer.getInstance().getPluginManager().registerCommand(this, new TplloCommand());
            ProxyServer.getInstance().getPluginManager().registerCommand(this, new CsTpllCommand());
        }

        if (SledgehammerConfig.geography.borderTeleportation) {
            ProxyServer.getInstance().getPluginManager().registerCommand(this, new BorderCommand());
            ProxyServer.getInstance().getScheduler().schedule(this, new BorderCheckerThread(), 0, 5, TimeUnit.SECONDS);
            ProxyServer.getInstance().getScheduler().schedule(this, new FlaggedBorderCheckerThread(), 0, 1, TimeUnit.SECONDS);
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
        onEnable();
        Sledgehammer.logger.warning("Reloaded Sledgehammer!");
    }
}
