/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - MapThread.java
 *
 * Sledgehammer is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Sledgehammer is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Sledgehammer.  If not, see <https://github.com/noahhusby/Sledgehammer/blob/master/LICENSE/>.
 *
 */

package com.noahhusby.sledgehammer.maps;

import com.noahhusby.sledgehammer.Sledgehammer;
import com.noahhusby.sledgehammer.maps.MapHandler;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MapThread implements Runnable {

    private MapHandler mapHandler = MapHandler.getInstance();

    boolean alreadyCheckedHeartbeat = false;

    @Override
    public void run() {
        try {
            if(!mapHandler.isMapInitalized()) {
                mapHandler.attemptInit();
                return;
            }

            if(mapHandler.getHeartBeatState() || mapHandler.ws.userSession == null) {
                mapHandler.setInitState(false);
                if(!alreadyCheckedHeartbeat) {
                    Sledgehammer.logger.warning("Lost connection with the map websocket! Attempting re-connection.");
                    alreadyCheckedHeartbeat = true;
                }
                return;
            } else {
                if(alreadyCheckedHeartbeat) {
                    alreadyCheckedHeartbeat = false;
                    Sledgehammer.logger.warning("Reconnected with the map websocket!");
                }
            }
            mapHandler.attemptHeartbeat();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

