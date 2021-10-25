package com.noahhusby.sledgehammer.proxy.terramap.network.packets.mapsync;

import com.noahhusby.sledgehammer.proxy.config.ConfigHandler;
import com.noahhusby.sledgehammer.proxy.players.PlayerHandler;
import com.noahhusby.sledgehammer.proxy.players.SledgehammerPlayer;
import com.noahhusby.sledgehammer.proxy.terramap.RemoteSynchronizer;
import com.noahhusby.sledgehammer.proxy.terramap.TerramapAddon;
import fr.thesmyler.bungee2forge.api.ForgePacket;
import io.netty.buffer.ByteBuf;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;

/**
 * Packet received from players that wish to start or stop receiving player position updates.
 *
 * @author SmylerMC
 * @see com.noahhusby.sledgehammer.proxy.terramap.RemoteSynchronizer
 */
public class C2PRegisterForUpdatePacket implements ForgePacket {

    public boolean register;

    @Override
    public void encode(ByteBuf buf) {
        buf.writeBoolean(this.register);
    }

    @Override
    public void decode(ByteBuf stream) {
        this.register = stream.readBoolean();
    }

    @Override
    public boolean processFromServer(String channel, Server fromServer, ProxiedPlayer toPlayer) {
        // Should never receive this from a server
        return ConfigHandler.terramapSyncPlayers;
    }

    @Override
    public boolean processFromClient(String channel, ProxiedPlayer fromPlayer, Server toServer) {
        if (!ConfigHandler.terramapSyncPlayers) {
            return false;
        }
        SledgehammerPlayer player = PlayerHandler.getInstance().getPlayer(fromPlayer.getName());
        if (this.register) {
            if (RemoteSynchronizer.hasSyncPermission(player)) {
                TerramapAddon.instance.synchronizer.registerPlayer(player);
            }
        } else {
            TerramapAddon.instance.synchronizer.unregisterPlayer(player);
        }
        return true; // Do not send to server
    }

}
