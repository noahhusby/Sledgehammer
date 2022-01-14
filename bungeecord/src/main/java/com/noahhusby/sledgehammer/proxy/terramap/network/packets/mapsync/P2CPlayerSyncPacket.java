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

package com.noahhusby.sledgehammer.proxy.terramap.network.packets.mapsync;

import com.noahhusby.sledgehammer.proxy.SledgehammerUtil;
import com.noahhusby.sledgehammer.proxy.config.SledgehammerConfig;
import com.noahhusby.sledgehammer.proxy.players.SledgehammerPlayer;
import com.noahhusby.sledgehammer.proxy.terramap.network.NetworkUtil;
import fr.thesmyler.bungee2forge.api.ForgePacket;
import io.netty.buffer.ByteBuf;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.chat.ComponentSerializer;

/**
 * Sent at regular intervals to players that registered with a {@link C2PRegisterForUpdatePacket}.
 * Contains the display name (JSON formatted), uuid, longitude, latitude, Azimuth and gamemode.
 * The interval at which packets are sent can be configured in {@link SledgehammerConfig.TerramapOptions#terramapSyncInterval}
 *
 * @author SmylerMC
 * @see com.noahhusby.sledgehammer.proxy.terramap.RemoteSynchronizer
 */
public class P2CPlayerSyncPacket implements ForgePacket {

    public final SledgehammerPlayer[] players;

    public P2CPlayerSyncPacket(SledgehammerPlayer[] players) {
        this.players = players;
    }

    public P2CPlayerSyncPacket() {
        this.players = new SledgehammerPlayer[0];
    }

    @Override
    public void encode(ByteBuf buf) {
        buf.writeInt(this.players.length);
        for (SledgehammerPlayer player : this.players) {
            double playerX = player.getLocation().getX();
            double playerZ = player.getLocation().getZ();
            double[] coordinates = SledgehammerUtil.toGeo(playerX, playerZ);

            // This only works because the BTE projection is conformal
            double[] northVec = SledgehammerUtil.getBTEProjection().vector(playerX, playerZ, 0.0001, 0);
            float north = (float) Math.toDegrees(Math.atan2(northVec[0], northVec[1]));
            float yaw = (float) player.getLocation().getYaw();
            float azimuth = north + yaw;
            if (azimuth < 0) {
                azimuth += 360;
            }

            buf.writeLong(player.getUniqueId().getLeastSignificantBits());
            buf.writeLong(player.getUniqueId().getMostSignificantBits());
            String playerDisplayName = ComponentSerializer.toString(TextComponent.fromLegacyText(player.getDisplayName()));
            NetworkUtil.writeStringToBuf(playerDisplayName, buf);
            buf.writeDouble(coordinates[0]);
            buf.writeDouble(coordinates[1]);
            buf.writeFloat(azimuth);
            NetworkUtil.writeStringToBuf(player.getGameMode().toString(), buf);
        }

    }

    @Override
    public void decode(ByteBuf buf) {
        // We will never receive this here
    }

    @Override
    public boolean processFromServer(String channel, Server fromServer, ProxiedPlayer toPlayer) {
        // We will never receive this here
        return SledgehammerConfig.terramap.terramapSyncPlayers;
    }

    @Override
    public boolean processFromClient(String channel, ProxiedPlayer fromPlayer, Server toServer) {
        // We will never receive this here
        return SledgehammerConfig.terramap.terramapSyncPlayers;
    }

}
