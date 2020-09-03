package com.noahhusby.sledgehammer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.noahhusby.sledgehammer.commands.*;
import com.noahhusby.sledgehammer.commands.admin.SetupAdminCommand;
import com.noahhusby.sledgehammer.config.ConfigHandler;
import com.noahhusby.sledgehammer.datasets.OpenStreetMaps;
import com.noahhusby.sledgehammer.handlers.CommunicationHandler;
import com.noahhusby.sledgehammer.handlers.PlayerLocationHandler;
import com.noahhusby.sledgehammer.handlers.TaskHandler;
import com.noahhusby.sledgehammer.handlers.WarpHandler;
import com.noahhusby.sledgehammer.util.ProxyUtil;
import com.noahhusby.sledgehammer.util.Warp;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import net.md_5.bungee.event.EventHandler;
import org.apache.commons.io.FileUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.nio.file.Files;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.logging.Logger;

public class Sledgehammer extends Plugin implements Listener {
    public static Logger logger;
    private static Configuration configuration;
    public static Plugin sledgehammer;

    @Override
    public void onEnable() {
        this.sledgehammer = this;
        logger = getLogger();

        ConfigHandler.getInstance().init(getDataFolder());
        configuration = ConfigHandler.getInstance().getConfiguration();

        if(!configuration.getString("warp-command").equals("")) {
            ProxyServer.getInstance().getPluginManager().registerCommand(this, new WarpCommand());
        }

        if(configuration.getBoolean("global-tpll")) {
            ProxyServer.getInstance().getPluginManager().registerCommand(this, new TpllCommand());
            ProxyServer.getInstance().getPluginManager().registerCommand(this, new CsTpllCommand());
        }

        ProxyServer.getInstance().getPluginManager().registerCommand(this, new SledgehammerCommand());
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new SledgehammerAdminCommand());

        ProxyServer.getInstance().registerChannel("sledgehammer:channel");
        ProxyServer.getInstance().getPluginManager().registerListener(this, this);

        OpenStreetMaps.getInstance();

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

    public static void setupAdminCommandListener(SetupAdminCommand s) {
        ProxyServer.getInstance().getPluginManager().registerListener(sledgehammer, s);
    }
}
