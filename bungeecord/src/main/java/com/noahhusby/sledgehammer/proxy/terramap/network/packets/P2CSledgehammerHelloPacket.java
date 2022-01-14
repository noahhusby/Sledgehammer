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

package com.noahhusby.sledgehammer.proxy.terramap.network.packets;

import com.noahhusby.sledgehammer.proxy.config.SledgehammerConfig;
import com.noahhusby.sledgehammer.proxy.terramap.network.NetworkUtil;
import com.noahhusby.sledgehammer.proxy.terramap.network.packets.mapsync.PlayerSyncStatus;
import fr.thesmyler.bungee2forge.api.ForgePacket;
import io.netty.buffer.ByteBuf;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;

import java.util.UUID;

/**
 * Sent to clients joining the network to inform them of this proxy's specific settings, such as:
 * <ul>
 * <li>The Sledgehammer version</li>
 * <li>Whether or not players are being synchronized. See {@link SledgehammerConfig.TerramapOptions#terramapSyncPlayers}</li>
 * <li>Whether or not to enable the map on all worlds and not just on Terra121's. See {@link SledgehammerConfig.TerramapOptions#terramapGlobalMap}</li>
 * <li>Whether or not to save settings per world or for the whole network. See See {@link SledgehammerConfig.TerramapOptions#terramapGlobalSettings}</li>
 * <li>Whether or not warps are supported.</li>
 * <li>The proxy UUID. See {@link SledgehammerConfig.TerramapOptions#terramapProxyUUID}.</li>
 * </ul>
 *
 * @author SmylerMC
 */
public class P2CSledgehammerHelloPacket implements ForgePacket {

    public String version = "";
    public PlayerSyncStatus syncPlayers = PlayerSyncStatus.DISABLED;
    public PlayerSyncStatus syncSpectators = PlayerSyncStatus.DISABLED;
    public boolean globalmap = true; // If true, the Terramap allows users to open the map on non-terra worlds
    public boolean globalSettings = false; // Should settings and preferences be saved for the whole network (true) or per server (false)
    public boolean hasWarpSupport = false; // Do this server have warp support
    public UUID proxyUUID = new UUID(0, 0);

    public P2CSledgehammerHelloPacket(String version, PlayerSyncStatus syncPlayers, PlayerSyncStatus syncSpectators, boolean globalMap, boolean globalSettings, boolean hasWarpSupport, UUID proxyUUID) {
        this.version = version;
        this.syncPlayers = syncPlayers;
        this.syncSpectators = syncSpectators;
        this.globalmap = globalMap;
        this.globalSettings = globalSettings;
        this.hasWarpSupport = hasWarpSupport;
        this.proxyUUID = proxyUUID;
    }

    public P2CSledgehammerHelloPacket() {
    }

    @Override
    public void encode(ByteBuf buf) {
        NetworkUtil.writeStringToBuf(this.version, buf);
        buf.writeByte(this.syncPlayers.VALUE);
        buf.writeByte(this.syncSpectators.VALUE);
        buf.writeBoolean(this.globalmap);
        buf.writeBoolean(this.globalSettings);
        buf.writeBoolean(this.hasWarpSupport);
        buf.writeLong(this.proxyUUID.getLeastSignificantBits());
        buf.writeLong(this.proxyUUID.getMostSignificantBits());
    }

    @Override
    public void decode(ByteBuf buf) {
        this.version = NetworkUtil.readStringFromBuf(buf);
        this.syncPlayers = PlayerSyncStatus.getFromNetworkCode(buf.readByte());
        this.syncSpectators = PlayerSyncStatus.getFromNetworkCode(buf.readByte());
        this.globalmap = buf.readBoolean();
        this.globalSettings = buf.readBoolean();
        this.hasWarpSupport = buf.readBoolean();
        long leastUUID = buf.readLong();
        long mostUUID = buf.readLong();
        this.proxyUUID = new UUID(mostUUID, leastUUID);
    }

    @Override
    public boolean processFromServer(String channel, Server fromServer, ProxiedPlayer toPlayer) {
        return false; // We should never receive that, only send
    }

    @Override
    public boolean processFromClient(String channel, ProxiedPlayer fromPlayer, Server toServer) {
        return false; // We should never receive that, only send
    }

}
