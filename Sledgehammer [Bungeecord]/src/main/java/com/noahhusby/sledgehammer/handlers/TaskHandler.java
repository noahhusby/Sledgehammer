package com.noahhusby.sledgehammer.handlers;

import com.noahhusby.sledgehammer.config.ConfigHandler;
import com.noahhusby.sledgehammer.tasks.data.IResponse;
import com.noahhusby.sledgehammer.tasks.data.ITask;
import com.noahhusby.sledgehammer.tasks.data.TaskPacket;
import com.noahhusby.sledgehammer.tasks.data.TransferPacket;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.minecraftforge.common.config.Config;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TaskHandler {

    private static TaskHandler mInstance = null;

    public static TaskHandler getInstance() {
        if(mInstance == null) mInstance = new TaskHandler();
        return mInstance;
    }

    private List<IResponse> responses = new ArrayList<>();

    public void execute(ITask task) {
        TaskPacket p = task.build();
        String executionMessage = String.format("%s,%s,%s,%s", task.getCommandName(), ConfigHandler.authenticationCode
        , System.currentTimeMillis(), p.sender);
        for(int x = 0; x < p.data.length; x++) {
            executionMessage+=","+p.data[x];
        }
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
            out.writeUTF(executionMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
        p.server.sendData("sledgehammer:channel", stream.toByteArray());
        if(task.getResponse() != null) {
            responses.add(task.getResponse());
        }
    }

    public void onIncomingMessage(PluginMessageEvent e) {
        try {
            DataInputStream in = new DataInputStream(new ByteArrayInputStream(e.getData()));
            String incomingChannel = e.getTag();

            if (!incomingChannel.equalsIgnoreCase("sledgehammer:channel")) return;
            String[] args = getStringFromMessage(in).split(",");
            if(args.length < 3) return;
            if(!args[1].equals(ConfigHandler.authenticationCode)) return;

            ArrayList<String> dataList = new ArrayList<>();
            for(int x = 4; x < args.length; x++) dataList.add(args[x]);

            String[] data = dataList.toArray(new String[dataList.size()]);

            List<IResponse> removalList = new ArrayList<>();

            for(IResponse r : responses) {
                if(r.getResponseCommand().equals(args[0])) {
                    TransferPacket t = new TransferPacket(null, args[3]);
                    if(r.validateResponse(t, args)) {
                        r.respond(data);
                        removalList.add(r);
                    }
                }
            }

            for(IResponse r: removalList) responses.remove(r);
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

    private String getStringFromMessage(DataInputStream i) throws IOException {
        String x = "";
        while(i.available() != 0) {
            char c = (char) i.readByte();
            x += c;
        }
        return x.substring(x.indexOf("<")+1).trim();
    }
}
