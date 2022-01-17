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

import com.noahhusby.lib.application.config.Config;
import com.noahhusby.lib.application.config.Config.Comment;
import com.noahhusby.lib.application.config.Config.Type;
import com.noahhusby.sledgehammer.proxy.Sledgehammer;

import java.util.Locale;
import java.util.UUID;

/**
 * @author Noah Husby
 */
@SuppressWarnings("CanBeFinal")
@Config(name = "sledgehammer", type = Type.HOCON)
public class SledgehammerConfig {

    @Comment({
            "General options for sledgehammer"
    })
    public static GeneralOptions general = new GeneralOptions();

    @SuppressWarnings("CanBeFinal")
    public static class GeneralOptions {
        @Comment({
                "The prefix of messages broadcast to players from the proxy."
        })
        public String messagePrefix = "&9&lBTE &8&l> ";
        @Comment({
                "Set this to false to disable global tpll. [/tpll & /cs tpll]"
        })
        public boolean globalTpll = true;
        @Comment({
                "When enabled, the default 'This command is not available!' is replaced with 'Unknown Command.'"
        })
        public boolean replaceNotAvailable = false;
    }

    @Comment({
            "Settings for the database"
    })
    public static DatabaseOptions database = new DatabaseOptions();

    @SuppressWarnings("CanBeFinal")
    public static class DatabaseOptions {
        @Comment({
                "Which database should be used?",
                "\"MONGO\" for MongoDB (Required for multi-proxy installations)",
                "\"SQL\" for SQL",
                "\"LOCAL\" for local storage only"
        })
        public String type = "LOCAL";

        @Comment({
                "The host IP for the database."
        })
        public String host = "127.0.0.1";

        @Comment({
                "The port for the database."
        })
        public int port = 3306;

        @Comment({
                "The username for the database."
        })
        public String user = "";

        @Comment({
                "The password for the database."
        })
        public String password = "";

        @Comment({
                "The name of the database."
        })
        public String database = "";
    }

    @Comment({
            "Options for warps"
    })
    public static WarpOptions warps = new WarpOptions();

    @SuppressWarnings("CanBeFinal")
    public static class WarpOptions {
        @Comment({
                "The command for network-wide warping. Leave blank to disable"
        })
        public String warpCommand = "nwarp";

        @Comment({
                "Local warp allows for each individual server (or group) to set their own warps.",
                "Standard warp [false] is a shared pool of warps for the entire network.",
                "All warps are accessible regardless of warp mode."
        })
        public boolean localWarp = false;

        @Comment({
                "This will open the interactive GUI by default when the warp command is called.",
                "Otherwise `/[warp command] menu` is required to open the menu."
        })
        public boolean guiMenuDefault = false;

        @Comment({
                "This is the main page that will be shown upon the warp menu opening.",
                "Use `local` to show the warps on the current server (or groups of servers)",
                "Use `all` to show all the warps by default",
                "Use `servers` to show all the warps sorted by server",
                "Use `groups` to show all the warps sorted by group",
                "If local is used, but no local warps are found, the GUI will default to showing all warps."
        })
        public String warpMenuPage = "";
    }

    @Comment({
            "Options for OpenStreetMaps and Teleportation"
    })
    public static GeographyOptions geography = new GeographyOptions();

    @SuppressWarnings("CanBeFinal")
    public static class GeographyOptions {
        @Comment({
                "Zoom detail level for OSM requests."
        })

        public int zoom = 12;
        @Comment({
                "Set false for fetching the latest data from OSM (more up to date), or true for using a downloaded database.",
                "Please follow the guide on https://github.com/noahhusby/Sledgehammer/wiki/Border-Offline-Database about downloading and configuring the offline database."
        })
        public boolean useOfflineMode = false;

        @Comment({
                "Set to false to disable automatic border teleportation, or true to enable it. Border teleportation requires the offline database to be configured."
        })
        public boolean borderTeleportation = false;
    }

    @Comment({
            "Terramap Addon Configuration"
    })
    public static TerramapOptions terramap = new TerramapOptions();

    @SuppressWarnings("CanBeFinal")
    public static class TerramapOptions {
        @Comment({
                "Enables Terramap integration"
        })
        public boolean terramapEnabled = true;

        @Comment({
                "Whether or not to synchronize players from server to client so everyone appears on the map, no matter the distance."
        })
        public boolean terramapSyncPlayers = true;

        @Comment({
                "The time interval, in milliseconds at which to synchronize players with clients."
        })
        public int terramapSyncInterval = 500;

        @Comment({
                "The default time interval, in milliseconds, before a clients needs to register again to continue getting player position updates."
        })
        public int terramapSyncTimeout = 120000;

        @Comment({
                "Set to false if you do not want to send custom maps to clients. This is only for testing, as if you don't want to send map styles to client, the first thing to do is to not configure any."
        })
        public boolean terramapSendCustomMapsToClient = true;

        @Comment({
                "Set this to false to only allow players to use the map when they are on an Earth world."
        })
        public boolean terramapGlobalMap = true;

        @Comment({
                "Set this to true is you want client's settings to be saved for the entire network instead of per-world."
        })
        public boolean terramapGlobalSettings = false;

        @Comment({
                "A UUID v4 that will be used by Terramap clients to identify this network when saving settings. DO NOT PUT YOUR NETWORK AUTHENTIFICATION CODE IN HERE, THIS IS SHARED WITH CLIENTS! You want this to be the same on all your network's proxies. The default value is randomly generated."
        })
        public String terramapProxyUUID = UUID.randomUUID().toString();
    }

    public static void validate() {
        // Validate Database Information
        String databaseType = database.type.toLowerCase(Locale.ROOT);
        if (!(databaseType.equals("mongo") || databaseType.equals("sql") || databaseType.equals("local"))) {
            Sledgehammer.logger.warning(validationError(databaseType, "database", "type", "Using local database instead."));
            database.type = "LOCAL";
        }
        if (geography.useOfflineMode && !ConfigHandler.getInstance().getOfflineBin().exists()) {
            Sledgehammer.logger.warning("Offline mode was enabled, but no location database exists! Please follow this guide to download the location database: https://github.com/noahhusby/Sledgehammer/wiki/Border-Offline-Database\n"
                                        + "Disabling offline mode!");
            geography.useOfflineMode = false;
        }
        if (geography.borderTeleportation && !geography.useOfflineMode) {
            Sledgehammer.logger.warning("Border teleportation was enabled, but offline mode is disabled. Please make sure the offline database is configured correctly, and offline mode is enabled.");
            geography.borderTeleportation = false;
        }
    }

    private static String validationError(String value, String cat, String field, String result) {
        return String.format("Invalid value \"%s\", for %s/%s. %s", value, cat, field, result);
    }
}
