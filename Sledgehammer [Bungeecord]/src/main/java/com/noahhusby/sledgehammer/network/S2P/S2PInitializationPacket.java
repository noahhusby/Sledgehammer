package com.noahhusby.sledgehammer.network.S2P;

import com.noahhusby.sledgehammer.Constants;
import com.noahhusby.sledgehammer.Sledgehammer;
import com.noahhusby.sledgehammer.SmartObject;
import com.noahhusby.sledgehammer.config.ServerConfig;
import com.noahhusby.sledgehammer.network.PacketInfo;
import com.noahhusby.sledgehammer.network.S2PPacket;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;

public class S2PInitializationPacket extends S2PPacket {
    @Override
    public String getPacketID() {
        return Constants.initID;
    }

    @Override
    public void onMessage(PacketInfo info, SmartObject data) {
        ServerInfo server = ProxyServer.getInstance().getServerInfo(info.getServer());

        Sledgehammer.logger.info(Constants.logInitPacket + server.getName());
        ServerConfig.getInstance().initializeServer(server, data.toJSON());
    }
}
