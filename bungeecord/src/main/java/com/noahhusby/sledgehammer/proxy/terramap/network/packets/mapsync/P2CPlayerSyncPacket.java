package com.noahhusby.sledgehammer.proxy.terramap.network.packets.mapsync;

import com.noahhusby.sledgehammer.proxy.SledgehammerUtil;
import com.noahhusby.sledgehammer.proxy.terramap.network.ForgeChannel;
import com.noahhusby.sledgehammer.proxy.terramap.network.packets.IForgePacket;
import com.noahhusby.sledgehammer.proxy.config.ConfigHandler;
import com.noahhusby.sledgehammer.proxy.players.SledgehammerPlayer;
import io.netty.buffer.ByteBuf;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.chat.ComponentSerializer;

/**
 * Sent at regular intervals to players that registered with a {@link C2PRegisterForUpdatePacket}.
 * Contains the display name (JSON formatted), uuid, longitude, latitude, Azimuth and gamemode.
 * The interval at which packets are sent can be configured in {@link ConfigHandler#terramapSyncInterval}
 *
 * @author SmylerMC
 * @see com.noahhusby.sledgehammer.proxy.addons.terramap.RemoteSynchronizer
 */
public class P2CPlayerSyncPacket implements IForgePacket {

    public SledgehammerPlayer[] players;

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
            ForgeChannel.writeStringToBuf(playerDisplayName, buf);
            buf.writeDouble(coordinates[0]);
            buf.writeDouble(coordinates[1]);
            buf.writeFloat(azimuth);
            ForgeChannel.writeStringToBuf(player.getGameMode().toString(), buf);
        }

    }

    @Override
    public void decode(ByteBuf buf) {
        // We will never receive this here
    }

    @Override
    public boolean processFromServer(String channel, Server fromServer, ProxiedPlayer toPlayer) {
        // We will never receive this here
        return ConfigHandler.terramapSyncPlayers;
    }

    @Override
    public boolean processFromClient(String channel, ProxiedPlayer fromPlayer, Server toServer) {
        // We will never receive this here
        return ConfigHandler.terramapSyncPlayers;
    }

}
