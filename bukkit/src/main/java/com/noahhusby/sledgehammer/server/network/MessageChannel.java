/*
 * MIT License
 *
 * Copyright 2020-2022 noahhusby
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy,
 * modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software
 * is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS
 * BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

package com.noahhusby.sledgehammer.server.network;

import com.google.common.collect.Lists;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
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
    private final List<Consumer<String>> messageReceivers = Lists.newArrayList();

    public MessageChannel(JavaPlugin plugin, String channel) {
        this.plugin = plugin;
        this.channel = channel;
        Bukkit.getMessenger().registerIncomingPluginChannel(plugin, channel, this);
        Bukkit.getMessenger().registerOutgoingPluginChannel(plugin, channel);
    }

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

    @SuppressWarnings("UnstableApiUsage")
    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (!this.channel.equalsIgnoreCase(channel)) {
            return;
        }
        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        String m = in.readUTF();
        messageReceivers.forEach(c -> c.accept(m));
    }
}
