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

import com.noahhusby.sledgehammer.proxy.config.SledgehammerConfig;
import com.noahhusby.sledgehammer.proxy.players.PlayerHandler;
import com.noahhusby.sledgehammer.proxy.players.SledgehammerPlayer;
import com.noahhusby.sledgehammer.proxy.terramap.RemoteSynchronizer;
import com.noahhusby.sledgehammer.proxy.terramap.TerramapModule;
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
        return SledgehammerConfig.terramap.terramapSyncPlayers;
    }

    @Override
    public boolean processFromClient(String channel, ProxiedPlayer fromPlayer, Server toServer) {
        if (!SledgehammerConfig.terramap.terramapSyncPlayers) {
            return false;
        }
        SledgehammerPlayer player = PlayerHandler.getInstance().getPlayer(fromPlayer.getName());
        if (this.register) {
            if (RemoteSynchronizer.hasSyncPermission(player)) {
                TerramapModule.instance.synchronizer.registerPlayer(player);
            }
        } else {
            TerramapModule.instance.synchronizer.unregisterPlayer(player);
        }
        return true; // Do not send to server
    }

}
