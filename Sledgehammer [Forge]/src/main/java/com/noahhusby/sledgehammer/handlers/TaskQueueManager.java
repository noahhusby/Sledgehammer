package com.noahhusby.sledgehammer.handlers;

import com.noahhusby.sledgehammer.tasks.Task;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.util.ArrayList;
import java.util.List;

public class TaskQueueManager {
    private static TaskQueueManager mInstance = null;

    public static TaskQueueManager getInstance() {
        if(mInstance == null) mInstance = new TaskQueueManager();

        return mInstance;
    }

    private TaskQueueManager() { }

    List<Task> tasks = new ArrayList<>();

    public void queueTask(Task t) {
        if(System.currentTimeMillis() > t.maxCheckTime) return;
        discardExpiredTasks();

        if(isServerSender(t)) {
            return;
        }

        List<EntityPlayerMP> players = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers();
        if(players.isEmpty()) {
            return;
        }

        for(EntityPlayerMP p : players) {
            if(p.getName().equals(t.sender) || System.currentTimeMillis() < t.maxCheckTime) {
                t.execute();
                return;
            }
        }

        tasks.add(t);
    }

    public void playerJoined(EntityPlayer p) {
        discardExpiredTasks();
        if(tasks.isEmpty()) return;

        for(Task t : tasks) {
            if(t.sender.equals(p.getName()) || System.currentTimeMillis() < t.maxCheckTime) {
                t.execute();
                tasks.remove(t);
            }
        }
    }

    private void discardExpiredTasks() {
        tasks.removeIf(t -> System.currentTimeMillis() > t.maxCheckTime);
    }

    private boolean isServerSender(Task t) {
        if(t.sender.equals("server") || t.sender.equals("*")) {
            return true;
        }
        return false;
    }
}
