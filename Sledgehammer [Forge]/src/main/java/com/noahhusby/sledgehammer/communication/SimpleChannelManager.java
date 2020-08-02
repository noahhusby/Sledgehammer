package com.noahhusby.sledgehammer.communication;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class SimpleChannelManager {
    public static SimpleNetworkWrapper network;
    public static void init() {
        network = NetworkRegistry.INSTANCE.newSimpleChannel("sledgehammer:channel");
        network.registerMessage(CommunicationMessage.Handler.class, CommunicationMessage.class, 0, Side.SERVER);
        network.registerMessage(RequestMessage.Handler.class, RequestMessage.class, 1, Side.CLIENT);

    }
}

