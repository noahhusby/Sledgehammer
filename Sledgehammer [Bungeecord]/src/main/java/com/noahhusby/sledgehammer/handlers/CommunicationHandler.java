package com.noahhusby.sledgehammer.handlers;

import com.noahhusby.sledgehammer.Sledgehammer;
import com.noahhusby.sledgehammer.util.Point;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.event.PluginMessageEvent;

import java.io.*;

public class CommunicationHandler {
    public static void executeRequest(ServerInfo server, String sender, String... args) {
        executeCommand(server, "request", sender, args);
    }

    public static void executeCommand(ServerInfo server, String command, String sender, String... args) {
        String executionMessage = String.format("%s,%s,%s,%s", command, Sledgehammer.configuration.getString("authentication-key")
        , System.currentTimeMillis(), sender);
        for(int x = 0; x < args.length; x++) {
            executionMessage+=","+args[x];
        }
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
            out.writeUTF(executionMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
        server.sendData("sledgehammer:channel", stream.toByteArray());
    }

    public static void onIncomingMessage(PluginMessageEvent e) {
        try {
            DataInputStream in = new DataInputStream(new ByteArrayInputStream(e.getData()));
            String incomingChannel = e.getTag();

            if (!incomingChannel.equalsIgnoreCase("sledgehammer:channel")) return;

            String[] args = in.readUTF().split(",");
            if(args.length < 3) return;
            if(!args[1].equals(Sledgehammer.configuration.getString("authentication-key"))) return;
            switch (args[4]) {
                case "WARP_LOC":
                    WarpHandler.getInstance().incomingLocationResponse(args[3], new Point(args[5].replaceAll("[^\\d-]", ""),
                            args[6].replaceAll("[^\\d-]", ""), args[7].replaceAll("[^\\d-]", "")));
                    break;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static String getRawArguments(String[] args) {
        if(args.length == 0) {
            return "";
        } else if(args.length == 1) {
            return args[0];
        }

        String arguments = args[0];

        for(int x = 1; x < args.length; x++) {
            arguments+=","+args[x];
        }

        return arguments;
    }
}
