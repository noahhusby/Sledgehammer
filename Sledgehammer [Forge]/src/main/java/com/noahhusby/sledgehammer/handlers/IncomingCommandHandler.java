package com.noahhusby.sledgehammer.handlers;

import com.noahhusby.sledgehammer.ConfigHandler;
import com.noahhusby.sledgehammer.Sledgehammer;
import com.noahhusby.sledgehammer.communication.CommunicationMessage;
import com.noahhusby.sledgehammer.communication.RequestMessage;
import com.noahhusby.sledgehammer.communication.SimpleChannelManager;
import com.noahhusby.sledgehammer.tasks.CommandTask;
import com.noahhusby.sledgehammer.tasks.TeleportTask;
import com.noahhusby.sledgehammer.tasks.TpllTask;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.util.List;

public class IncomingCommandHandler {

    private static IncomingCommandHandler mInstance = null;

    public static IncomingCommandHandler getInstance() {
        if(mInstance == null) mInstance = new IncomingCommandHandler();
        return mInstance;
    }

    public void incomingCommand(CommunicationMessage message) {
        String[] data = message.text.split(",");
        if(data.length < 2 || !isGenuineRequest(data[1])) {
            return;
        }

        switch (data[0]) {
            case "command":
                try {
                    TaskQueueManager.getInstance().queueTask(new CommandTask(data[3], Long.parseLong(data[2]),10000, data));
                } catch (Exception e) {
                    Sledgehammer.logger.error("There was an error executing the command!");
                }
                break;
            case "location":
                try {
                    TaskQueueManager.getInstance().queueTask(new TpllTask(data[3], Long.parseLong(data[2]),10000, data[4], data[5]));
                } catch (Exception e) {
                    Sledgehammer.logger.error("There was an error executing the location command!");
                }
                break;
            case "title":
                break;
            case "announce":
                break;
            case "teleport":
                try {
                    TaskQueueManager.getInstance().queueTask(new TeleportTask(data[3], Long.parseLong(data[2]),10000, data[4], data[5], data[6]));
                } catch (Exception e) {
                    Sledgehammer.logger.error("There was an error executing the teleport command!");
                }
                break;
            case "request":
                request(message);
                break;
            default:
                Sledgehammer.logger.error("The sledgehammer communication client received an unknown command!");
                break;
        }
        return;
    }

    private void request(CommunicationMessage message) {
        String[] args = message.text.split(",");
        if(args.length < 4) return;
        switch(args[4]) {
            case "WARP_LOC":
                List<EntityPlayerMP> players = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers();
                if(players.isEmpty()) {
                    return;
                }

                for(EntityPlayerMP p : players) {
                    if(p.getName().equals(args[3])) {
                        SimpleChannelManager.network.sendTo(new RequestMessage(String.format("%s,%s,%s,%s,%s,%s,%s,%s",
                                "response", ConfigHandler.authenticationCode, System.currentTimeMillis(), args[3], "WARP_LOC",
                                p.getPosition().getX(), p.getPosition().getY(), p.getPosition().getZ())), p);
                        return;
                    }
                }
                break;
        }

    }

    private boolean isGenuineRequest(String u) {
        try {
            return u.equals(ConfigHandler.authenticationCode);
        } catch (Exception e) {
            Sledgehammer.logger.info("Error occurred while parsing incoming authentication command!");
            return false;
        }
    }
}
