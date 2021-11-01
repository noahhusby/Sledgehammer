/*
 *  Copyright (c) 2021 Noah Husby
 *  Sledgehammer - Sledgehammerjava
 *
 *  Sledgehammer is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Sledgehammer is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Sledgehammer.  If not, see <https://github.com/noahhusby/Sledgehammer/blob/master/LICENSE/>.
 */

package com.noahhusby.sledgehammer.proxy.config;

import com.noahhusby.lib.application.config.Config;
import com.noahhusby.lib.application.config.Config.Comment;
import com.noahhusby.lib.application.config.Config.Type;

import java.util.UUID;

/**
 * @author Noah Husby
 */
@Config(name = "sledgehammer", type = Type.HOCON)
public class SledgehammerConfig {

    @Comment({
            "General options for sledgehammer"
    })
    public static GeneralOptions general = new GeneralOptions();

    public static class GeneralOptions {
        @Comment({
                "Generate a new key using https://uuidgenerator.net/version4",
                "All corresponding sledgehammer clients must have the same code",
                "Don't share this key with anyone you don't trust as it will allow anybody to run any command on connected servers." })
        public String authenticationCode = UUID.randomUUID().toString();

        @Comment({
                "The Id of the proxy. The first proxy starts at zero. Leave at zero if this is a single-proxy configuration"
        })
        public int proxyId = 0;
        @Comment({
                "The total amount of proxies, if a multi-server setup. Zero includes first proxy.",
                "Set this to -1 if this is a single-proxy configuration"
        })
        public int proxyTotal = -1;
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
            "Settings for the MySQL Database"
    })
    public static DatabaseOptions database = new DatabaseOptions();

    public static class DatabaseOptions {
        @Comment({
                "Should SQL be used to synchronize/store data?"
        })
        public boolean useSql = false;

        @Comment({
                "The host IP for the database."
        })
        public String sqlHost = "127.0.0.1";

        @Comment({
                "The port for the database."
        })
        public int sqlPort = 3306;

        @Comment({
                "The username for the database."
        })
        public String sqlUser = "";

        @Comment({
                "The password for the database."
        })
        public String sqlPassword = "";

        @Comment({
                "The name of the database."
        })
        public String sqlDb = "";

        @Comment({
                "How often SH should automatically refresh storage data (in seconds)."
        })
        public long autoLoad = 30;
    }

    @Comment({
            "Options for warps"
    })
    public static WarpOptions warps = new WarpOptions();

    public static class WarpOptions {
        @Comment({
                "The command for network-wide warping. Leave blank to disable"
        })
        public String warpCommand = "nwarp";

        @Comment({
                "Local warp allows for each individual server to set their own warps.",
                "Standard warp [false] is a shared pool of warps for the entire network.",
                "All warps are accessible regardless of warp mode."
        })
        public boolean localWarp = false;

        @Comment({
                "This will open the interactive GUI by default when the warp command is called.",
                "Otherwise `/[warp command] menu` is required to open the menu."
        })
        public boolean warpMenuDefault = false;

        @Comment({
                "This is the main page that will be shown upon the warp menu opening.",
                "Use `local` to show the warps on the current server (or groups of servers)",
                "Use `all` to show all the warps by default",
                "Use `servers` to show all the warps sorted by server",
                "Use `groups` to show all the warps sorted by group",
                "If local is used, but no local warps are found, the GUI will default to showing all warps."
        })
        public String warpMenuPage = "";

        public boolean showServersOption = true;

        public boolean showGroupsOption = true;
    }

    @Comment({
            "Options for OpenStreetMaps and Teleportation"
    })
    public static GeographyOptions geography = new GeographyOptions();

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
                "If player sync is enabled, should players be displayed by default (true) or should they explicitly opt-in with /terrashow (false)"
        })
        public boolean terramapPlayersDisplayDefault = true;

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
}