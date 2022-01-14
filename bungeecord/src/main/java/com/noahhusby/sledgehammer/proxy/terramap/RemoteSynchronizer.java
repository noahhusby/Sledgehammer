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
import com.noahhusby.sledgehammer.proxy.players.PlayerHandler;
import com.noahhusby.sledgehammer.proxy.players.SledgehammerPlayer;
import com.noahhusby.sledgehammer.proxy.terramap.network.packets.mapsync.P2CPlayerSyncPacket;
import com.noahhusby.sledgehammer.proxy.terramap.network.packets.mapsync.P2CRegistrationExpiresPacket;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Handles sending player position updates to registered players
 *
 * @author SmylerMC
 */
public class RemoteSynchronizer {

    private final Map<UUID, RegisteredForUpdatePlayer> playersToUpdate = new HashMap<>();

    /**
     * Sends position updates to registered players
     */
    public void syncPlayers() {
        synchronized (this.playersToUpdate) {
            if (this.playersToUpdate.size() <= 0) {
                return;
            }
            long ctime = System.currentTimeMillis();
            List<SledgehammerPlayer> playersToSend = new ArrayList<>();
            List<SledgehammerPlayer> players = new ArrayList<>(PlayerHandler.getInstance().getPlayers().values());
            for (SledgehammerPlayer player : players) {
                if (!player.onEarthServer()) {
                    continue;
                }
                if (!PlayerDisplayPreferences.shouldDisplayPlayer(player)) {
                    continue;
                }
                if (player.getLocation() == null) {
                    continue; // New players can have this set to null
                }
                playersToSend.add(player);
            }
            SledgehammerPlayer[] players2send2 = new SledgehammerPlayer[this.playersToUpdate.size()];
            int i = 0;
            for (RegisteredForUpdatePlayer player : this.playersToUpdate.values()) {
                players2send2[i++] = player.player;
            }
            P2CPlayerSyncPacket pkt = new P2CPlayerSyncPacket(playersToSend.toArray(new SledgehammerPlayer[0]));
            TerramapModule.instance.mapSyncChannel.send(pkt, players2send2);
            for (RegisteredForUpdatePlayer player : this.playersToUpdate.values()) {
                if (ctime - player.lastRegisterTime > SledgehammerConfig.terramap.terramapSyncTimeout - 10000 && !player.noticeSent) {
                    Sledgehammer.logger.fine("Sending registration expires notice to " + player.player.getName());
                    TerramapModule.instance.mapSyncChannel.send(new P2CRegistrationExpiresPacket(), player.player);
                    player.noticeSent = true;
                }
            }
            for (RegisteredForUpdatePlayer player : this.playersToUpdate.values()) {
                if (ctime - player.lastRegisterTime > SledgehammerConfig.terramap.terramapSyncTimeout) {
                    Sledgehammer.logger.fine("Unregistering " + player.player.getName() + " from map update as it did not renew its registration");
                    this.playersToUpdate.remove(player.player.getUniqueId());
                    TerramapModule.instance.mapSyncChannel.send(new P2CRegistrationExpiresPacket(), player.player);
                }
            }
        }
    }

    /**
     * Registers a players so it gets position updates
     *
     * @param player - The player
     */
    public void registerPlayer(SledgehammerPlayer player) {
        Sledgehammer.logger.fine("Registering player for map updates: " + player.getName());
        synchronized (this.playersToUpdate) {
            TerramapModule.instance.synchronizer.playersToUpdate.put(player.getUniqueId(), new RegisteredForUpdatePlayer(player, System.currentTimeMillis()));
        }
    }

    /**
     * Unregisters a player so it stops getting position updates
     *
     * @param player - The player
     */
    public void unregisterPlayer(ProxiedPlayer player) {
        Sledgehammer.logger.fine("Unregistering player for map updates: " + player.getName());
        synchronized (this.playersToUpdate) {
            TerramapModule.instance.synchronizer.playersToUpdate.remove(player.getUniqueId());
        }
    }

    /**
     * Unregisters all players so they stop getting map updates
     */
    public void unregisterAllPlayers() {
        synchronized (this.playersToUpdate) {
            this.playersToUpdate.clear();
        }
    }

    public static boolean hasSyncPermission(ProxiedPlayer player) {
        return player.hasPermission(TerramapModule.PLAYER_SYNC_PERMISSION_NODE) || PlayerHandler.getInstance().isAdmin(player);
    }

    private static class RegisteredForUpdatePlayer {

        public SledgehammerPlayer player;
        public long lastRegisterTime;
        boolean noticeSent = false;

        public RegisteredForUpdatePlayer(SledgehammerPlayer player, long time) {
            this.player = player;
            this.lastRegisterTime = time;
        }

    }

}
