package com.noahhusby.sledgehammer;

import com.noahhusby.sledgehammer.commands.*;
import com.noahhusby.sledgehammer.config.ConfigHandler;
import com.noahhusby.sledgehammer.datasets.OpenStreetMaps;
import com.noahhusby.sledgehammer.handlers.PlayerLocationHandler;
import com.noahhusby.sledgehammer.handlers.TaskHandler;
import com.noahhusby.sledgehammer.map.MapHandler;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class Sledgehammer extends Plugin implements Listener {
    public static Logger logger;
    public static Plugin sledgehammer;

    @Override
    public void onEnable() {
        this.sledgehammer = this;
        logger = getLogger();

        ConfigHandler.getInstance().init(getDataFolder());

        ProxyServer.getInstance().getPluginManager().registerCommand(this, new WarpTemp());

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

    @EventHandler
    public void onMessage(PluginMessageEvent e) {
        TaskHandler.getInstance().onIncomingMessage(e);
    }

    @EventHandler
    public void onPlayerJoin(PostLoginEvent e) {
        PlayerLocationHandler.getInstance().onPlayerJoin(e.getPlayer());
    }

    @EventHandler
    public void onPlayerLeave(PlayerDisconnectEvent e) {
        PlayerLocationHandler.getInstance().onPlayerQuit(e.getPlayer());
    }

    public static void setupListener(Listener l) {
        ProxyServer.getInstance().getPluginManager().registerListener(sledgehammer, l);
    }
}
