package com.noahhusby.sledgehammer.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.noahhusby.sledgehammer.handlers.WarpHandler;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.function.Predicate;

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
    private Configuration configuration;

    private ConfigHandler() { }

    public void init(File dataFolder) {
        this.dataFolder = dataFolder;
        configurationFile = new File(dataFolder, "config.yml");
        warpFile = new File(dataFolder, "warps.json");
        serverFile = new File(dataFolder, "servers.json");

        loadWarpDB();
        loadServerDB();

        createConfig();

        try {
            configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(configurationFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    private void createConfig() {
        if (!dataFolder.exists())
            dataFolder.mkdir();

        File configurationF = new File(dataFolder, "config.yml");

        if (!configurationF.exists()) {
            try (InputStream in = getClass().getResourceAsStream("config.yml")) {
                Files.copy(in, configurationF.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
                json = FileUtils.readFileToString(warpFile, "UTF-8");
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
