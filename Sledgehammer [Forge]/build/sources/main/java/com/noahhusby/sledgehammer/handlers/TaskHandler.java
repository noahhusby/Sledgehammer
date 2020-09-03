package com.noahhusby.sledgehammer.handlers;

import com.noahhusby.sledgehammer.ConfigHandler;
import com.noahhusby.sledgehammer.Reference;
import com.noahhusby.sledgehammer.Sledgehammer;
import com.noahhusby.sledgehammer.communication.CommunicationMessage;
import com.noahhusby.sledgehammer.communication.RequestMessage;
import com.noahhusby.sledgehammer.communication.SimpleChannelManager;
import com.noahhusby.sledgehammer.tasks.*;
import com.noahhusby.sledgehammer.tasks.data.TransferPacket;
import gnu.trove.impl.sync.TSynchronizedShortByteMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.FMLCommonHandler;
import org.lwjgl.Sys;

import java.util.ArrayList;
import java.util.List;

public class TaskHandler {
    private static TaskHandler mInstance = null;

    public static TaskHandler getInstance() {
        if(mInstance == null) mInstance = new TaskHandler();

        return mInstance;
    }

    private TaskHandler() {
        registerTask(new CommandTask(null, null));
        registerTask(new LocationTask(null, null));
        registerTask(new SetWarpTask(null, null));
        registerTask(new TeleportTask(null, null));
    }

    List<Task> tasks = new ArrayList<>();
    List<Task> queuedTasks = new ArrayList<>();

    private void registerTask(Task t) {
        tasks.add(t);
    }

    public void newMessage(CommunicationMessage message) {
        String[] args = message.text.split(",");
        if(args.length < 2 || !isGenuineRequest(args[1])) {
            return;
        }

        TransferPacket t = new TransferPacket(args[2], args[3]);
        List<String> dataList = new ArrayList<>();
        for(int x = 4; x < args.length; x++) {
            dataList.add(args[x]);
        }

        String[] data = dataList.toArray(new String[dataList.size()]);

        boolean success = false;
        for(Task task : tasks) {
            if(task.getCommandName().equals(args[0])) {
                task.build(t, data);
                success = true;
            }
        }

        if(!success) Sledgehammer.logger.error("The sledgehammer communication client received an unknown command!");
    }

    public void queueTask(Task t) {
        if(System.currentTimeMillis() > t.getMaxTime()) return;
        discardExpiredTasks();

        if(isServerSender(t)) {
            return;
        }

        List<EntityPlayerMP> players = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers();
        if(players.isEmpty()) {
            return;
        }

        for(EntityPlayerMP p : players) {
            if(p.getName().equals(t.getTransferPacket().sender) && System.currentTimeMillis() < t.getMaxTime()) {
                t.execute();
                return;
            }
        }

        queuedTasks.add(t);
    }

    public void sendResponse(Task t) {
        if(t.getResponse() == null) {
            Sledgehammer.logger.warn("A response was called for a task with no response!");
            return;
        } else {
            List<EntityPlayerMP> players = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers();
            if(players.isEmpty()) {
                return;
            }

            for(EntityPlayerMP p : players) {
                if(p.getName().equals(t.getTransferPacket().sender)) {
                    SimpleChannelManager.network.sendTo(new RequestMessage(String.format("%s,%s,%s,%s%s",
                            Reference.responsePrefix+t.getResponse().getResponseCommand(), ConfigHandler.authenticationCode, System.currentTimeMillis(),
                            t.getTransferPacket().sender, getResponseFromArray(t.getResponse().response()))), p);
                    return;
                }
            }
        }
    }


    public void playerJoined(EntityPlayer p) {
        discardExpiredTasks();
        if(queuedTasks.isEmpty()) return;

        for(Task t : queuedTasks) {
            if(t.getTransferPacket().sender.equals(p.getName()) && System.currentTimeMillis() < t.getMaxTime()) {
                t.execute();
                queuedTasks.remove(t);
                return;
            }
        }
    }

    private void discardExpiredTasks() {
        queuedTasks.removeIf(t -> System.currentTimeMillis() > t.getMaxTime());
    }

    private boolean isServerSender(Task t) {
        if(t.getTransferPacket().sender.equals("server") || t.getTransferPacket().sender.equals("*")) {
            return true;
        }
        return false;
    }

    private boolean isGenuineRequest(String u) {
        try {
            return u.equals(ConfigHandler.authenticationCode);
        } catch (Exception e) {
            Sledgehammer.logger.info("Error occurred while parsing incoming authentication command!");
            return false;
        }
    }

    private String getResponseFromArray(String[] data) {
        String z = "";
        for(int x = 0; x < data.length; x++) {
            z += ","+data[x];
        }

        return z;
    }
}
