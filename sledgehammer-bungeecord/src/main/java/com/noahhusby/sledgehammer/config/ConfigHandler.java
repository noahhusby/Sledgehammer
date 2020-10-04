/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - ConfigHandler.java
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
 * You should have received a copy of the GNU General Public License
 * along with Sledgehammer.  If not, see <https://github.com/noahhusby/Sledgehammer/blob/master/LICENSE/>.
 */

package com.noahhusby.sledgehammer.config;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.noahhusby.sledgehammer.Sledgehammer;
import com.noahhusby.sledgehammer.warp.WarpHandler;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public class ConfigHandler {
    private static ConfigHandler mInstance = null;

    public static ConfigHandler getInstance() {
        if(mInstance == null) mInstance = new ConfigHandler();
        return mInstance;
    }

    private File dataFolder;

    private File configurationFile;
    private File warpFile;
    private File serverFile;

    private ConfigHandler() { }

    public static net.minecraftforge.common.config.Configuration config;
    private boolean authCodeConfigured;

    public static String authenticationCode = "";
    public static String messagePrefix = "";
    public static boolean globalTpll = false;

    public static String warpCommand = "";
    public static String warpMode = "";

    public static boolean useOfflineMode = false;
    public static boolean borderTeleportation = false;
    public static int zoom = 0;

    public static boolean doesOfflineExist = false;

    public static boolean mapEnabled = false;
    public static String mapHost = "";
    public static String mapPort = "";

    public static String mapTitle = "";
    public static String mapSubtitle = "";

    public static double startingLon = 0.0;
    public static double startingLat = 0.0;
    public static int startingZoom = 0;
    public static int mapTimeout = 15;
    public static String mapLink = "";

    private String category;

    Map<String, List<String>> categories = Maps.newHashMap();

    public void init(File dataFolder) {
        this.dataFolder = dataFolder;
        configurationFile = new File(dataFolder, "sledgehammer.cfg");
        warpFile = new File(dataFolder, "warps.json");
        serverFile = new File(dataFolder, "servers.json");

        loadWarpDB();
        loadServerDB();

        createConfig();

        config = new net.minecraftforge.common.config.Configuration(configurationFile);
        cat("General", "General options for sledgehammer");
        authenticationCode = config.getString(prop("Network Authentication Code"), "General", ""
                , "Generate a new key using https://uuidgenerator.net/version4\nAll corresponding sledgehammer clients must have the same code\nDon't share this key with anyone you don't trust as it will allow anybody to run any command on connected servers.");

        Pattern p = Pattern.compile("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$");
        authCodeConfigured = p.matcher(authenticationCode).matches();

        messagePrefix = config.getString(prop("Message Prefix"), "General", "&9&lBTE &8&l> "
                , "The prefix of messages broadcasted to players from the proxy");
        globalTpll = config.getBoolean(prop("Global Tpll"), "General", true, "Set this to false to disable global tpll [/tpll & /cs tpll]");
        order();

        cat("Warps", "Options for warps");
        warpCommand = config.getString(prop("Warp Command"), "Warps", "nwarp",
                "The command for network-wide warping. Leave blank to disable\nPermissions: sledgehammer.warp for teleporting, and sledgehammer.warp.admin for setting warps.");
        warpMode = config.getString(prop("Warp Mode"), "Warps", "chat",
                "The default way to list warps.\nUse 'chat' to list the syntax for the warp command, or `gui` to open the GUI automatically.\n" +
                        "Note: The mode will always default to 'chat' when the GUI is unavailable.");


        cat("Geography", "Options for OpenStreetMaps and Teleportation.");
        zoom = config.getInt(prop("Zoom level"), "Geography", 12, 1, 19,
                "Zoom detail level for OSM requests.");
        useOfflineMode = config.getBoolean(prop("OSM Offline Mode"), "Geography", false,
                "Set false for fetching the latest data from OSM (more up to date), or true for using a downloaded database.\nPlease follow the guide on https://github.com/noahhusby/sledgehammer about downloading and configuring the offline database.");
        borderTeleportation = config.getBoolean(prop("Auto Border Teleportation"), "Geography", false,
                "Set to false to disable automatic border teleportation, or true to enable it. (Note: OSM Offline Mode must be set to true for this to be enabled.");
        order();

        cat("Map", "Options for sledgehammer's map");
        mapEnabled = config.getBoolean(prop("Enable"), "Map", false,
                "Set this to true to enable sledgehammer's map");
        mapHost = config.getString(prop("Host"), "Map", "127.0.0.1",
                "The websocket url/ip where sledgehammer map is running");
        mapPort = config.getString(prop("Port"), "Map", "7000",
                "The port that the map websocket is running.\nThe websocket port can be changed in the map's config file");
        mapTitle = config.getString(prop("Title"), "Map", "A BTE Network", "");
        mapSubtitle = config.getString(prop("Subtitle"), "Map", "IP: bte-network.net", "");
        startingLon = config.getFloat(prop("Longitude"), "Map", 0, -90, 90,
                "The starting longitude when the map loads.");
        startingLat = config.getFloat(prop("Latitude"), "Map", 0, -90, 90,
                "The starting latitude when the map loads.");
        startingZoom = config.getInt(prop("Zoom"), "Map", 6, 5, 18,
                "The starting zoom when the map loads");
        mapTimeout = config.getInt(prop("Map Session Timeout"), "Map", 10, 5, 60,
                "How long (in minutes) a session will last before the player needs to invoke the map command again.");
        mapLink = config.getString(prop("Map Link"), "Map", "http://map.bte-network.net",
                "The direct http link for the map. This is the link that players will interact with.\n" +
                        "NOTE: You must put either http:// or https:// at the beginning");
        order();

        File f = new File(dataFolder, "offline.bin");
        doesOfflineExist = f.exists();

        config.save();
    }

    public boolean isAuthCodeConfigured() {
        return authCodeConfigured;
    }

    public void reload() {
        Sledgehammer.logger.info("Reloaded the config");
        init(dataFolder);
    }

    public String prop(String n) {
        categories.get(category).add(n);
        return n;
    }

    public void cat(String category, String comment) {
        this.category = category;
        if(!categories.containsKey(category)) {
            categories.put(category, new ArrayList<>());
        }
        config.addCustomCategoryComment(category, comment);
    }

    public void order() {
        config.setCategoryPropertyOrder(category, categories.get(category));
    }

    public File getOfflineBin() {
        return new File(dataFolder, "offline.bin");
    }

    private void createConfig() {
        if (!dataFolder.exists())
            dataFolder.mkdir();
    }

    public void loadWarpDB() {
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

    public void saveWarpDB() {
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

    public void loadServerDB() {
        if (serverFile.exists())
        {
            String json = null;
            try
            {
                Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting()
                        .create();
                json = FileUtils.readFileToString(serverFile, "UTF-8");
                ServerConfig.setInstance(gson.fromJson(json, ServerConfig.class));
                ServerConfig.getInstance();
                saveServerDB();
            }
            catch (Throwable e)
            {
                e.printStackTrace();
                System.err.println("\n" + json);
            }
            return;
        }

        ServerConfig.setInstance(new ServerConfig());
        ServerConfig.getInstance();
        saveServerDB();
    }

    public void saveServerDB() {
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
            FileUtils.writeStringToFile(serverFile, gson.toJson(ServerConfig.getInstance()), "UTF-8");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}
