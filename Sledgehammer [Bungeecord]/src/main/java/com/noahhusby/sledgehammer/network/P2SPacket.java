package com.noahhusby.sledgehammer.network;

public abstract class P2SPacket implements IP2SPacket {
    public SledgehammerNetworkManager getManager() {
        return SledgehammerNetworkManager.getInstance();
    }
}
