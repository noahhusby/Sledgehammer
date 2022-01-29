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

package com.noahhusby.sledgehammer.proxy.config;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCommandException;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;
import com.noahhusby.lib.application.config.Configuration;
import com.noahhusby.lib.data.sql.Credentials;
import com.noahhusby.lib.data.sql.MySQL;
import com.noahhusby.lib.data.sql.structure.Structure;
import com.noahhusby.lib.data.sql.structure.Type;
import com.noahhusby.lib.data.storage.Storage;
import com.noahhusby.lib.data.storage.handlers.LocalStorageHandler;
import com.noahhusby.lib.data.storage.handlers.MongoStorageHandler;
import com.noahhusby.lib.data.storage.handlers.SQLStorageHandler;
import com.noahhusby.sledgehammer.common.warps.Warp;
import com.noahhusby.sledgehammer.common.warps.WarpGroup;
import com.noahhusby.sledgehammer.proxy.Sledgehammer;
import com.noahhusby.sledgehammer.proxy.players.Attribute;
import com.noahhusby.sledgehammer.proxy.players.PlayerHandler;
import com.noahhusby.sledgehammer.proxy.servers.ServerHandler;
import com.noahhusby.sledgehammer.proxy.servers.SledgehammerServer;
import com.noahhusby.sledgehammer.proxy.terramap.MapStyleLibrary;
import com.noahhusby.sledgehammer.proxy.warp.WarpHandler;
import lombok.Getter;
import lombok.SneakyThrows;
import net.md_5.bungee.api.ProxyServer;

import java.io.File;
import java.util.concurrent.TimeUnit;

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
        warpGroupsFile = new File(localStorage, "groups.json");
        offlineBin = new File(localStorage, "offline.bin");
    }

    @SneakyThrows
    public void load() {
        Configuration configuration = Configuration.of(SledgehammerConfig.class, dataFolder);
        configuration.sync(SledgehammerConfig.class);
        SledgehammerConfig.validate();
        if (SledgehammerConfig.terramap.terramapEnabled) {
            File customMaps = new File(dataFolder + "/" + MapStyleLibrary.FILENAME);
            MapStyleLibrary.setConfigMapFile(customMaps);
            MapStyleLibrary.loadFromConfigFile();
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
        Storage<SledgehammerServer> serverData = ServerHandler.getInstance().getServers();
        serverData.handlers().clear();
        serverData.handlers().register(new LocalStorageHandler<>(ConfigHandler.serverFile));

        Storage<Warp> warpData = WarpHandler.getInstance().getWarps();
        warpData.handlers().clear();
        warpData.handlers().register(new LocalStorageHandler<>(ConfigHandler.warpFile));

        Storage<Attribute> attributeData = PlayerHandler.getInstance().getAttributes();
        attributeData.handlers().clear();
        attributeData.handlers().register(new LocalStorageHandler<>(ConfigHandler.attributeFile));

        Storage<WarpGroup> warpGroups = WarpHandler.getInstance().getWarpGroups();
        warpGroups.handlers().clear();
        warpGroups.handlers().register(new LocalStorageHandler<>(ConfigHandler.warpGroupsFile));

        if (SledgehammerConfig.database.type.equals("SQL")) {
            Credentials credentials = new Credentials(SledgehammerConfig.database.host, SledgehammerConfig.database.port, SledgehammerConfig.database.user, SledgehammerConfig.database.password, SledgehammerConfig.database.database);
            {
                SQLStorageHandler<SledgehammerServer> sqlStorageHandler = new SQLStorageHandler<>(new MySQL(credentials), "Servers",
                        Structure.builder()
                                .add("name", Type.TEXT)
                                .add("earthServer", Type.TEXT)
                                .add("nick", Type.TEXT)
                                .add("locations", Type.TEXT)
                                .add("xOffset", Type.INT)
                                .add("zOffset", Type.INT)
                                .add("stealthMode", Type.TEXT)
                                .repair(true)
                                .build());
                sqlStorageHandler.setPriority(100);
                serverData.handlers().register(sqlStorageHandler);
            }

            {
                SQLStorageHandler<Warp> sqlStorageHandler = new SQLStorageHandler<>(new MySQL(credentials), "Warps",
                        Structure.builder()
                                .add("id", Type.INT)
                                .add("name", Type.TEXT)
                                .add("server", Type.TEXT)
                                .add("point", Type.TEXT)
                                .add("headId", Type.TEXT)
                                .add("global", Type.TEXT)
                                .repair(true)
                                .build());
                sqlStorageHandler.setPriority(100);
                warpData.handlers().register(sqlStorageHandler);
            }

            {
                SQLStorageHandler<Attribute> sqlStorageHandler = new SQLStorageHandler<>(new MySQL(credentials), "Attributes",
                        Structure.builder()
                                .add("UUID", Type.TEXT)
                                .add("Attributes", Type.TEXT)
                                .repair(true)
                                .build());
                sqlStorageHandler.setPriority(100);
                attributeData.handlers().register(sqlStorageHandler);
            }

            {
                SQLStorageHandler<WarpGroup> sqlStorageHandler = new SQLStorageHandler<>(new MySQL(credentials), "Groups",
                        Structure.builder()
                                .add("id", Type.TEXT)
                                .add("name", Type.TEXT)
                                .add("headId", Type.TEXT)
                                .add("warps", Type.TEXT)
                                .add("servers", Type.TEXT)
                                .repair(true)
                                .build());
                sqlStorageHandler.setPriority(100);
                warpGroups.handlers().register(sqlStorageHandler);

                ProxyServer.getInstance().getScheduler().schedule(Sledgehammer.getInstance(), () -> {
                    serverData.loadAsync();
                    warpData.loadAsync();
                    attributeData.loadAsync();
                    warpGroups.loadAsync();
                }, 10, TimeUnit.SECONDS);
            }
        } else if (SledgehammerConfig.database.type.equals("MONGO")) {
            MongoCredential credential = MongoCredential.createCredential(SledgehammerConfig.database.user, SledgehammerConfig.database.database, SledgehammerConfig.database.password.toCharArray());
            MongoClient client = new MongoClient(new ServerAddress(SledgehammerConfig.database.host, SledgehammerConfig.database.port), credential, MongoClientOptions.builder().build());
            MongoDatabase database = client.getDatabase(SledgehammerConfig.database.database);
            {
                MongoStorageHandler<SledgehammerServer> mongoStorageHandler = new MongoStorageHandler<>(database.getCollection("servers"));
                mongoStorageHandler.setPriority(100);
                mongoStorageHandler.enableEventUpdates(e -> {
                    if (e instanceof MongoCommandException) {
                        Sledgehammer.logger.warning("Failed to enable event-driven updates for MongoDB. If sledgehammer is being used in a multi-proxy configuration, please set MongoDB up as a replica set.");
                    }
                });
                serverData.handlers().register(mongoStorageHandler);
            }

            {
                MongoStorageHandler<Warp> mongoStorageHandler = new MongoStorageHandler<>(database.getCollection("warps"));
                mongoStorageHandler.setPriority(100);
                mongoStorageHandler.enableEventUpdates();
                warpData.handlers().register(mongoStorageHandler);
            }

            {
                MongoStorageHandler<Attribute> mongoStorageHandler = new MongoStorageHandler<>(database.getCollection("attributes"));
                mongoStorageHandler.setPriority(100);
                mongoStorageHandler.enableEventUpdates();
                attributeData.handlers().register(mongoStorageHandler);
            }

            {
                MongoStorageHandler<WarpGroup> mongoStorageHandler = new MongoStorageHandler<>(database.getCollection("groups"));
                mongoStorageHandler.setPriority(100);
                mongoStorageHandler.enableEventUpdates();
                warpGroups.handlers().register(mongoStorageHandler);
            }
        }
        serverData.loadAsync();
        warpData.loadAsync();
        attributeData.loadAsync();
        warpGroups.loadAsync();
        ProxyServer.getInstance().getScheduler().schedule(Sledgehammer.getInstance(), () -> {
            serverData.saveAsync();
            warpData.saveAsync();
            attributeData.saveAsync();
            warpGroups.saveAsync();
        }, 5, TimeUnit.SECONDS);
    }

    /**
     * Migrates the data from local storage to databases
     */
    public void migrate() {
        ServerHandler.getInstance().getServers().migrate().migrate(0);
        WarpHandler.getInstance().getWarpGroups().migrate().migrate(0);
        WarpHandler.getInstance().getWarps().migrate().migrate(0);
        PlayerHandler.getInstance().getAttributes().migrate().migrate(0);
    }
}
