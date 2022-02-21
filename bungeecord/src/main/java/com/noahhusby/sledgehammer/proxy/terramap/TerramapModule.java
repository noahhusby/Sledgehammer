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

package com.noahhusby.sledgehammer.proxy.terramap;

import com.noahhusby.sledgehammer.proxy.Sledgehammer;
import com.noahhusby.sledgehammer.proxy.config.SledgehammerConfig;
import com.noahhusby.sledgehammer.proxy.modules.Module;
import com.noahhusby.sledgehammer.proxy.terramap.TerramapVersion.ReleaseType;
import com.noahhusby.sledgehammer.proxy.terramap.commands.TerrashowCommand;
import com.noahhusby.sledgehammer.proxy.terramap.network.packets.P2CMapStylePacket;
import com.noahhusby.sledgehammer.proxy.terramap.network.packets.P2CSledgehammerHelloPacket;
import com.noahhusby.sledgehammer.proxy.terramap.network.packets.mapsync.C2PRegisterForUpdatePacket;
import com.noahhusby.sledgehammer.proxy.terramap.network.packets.mapsync.P2CPlayerSyncPacket;
import com.noahhusby.sledgehammer.proxy.terramap.network.packets.mapsync.P2CRegistrationExpiresPacket;
import fr.thesmyler.bungee2forge.BungeeToForgePlugin;
import fr.thesmyler.bungee2forge.api.ForgeChannel;
import fr.thesmyler.bungee2forge.api.ForgeChannelRegistry;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.scheduler.ScheduledTask;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Main Terramap module class
 *
 * @author SmylerMC
 */
public class TerramapModule implements Module, Listener {

    public static final String TERRAMAP_MODID = "terramap";
    public static final String MAPSYNC_CHANNEL_NAME = "terramap:mapsync";
    public static final String SLEDGEHAMMER_CHANNEL_NAME = "terramap:sh"; // Forge does not support channel names longer than 20
    public static final TerramapVersion MINIMUM_COMPATIBLE_VERSION = new TerramapVersion(1, 0, 0, ReleaseType.BETA, 6, 0);
    public static final TerramapVersion OLDEST_TERRA121_TERRAMAP_VERSION = new TerramapVersion(1, 0, 0, ReleaseType.BETA, 6, 7);
    public static final String PLAYER_SYNC_PERMISSION_NODE = "sledgehammer.terramap.playersync";
    public static final String TERRASHOW_BASE_PERMISSION_NODE = "sledgehammer.terramap.terrashow";
    public static final String TERRASHOW_SELF_PERMISSION_NODE = "sledgehammer.terramap.terrashow.self";
    public static final String TERRASHOW_OTHERS_PERMISSION_NODE = "sledgehammer.terramap.terrashow.other";
    public static TerramapModule instance;
    public final ForgeChannel mapSyncChannel = ForgeChannelRegistry.instance().get(MAPSYNC_CHANNEL_NAME);
    public final ForgeChannel sledgehammerChannel = ForgeChannelRegistry.instance().get(SLEDGEHAMMER_CHANNEL_NAME);

    public final RemoteSynchronizer synchronizer = new RemoteSynchronizer();

    private Listener listener;
    private ScheduledTask syncTask;

    private UUID proxyUUID = new UUID(0, 0);

    public TerramapModule() {
        instance = this;
    }

    @Override
    public void onEnable() {
        BungeeToForgePlugin.onEnable(Sledgehammer.getInstance());
        this.mapSyncChannel.registerPacket(0, C2PRegisterForUpdatePacket.class);
        this.mapSyncChannel.registerPacket(1, P2CPlayerSyncPacket.class);
        this.mapSyncChannel.registerPacket(2, P2CRegistrationExpiresPacket.class);
        this.sledgehammerChannel.registerPacket(0, P2CSledgehammerHelloPacket.class);
        this.sledgehammerChannel.registerPacket(2, P2CMapStylePacket.class);
        try {
            this.proxyUUID = UUID.fromString(SledgehammerConfig.terramap.terramapProxyUUID);
        } catch (IllegalArgumentException e) {
            Sledgehammer.logger.warning("Failed to parse Terramap proxy uuid. Will be using 0.");
        }
        this.listener = new TerramapModuleEventHandler();
        Sledgehammer.addListener(this.listener);
        if (SledgehammerConfig.terramap.terramapSyncPlayers) {
            this.syncTask = Sledgehammer.getInstance().getProxy().getScheduler().schedule(Sledgehammer.getInstance(), this.synchronizer::syncPlayers, 0, SledgehammerConfig.terramap.terramapSyncInterval, TimeUnit.MILLISECONDS);
        }
        ProxyServer.getInstance().getPluginManager().registerCommand(Sledgehammer.getInstance(), new TerrashowCommand());
    }

    @Override
    public void onDisable() {
        this.mapSyncChannel.deregisterAllPackets();
        this.sledgehammerChannel.deregisterAllPackets();
        this.proxyUUID = new UUID(0, 0);
        this.synchronizer.unregisterAllPlayers();
        Sledgehammer.removeListener(this.listener);
        if (this.syncTask != null) {
            Sledgehammer.getInstance().getProxy().getScheduler().cancel(this.syncTask);
        }
        BungeeToForgePlugin.onDisable(Sledgehammer.getInstance());
    }

    @Override
    public String getModuleName() {
        return "Terramap";
    }

    public UUID getProxyUUID() {
        return this.proxyUUID;
    }
}