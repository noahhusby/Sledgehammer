package com.noahhusby.sledgehammer.tasks.data;

import com.noahhusby.sledgehammer.Sledgehammer;

public class TransferPacket {
    public double time = 0;
    public final String sender;
    public final String server;

    public TransferPacket(String time, String sender, String server) {
        try {
            this.time = Double.parseDouble(time);
        } catch (Exception e) {
            Sledgehammer.logger.warning("An error occured while attempting to parse the transfer packet for: "+sender);
        }
        this.sender = sender;
        this.server = server;
    }
}
