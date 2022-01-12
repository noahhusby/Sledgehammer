/*
 * MIT License
 *
 * Copyright 2020-2021 noahhusby
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

package com.noahhusby.sledgehammer.proxy.config;

import com.noahhusby.lib.application.config.Configuration;
import com.noahhusby.lib.data.sql.Credentials;
import com.noahhusby.lib.data.sql.MySQL;
import com.noahhusby.lib.data.sql.structure.Structure;
import com.noahhusby.lib.data.sql.structure.Type;
import com.noahhusby.lib.data.storage.Storage;
import com.noahhusby.lib.data.storage.handlers.LocalStorageHandler;
import com.noahhusby.lib.data.storage.handlers.SQLStorageHandler;
import com.noahhusby.sledgehammer.proxy.Sledgehammer;
import com.noahhusby.sledgehammer.proxy.players.PlayerHandler;
import com.noahhusby.sledgehammer.proxy.servers.ServerHandler;
import com.noahhusby.sledgehammer.proxy.terramap.MapStyleRegistry;
import com.noahhusby.sledgehammer.proxy.warp.WarpHandler;
import lombok.Getter;
import lombok.SneakyThrows;
import net.md_5.bungee.api.ProxyServer;

import java.io.File;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class ConfigHandler {
    private static ConfigHandler instance = null;

    public static ConfigHandler getInstance() {
        return instance == null ? instance = new ConfigHandler() : instance;
    }

    private File dataFolder;

    public static File warpFile;
    public static File serverFile;
    public static File localStorage;
    public static File attributeFile;
    public static File warpGroupsFile;
    @Getter
    private File offlineBin;

    private ConfigHandler() {
    }

    /**
     * Creates initial data structures upon startup
     *
     * @param dataFolder Sledgehammer plugin folder
     */
    public void init(File dataFolder) {

        this.dataFolder = dataFolder;
        if (!dataFolder.exists()) {
            dataFolder.mkdir();
        }
        localStorage = new File(dataFolder, "local");
        if (!localStorage.exists()) {
            localStorage.mkdir();
        }
        warpFile = new File(localStorage, "warps.json");
        serverFile = new File(localStorage, "servers.json");
        attributeFile = new File(localStorage, "attributes.json");
        warpGroupsFile = new File(localStorage, "warpgroups.json");
        offlineBin = new File(localStorage, "offline.bin");
    }

    @SneakyThrows
    public void load() {
        Configuration configuration = Configuration.of(SledgehammerConfig.class, dataFolder);
        configuration.sync(SledgehammerConfig.class);
        if (SledgehammerConfig.terramap.terramapEnabled) {
            File customMaps = new File(dataFolder + "/" + MapStyleRegistry.FILENAME);
            MapStyleRegistry.setConfigMapFile(customMaps);
            MapStyleRegistry.loadFromConfigFile();
        }
        loadHandlers();
    }

    public void unload() {
        ServerHandler.getInstance().getServers().close();
        WarpHandler.getInstance().getWarps().close();
        PlayerHandler.getInstance().getAttributes().close();
        WarpHandler.getInstance().getWarpGroups().close();
    }

    private void loadHandlers() {
        Storage serverData = ServerHandler.getInstance().getServers();
        serverData.clearHandlers();
        serverData.registerHandler(new LocalStorageHandler(ConfigHandler.serverFile));

        Storage warpData = WarpHandler.getInstance().getWarps();
        warpData.clearHandlers();
        warpData.registerHandler(new LocalStorageHandler(ConfigHandler.warpFile));

        Storage attributeData = PlayerHandler.getInstance().getAttributes();
        attributeData.clearHandlers();
        attributeData.registerHandler(new LocalStorageHandler(ConfigHandler.attributeFile));

        Storage warpGroups = WarpHandler.getInstance().getWarpGroups();
        warpGroups.clearHandlers();
        warpGroups.registerHandler(new LocalStorageHandler(ConfigHandler.warpGroupsFile));

        if (SledgehammerConfig.database.databaseType.equals("SQL")) {
            Credentials credentials = new Credentials(SledgehammerConfig.database.host, SledgehammerConfig.database.port, SledgehammerConfig.database.user, SledgehammerConfig.database.password, SledgehammerConfig.database.database);
            {
                SQLStorageHandler sqlStorageHandler = new SQLStorageHandler(new MySQL(credentials), "Servers",
                        Structure.builder()
                                .add("Name", Type.TEXT)
                                .add("EarthServer", Type.TEXT)
                                .add("Nick", Type.TEXT)
                                .add("Locations", Type.TEXT)
                                .add("XOffset", Type.INT)
                                .add("ZOffset", Type.INT)
                                .add("StealthMode", Type.TEXT)
                                .repair(true)
                                .build());
                sqlStorageHandler.setPriority(100);
                serverData.registerHandler(sqlStorageHandler);
            }

            {
                SQLStorageHandler sqlStorageHandler = new SQLStorageHandler(new MySQL(credentials), "Warps",
                        Structure.builder()
                                .add("Id", Type.INT)
                                .add("Name", Type.TEXT)
                                .add("Server", Type.TEXT)
                                .add("Point", Type.TEXT)
                                .add("HeadId", Type.TEXT)
                                .add("Global", Type.TEXT)
                                .repair(true)
                                .build());
                sqlStorageHandler.setPriority(100);
                warpData.registerHandler(sqlStorageHandler);
            }

            {
                SQLStorageHandler sqlStorageHandler = new SQLStorageHandler(new MySQL(credentials), "Attributes",
                        Structure.builder()
                                .add("UUID", Type.TEXT)
                                .add("Attributes", Type.TEXT)
                                .repair(true)
                                .build());
                sqlStorageHandler.setPriority(100);
                attributeData.registerHandler(sqlStorageHandler);
            }

            {
                SQLStorageHandler sqlStorageHandler = new SQLStorageHandler(new MySQL(credentials), "WarpGroups",
                        Structure.builder()
                                .add("Id", Type.TEXT)
                                .add("Name", Type.TEXT)
                                .add("HeadId", Type.TEXT)
                                .add("Warps", Type.TEXT)
                                .add("Servers", Type.TEXT)
                                .repair(true)
                                .build());
                sqlStorageHandler.setPriority(100);
                warpGroups.registerHandler(sqlStorageHandler);
            }
        }

        ProxyServer.getInstance().getScheduler().schedule(Sledgehammer.getInstance(), () -> {
            serverData.loadAsync();
            warpData.loadAsync();
            attributeData.loadAsync();
            warpGroups.loadAsync();
        }, 10, TimeUnit.SECONDS);
    }

    /**
     * Gets whether the authentication code is configured correctly
     *
     * @return True if configured correctly, false if not
     */
    public boolean isAuthenticationConfigured() {
        Pattern p = Pattern.compile("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$");
        return p.matcher(SledgehammerConfig.general.authenticationCode).matches();
    }

    /**
     * Reloads the config
     */
    public void reload() {
        unload();
        load();
    }

    /**
     * Migrates the data from local storage to databases
     */
    public void migrate() {
        ServerHandler.getInstance().getServers().migrate(0);
        WarpHandler.getInstance().getWarpGroups().migrate(0);
        WarpHandler.getInstance().getWarps().migrate(0);
        PlayerHandler.getInstance().getAttributes().migrate(0);
    }
}
