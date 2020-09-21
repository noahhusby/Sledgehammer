package com.noahhusby.sledgehammer.handlers;

import com.noahhusby.sledgehammer.ConfigHandler;
import com.noahhusby.sledgehammer.Constants;
import com.noahhusby.sledgehammer.Sledgehammer;
import com.noahhusby.sledgehammer.communication.CommunicationManager;
import com.noahhusby.sledgehammer.tasks.*;
import com.noahhusby.sledgehammer.tasks.data.TransferPacket;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.Collection;
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
        registerTask(new TestLocationTask(null, null));
        registerTask(new InitializationPacket(null, null));
    }

    List<Task> tasks = new ArrayList<>();
    List<Task> queuedTasks = new ArrayList<>();

    private void registerTask(Task t) {
        tasks.add(t);
    }

    public void newMessage(String message) {
        try {
            JSONObject o = (JSONObject) new JSONParser().parse(message);
            if(!isGenuineRequest((String) o.get("uuid"))) return;
            TransferPacket t = new TransferPacket(String.valueOf((long) o.get("time")), (String) o.get("sender"), (String) o.get("server"));

            boolean success = false;
            for(Task task : tasks) {
                if(task.getCommandName().equals((String) o.get("command"))) {
                    task.build(t, o);
                    success = true;
                }
            }

            if(!success) Sledgehammer.logger.warning("The sledgehammer communication client received an unknown command!");
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    public void queueTask(Task t) {
        if(System.currentTimeMillis() > t.getMaxTime()) return;
        discardExpiredTasks();

        if(isServerSender(t)) {
            return;
        }

        Collection<? extends Player> players = Bukkit.getServer().getOnlinePlayers();
        if(players.isEmpty()) {
            return;
        }

        for(Player p : players) {
            if(p.getName().equals(t.getTransferPacket().sender) && System.currentTimeMillis() < t.getMaxTime()) {
                t.execute();
                return;
            }
        }

        queuedTasks.add(t);
    }

    public void sendResponse(Task t) {
        if(t.getResponse() == null) {
            Sledgehammer.logger.warning("A response was called for a task with no response!");
            return;
        } else {
            Player p = Bukkit.getPlayer(t.getTransferPacket().sender);
            if (p == null) return;
            if (!p.isOnline()) return;

            CommunicationManager.sendMessage(Constants.responsePrefix+t.getResponse().response().toJSONString());
        }

    }


    public void playerJoined(Player p) {
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
