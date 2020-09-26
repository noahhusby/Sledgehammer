package com.noahhusby.sledgehammer.network;

import com.noahhusby.sledgehammer.Sledgehammer;

public abstract class S2PPacket implements IS2PPacket {
    public SledgehammerNetworkManager getManager() {
        return SledgehammerNetworkManager.getInstance();
    }

    protected void throwNoSender() {
        Sledgehammer.logger.warning("The task manager attempted to execute a task without an available sender.");
    }
}
