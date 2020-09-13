package com.noahhusby.sledgehammer.communication;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import com.noahhusby.sledgehammer.Sledgehammer;
import com.noahhusby.sledgehammer.handlers.TaskHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class CommunicationManager implements PluginMessageListener {

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (!channel.equalsIgnoreCase( "sledgehammer:channel")) {
            return;
        }
        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        String m = in.readUTF();
        TaskHandler.getInstance().newMessage(m);
    }

    public static void sendMessage(String message) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
            out.writeUTF(message);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Player[] players = Bukkit.getOnlinePlayers().toArray(new Player[0]);
        players[0].sendPluginMessage(Sledgehammer.sledgehammer, "sledgehammer:channel", stream.toByteArray());
    }
}

