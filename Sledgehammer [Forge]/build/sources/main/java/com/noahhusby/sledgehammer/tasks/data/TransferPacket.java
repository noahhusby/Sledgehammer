package com.noahhusby.sledgehammer.tasks.data;

import com.noahhusby.sledgehammer.Sledgehammer;

public class TransferPacket {
    public double time = 0;
    public final String sender;

    public TransferPacket(String time, String sender) {
        try {
            this.time = Double.parseDouble(time);
        } catch (Exception e) {
            Sledgehammer.logger.warn("An error occured while attempting to parse the transfer packet for: "+sender);
        }
        this.sender = sender;
    }
}
