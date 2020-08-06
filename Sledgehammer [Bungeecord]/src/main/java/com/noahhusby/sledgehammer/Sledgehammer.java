package com.noahhusby.sledgehammer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.noahhusby.sledgehammer.commands.CsTpllCommand;
import com.noahhusby.sledgehammer.commands.SledgehammerCommand;
import com.noahhusby.sledgehammer.commands.TpllCommand;
import com.noahhusby.sledgehammer.commands.WarpCommand;
import com.noahhusby.sledgehammer.datasets.OpenStreetMaps;
import com.noahhusby.sledgehammer.handlers.CommunicationHandler;
import com.noahhusby.sledgehammer.handlers.PlayerLocationHandler;
import com.noahhusby.sledgehammer.handlers.WarpHandler;
import com.noahhusby.sledgehammer.util.Warp;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
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
    private static File warpFile = null;
    private final File configFile = new File(getDataFolder(), "config.yml");
    public static Configuration configuration;

    @Override
    public void onEnable() {
        logger = getLogger();
        initConfig();

        warpFile = new File(getDataFolder(), "warps.json");
        loadWarpDB();

        if(!configuration.getString("warp-command").equals("")) {
            ProxyServer.getInstance().getPluginManager().registerCommand(this, new WarpCommand());
        }

        if(configuration.getBoolean("global-tpll")) {
            ProxyServer.getInstance().getPluginManager().registerCommand(this, new TpllCommand());
            ProxyServer.getInstance().getPluginManager().registerCommand(this, new CsTpllCommand());
        }

        ProxyServer.getInstance().getPluginManager().registerCommand(this, new SledgehammerCommand());

        ProxyServer.getInstance().registerChannel("sledgehammer:channel");
        ProxyServer.getInstance().getPluginManager().registerListener(this, this);

        OpenStreetMaps.getInstance();

        getProxy().getScheduler().schedule(this, () -> {

        }, 10,  TimeUnit.SECONDS);
    }

    @EventHandler
    public void onMessage(PluginMessageEvent e) {
        CommunicationHandler.onIncomingMessage(e);
    }

    @EventHandler
    public void onPlayerJoin(PostLoginEvent e) {
        PlayerLocationHandler.getInstance().onPlayerJoin(e.getPlayer());
    }

    @EventHandler
    public void onPlayerLeave(PlayerDisconnectEvent e) {
        PlayerLocationHandler.getInstance().onPlayerQuit(e.getPlayer());
    }


    private void initConfig() {
        createConfig();

        try {
            configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createConfig() {
        if (!getDataFolder().exists())
            getDataFolder().mkdir();

        File file = new File(getDataFolder(), "config.yml");

        if (!file.exists()) {
            try (InputStream in = getResourceAsStream("config.yml")) {
                Files.copy(in, file.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void loadWarpDB() {
        if (warpFile.exists())
        {
            String json = null;
            try
            {
                Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting()
                        .create();
                json = FileUtils.readFileToString(warpFile, "UTF-8");
                WarpHandler.setInstance(gson.fromJson(json, WarpHandler.class));
                WarpHandler.getInstance();
                saveWarpDB();
            }
            catch (Throwable e)
            {
                e.printStackTrace();
                System.err.println("\n" + json);
            }
            return;
        }

        WarpHandler.setInstance(new WarpHandler());
        WarpHandler.getInstance();
        saveWarpDB();
    }

    public static void saveWarpDB() {
        try
        {
            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
            Predicate<String> nonnull = new Predicate<String>()
            {
                @Override
                public boolean test(String t)
                {
                    return t == null || t.isEmpty();
                }
            };
            FileUtils.writeStringToFile(warpFile, gson.toJson(WarpHandler.getInstance()), "UTF-8");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


}
