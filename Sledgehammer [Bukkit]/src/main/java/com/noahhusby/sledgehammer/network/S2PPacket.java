package com.noahhusby.sledgehammer.network;

public abstract class S2PPacket implements IS2PPacket {
    public SledgehammerNetworkManager getManager() {
        return SledgehammerNetworkManager.getInstance();
    }
}
