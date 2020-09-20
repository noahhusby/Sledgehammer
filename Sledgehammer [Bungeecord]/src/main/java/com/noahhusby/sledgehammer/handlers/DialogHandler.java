package com.noahhusby.sledgehammer.handlers;

import com.google.common.collect.Maps;
import com.noahhusby.sledgehammer.Sledgehammer;
import com.noahhusby.sledgehammer.data.dialogs.scenes.IDialogScene;
import com.noahhusby.sledgehammer.util.ChatHelper;
import com.noahhusby.sledgehammer.util.TextElement;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.Map;

public class DialogHandler implements Listener {
    private static DialogHandler mInstance = null;

    public static DialogHandler getInstance() {
        if(mInstance == null) mInstance = new DialogHandler();
        return mInstance;
    }

    private DialogHandler() {
        Sledgehammer.setupListener(this);
    }

    private Map<CommandSender, IDialogScene> activeScenes = Maps.newHashMap();

    public void startDialog(CommandSender c, IDialogScene s) {
        activeScenes.put(c, s);
        s.init(c);
    }

    public void discardDialog(IDialogScene s) {
        activeScenes.remove(s.getCommandSender(), s);
    }

    public void progressDialog(IDialogScene s, boolean error) {
        for(int x = 0; x < 20; x++) {
            s.getCommandSender().sendMessage();
        }

        if(error) {
            s.getCommandSender().sendMessage(ChatHelper.getInstance().makeTextComponent(new TextElement("Invalid entry!", ChatColor.RED)));
            s.getCommandSender().sendMessage();
        }

        if(s.getTitle() != null) {
            s.getCommandSender().sendMessage(ChatHelper.getInstance().makeTextComponent(s.getTitle()));
        }

        if(s.getToolbar() != null) {
            Map<String, String> tools = s.getToolbar().getTools();
            for(Map.Entry<String, String> x : tools.entrySet()) {
                s.getCommandSender().sendMessage(ChatHelper.getInstance().makeTextComponent(new TextElement("Use ", ChatColor.GRAY),
                        new TextElement(x.getKey(), ChatColor.BLUE), new TextElement(" to ", ChatColor.GRAY), new TextElement(x.getValue(), ChatColor.GRAY)));
            }
            s.getCommandSender().sendMessage();
        }

        if(s.isAdmin()) {
            s.getCommandSender().sendMessage(ChatHelper.getInstance().makeAdminTextComponent(new TextElement(s.getCurrentComponent().getPrompt(), ChatColor.YELLOW)));
        } else {
            s.getCommandSender().sendMessage(ChatHelper.getInstance().makeTitleTextComponent(new TextElement(s.getCurrentComponent().getPrompt(), ChatColor.YELLOW)));
        }
        if(s.getCurrentComponent().getExplanation() != null) {
            s.getCommandSender().sendMessage(ChatHelper.getInstance().makeTextComponent(s.getCurrentComponent().getExplanation()));
        }
    }

    @EventHandler
    public void onChatEvent(ChatEvent e) {
        for(Map.Entry<CommandSender, IDialogScene> l : activeScenes.entrySet()) {
            ProxiedPlayer p = ProxyServer.getInstance().getPlayer(l.getKey().getName());
            if(p != null) {
                if(p.equals(e.getSender()) || p.equals(e.getSender())) {
                    e.setCancelled(true);
                    l.getValue().onMessage(e.getMessage());
                }
            }
        }
    }

    @EventHandler
    public void onPlayerLeave(PlayerDisconnectEvent e) {
        for(CommandSender c : activeScenes.keySet()) {
            if(c.getName().equals(e.getPlayer().getName())) {
                activeScenes.remove(c);
                return;
            }
        }
    }

}
