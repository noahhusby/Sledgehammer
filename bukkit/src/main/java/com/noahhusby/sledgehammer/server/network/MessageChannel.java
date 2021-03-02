package com.noahhusby.sledgehammer.server.network;

import com.google.common.collect.Lists;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import com.noahhusby.sledgehammer.server.SledgehammerUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author Noah Husby
 */
public class MessageChannel implements PluginMessageListener {
    private final JavaPlugin plugin;
    private final String channel;

    public MessageChannel(JavaPlugin plugin, String channel) {
        this.plugin = plugin;
        this.channel = channel;
        Bukkit.getMessenger().registerIncomingPluginChannel(plugin, channel, this);
        Bukkit.getMessenger().registerOutgoingPluginChannel(plugin, channel);
    }

    private final List<Consumer<String>> messageReceivers = Lists.newArrayList();

    public void send(Player player, String message) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
            out.writeUTF(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
        player.sendPluginMessage(plugin, channel, stream.toByteArray());
    }

    public void onMessage(Consumer<String> consumer) {
        this.messageReceivers.add(consumer);
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (!this.channel.equalsIgnoreCase( channel)) {
            return;
        }
        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        String m = in.readUTF();
        messageReceivers.forEach(c -> c.accept(m));
    }
}
