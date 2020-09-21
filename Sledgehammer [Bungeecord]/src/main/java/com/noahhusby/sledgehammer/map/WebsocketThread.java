package com.noahhusby.sledgehammer.map;

import com.noahhusby.sledgehammer.Sledgehammer;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class WebsocketThread {

    private MapHandler mapHandler = MapHandler.getInstance();

    public void t() {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        CheckInit i = new CheckInit();
        executor.scheduleAtFixedRate(i, 0, 30, TimeUnit.SECONDS);
    }


    private class CheckInit implements Runnable {

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
}

