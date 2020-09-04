package com.noahhusby.sledgehammer.commands.admin;

import com.google.common.collect.Maps;
import com.noahhusby.sledgehammer.Constants;
import com.noahhusby.sledgehammer.Sledgehammer;
import com.noahhusby.sledgehammer.commands.data.IAdminCommand;
import com.noahhusby.sledgehammer.commands.data.SetupAdminTracker;
import com.noahhusby.sledgehammer.commands.data.SetupField;
import com.noahhusby.sledgehammer.config.ServerConfig;
import com.noahhusby.sledgehammer.datasets.Location;
import com.noahhusby.sledgehammer.util.ChatHelper;
import com.noahhusby.sledgehammer.util.TextElement;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SetupAdminCommand implements IAdminCommand, Listener {

    Map<CommandSender, SetupAdminTracker> liveDialogs = Maps.newHashMap();
    List<SetupField> setupFields = new ArrayList<>();

    public SetupAdminCommand() {
        Sledgehammer.setupAdminCommandListener(this);
        registerField(dialogAction.SERVER_EDIT,
                ChatHelper.getInstance().makeTextComponent(new TextElement("Do you want to edit this server?", ChatColor.GREEN)),
                ChatHelper.getInstance().makeTextComponent(new TextElement("Type ", ChatColor.GRAY),
                        new TextElement("Yes [Y] or No [N]", ChatColor.RED)), new String[]{"yes","y"}, new String[]{"no","n"});
        registerField(dialogAction.SERVER_PERMISSION_TYPE,
                ChatHelper.getInstance().makeTextComponent(new TextElement("Where should permissions be handled?", ChatColor.GREEN)),
                ChatHelper.getInstance().makeTextComponent(new TextElement("Type ", ChatColor.GRAY),
                        new TextElement("global", ChatColor.RED), new TextElement(" to have the bungeecord server handle permissions, or ", ChatColor.GRAY),
                        new TextElement("local", ChatColor.RED), new TextElement(" to have the local server handle permissions.", ChatColor.GRAY)),
                new String[]{"global"}, new String[]{"local"});
        registerField(dialogAction.SERVER_LOCATION_MENU,
                ChatHelper.getInstance().makeTextComponent(new TextElement("Edit the server locations?", ChatColor.GREEN)),
                ChatHelper.getInstance().makeTextComponent(new TextElement("Type ", ChatColor.GRAY),
                        new TextElement("list", ChatColor.RED), new TextElement(" to view current locations, ", ChatColor.GRAY),
                        new TextElement("add", ChatColor.RED), new TextElement(" to add a new location, ", ChatColor.GRAY),
                        new TextElement("remove", ChatColor.RED), new TextElement(" to remove an existing location, or ", ChatColor.GRAY),
                        new TextElement("finish", ChatColor.RED), new TextElement(" to finish setting up this server.", ChatColor.GRAY)),
                new String[]{"list", "add", "remove", "finish"}, new String[]{""});
        registerField(dialogAction.LOCATION_TYPE,
                ChatHelper.getInstance().makeTextComponent(new TextElement("What type of location?", ChatColor.GREEN)),
                ChatHelper.getInstance().makeTextComponent(new TextElement("Type ", ChatColor.GRAY),
                        new TextElement("city", ChatColor.RED), new TextElement(" for cities and towns, ", ChatColor.GRAY),
                        new TextElement("county", ChatColor.RED), new TextElement(" for counties, ", ChatColor.GRAY),
                        new TextElement("state", ChatColor.RED), new TextElement(" for states and territories, or ", ChatColor.GRAY),
                        new TextElement("country", ChatColor.RED), new TextElement(" for countries.", ChatColor.GRAY)),
                new String[]{"city","county", "state", "country"}, new String[]{""});
        registerField(dialogAction.LOCATION_CITY,
                ChatHelper.getInstance().makeTextComponent(new TextElement("What city?", ChatColor.GREEN)),
                ChatHelper.getInstance().makeTextComponent(new TextElement("Enter the city below.", ChatColor.GRAY)),
                new String[]{"*"}, new String[]{});
        registerField(dialogAction.LOCATION_COUNTY,
                ChatHelper.getInstance().makeTextComponent(new TextElement("What county?", ChatColor.GREEN)),
                ChatHelper.getInstance().makeTextComponent(new TextElement("Enter the county below.", ChatColor.GRAY)),
                new String[]{"*"}, new String[]{});
        registerField(dialogAction.LOCATION_STATE,
                ChatHelper.getInstance().makeTextComponent(new TextElement("What state/territory?", ChatColor.GREEN)),
                ChatHelper.getInstance().makeTextComponent(new TextElement("Enter the state below.", ChatColor.GRAY)),
                new String[]{"*"}, new String[]{});
        registerField(dialogAction.LOCATION_COUNTRY,
                ChatHelper.getInstance().makeTextComponent(new TextElement("What country?", ChatColor.GREEN)),
                ChatHelper.getInstance().makeTextComponent(new TextElement("Enter the country below.", ChatColor.GRAY)),
                new String[]{"*"}, new String[]{});
        registerField(dialogAction.LOCATION_REMOVE,
                ChatHelper.getInstance().makeTextComponent(new TextElement("What location do you want to remove?", ChatColor.GREEN)),
                ChatHelper.getInstance().makeTextComponent(new TextElement("Enter the number below.", ChatColor.GRAY)),
                new String[]{"*"}, new String[]{});
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        liveDialogs.put(sender, new SetupAdminTracker());
        executeDialog(sender, null,true);
    }

    @Override
    public String getName() {
        return "setup";
    }

    @Override
    public String getPurpose() {
        return "Run the automatic setup prompt";
    }

    @EventHandler
    public void onChatEvent(ChatEvent e) {
        for(Map.Entry<CommandSender, SetupAdminTracker> l : liveDialogs.entrySet()) {
            ProxiedPlayer p = ProxyServer.getInstance().getPlayer(l.getKey().getName());
            if(p != null) {
                if(p.equals(e.getSender())) {
                    e.setCancelled(true);
                    executeDialog(l.getKey(), e.getMessage(),false);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerLeave(PlayerDisconnectEvent e) {
        for(CommandSender c : liveDialogs.keySet()) {
            if(c.getName().equals(e.getPlayer().getName())) {
                liveDialogs.remove(c);
                return;
            }
        }
    }

    private void registerField(dialogAction d, TextComponent title, TextComponent menu, String[] acceptable, String[] deniable) {
        setupFields.add(new SetupField(d, title, menu, acceptable, deniable));
    }

    private void executeDialog(CommandSender sender, String message, boolean start) {
        SetupAdminTracker tracker = liveDialogs.get(sender);
        if(message != null) {
            if(message.startsWith("@")) {
                tracker.nextServer();
            } else if(message.startsWith("exit")) {
                liveDialogs.remove(sender, tracker);
                sender.sendMessage(ChatHelper.getInstance().makeTextComponent(new TextElement("Exited the setup dialog!", ChatColor.RED)));
                return;
            } else {
                if(!validateField(sender, message)) return;
                // Aciton case
                switch (tracker.dialogAction) {
                    case SERVER_EDIT:
                        if(getFieldBoolean(sender, message)) {
                            tracker.dialogAction = dialogAction.SERVER_PERMISSION_TYPE;
                        } else {
                            tracker.nextServer();
                        }
                    break;
                    case SERVER_PERMISSION_TYPE:
                        tracker.currentServer.permission_type = message.toLowerCase().trim();
                        tracker.dialogAction = dialogAction.SERVER_LOCATION_MENU;
                        break;
                    case SERVER_LOCATION_MENU:
                        String locationCommand = message.toLowerCase().trim();
                        if(locationCommand.equals("list")) {
                            for(int x = 0; x < 12; x++) {
                                sender.sendMessage(ChatHelper.getInstance().makeTextComponent(new TextElement("", ChatColor.WHITE)));
                            }
                            sender.sendMessage(ChatHelper.getInstance().makeTextComponent(new TextElement("Current Locations:", ChatColor.GOLD)));
                            List<Location> locations = tracker.currentServer.locations;
                            for(Location l : locations) {
                                String x = "";
                                if(!l.city.equals("")) x+= l.city+", ";
                                if(!l.county.equals("")) x+= l.county+", ";
                                if(!l.state.equals("")) x+= l.state+", ";
                                if(!l.country.equals("")) x+= l.country;
                                sender.sendMessage(ChatHelper.getInstance().makeTextComponent(new TextElement(l.detailType.name()+" - ", ChatColor.RED),
                                        new TextElement(x, ChatColor.GOLD)));
                            }
                            sender.sendMessage();
                            sendEntryField(sender);
                            return;
                        } else if(locationCommand.equals("add")) {
                            tracker.dialogAction = dialogAction.LOCATION_TYPE;
                        } else if(locationCommand.equals("finish")) {
                            ServerConfig.getInstance().pushServer(tracker.currentServer);
                            tracker.nextServer();
                            tracker.dialogAction = dialogAction.SERVER_EDIT;
                        } else {
                            for(int x = 0; x < 12; x++) {
                                sender.sendMessage(ChatHelper.getInstance().makeTextComponent(new TextElement("", ChatColor.WHITE)));
                            }
                            List<Location> locations = tracker.currentServer.locations;
                            for(int x = 0; x < locations.size(); x++) {
                                Location l = locations.get(x);
                                String s = "";
                                if(!l.city.equals("")) s+= l.city+", ";
                                if(!l.county.equals("")) s+= l.county+", ";
                                if(!l.state.equals("")) s+= l.state+", ";
                                if(!l.country.equals("")) s+= l.country;
                                sender.sendMessage(ChatHelper.getInstance().makeTextComponent(
                                        new TextElement(String.valueOf((x+1)+". "), ChatColor.YELLOW),
                                        new TextElement(l.detailType.name()+" - ", ChatColor.RED),
                                        new TextElement(s, ChatColor.GOLD)));
                            }
                            sender.sendMessage();
                            tracker.dialogAction = dialogAction.LOCATION_REMOVE;
                            sendEntryField(sender);
                            return;
                        }
                        break;
                    case LOCATION_TYPE:
                        String typeCommand = message.toLowerCase().trim();
                        if(typeCommand.equals("city")) {
                            tracker.locationEdit.detailType = Location.detail.city;
                            tracker.dialogAction = dialogAction.LOCATION_CITY;
                        } else if(typeCommand.equals("county")) {
                            tracker.locationEdit.detailType = Location.detail.county;
                            tracker.dialogAction = dialogAction.LOCATION_COUNTY;
                        } else if(typeCommand.equals("state")) {
                            tracker.locationEdit.detailType = Location.detail.state;
                            tracker.dialogAction = dialogAction.LOCATION_STATE;
                        } else {
                            tracker.locationEdit.detailType = Location.detail.country;
                            tracker.dialogAction = dialogAction.LOCATION_COUNTRY;
                        }
                        break;
                    case LOCATION_CITY:
                        tracker.locationEdit.city = message.toLowerCase().trim();
                        tracker.dialogAction = dialogAction.LOCATION_STATE;
                        break;
                    case LOCATION_COUNTY:
                        tracker.locationEdit.county = message.toLowerCase().trim();
                        tracker.dialogAction = dialogAction.LOCATION_STATE;
                        break;
                    case LOCATION_STATE:
                        tracker.locationEdit.state = message.toLowerCase().trim();
                        tracker.dialogAction = dialogAction.LOCATION_COUNTRY;
                        break;
                    case LOCATION_COUNTRY:
                        tracker.locationEdit.country = message.toLowerCase().trim();
                        tracker.dialogAction = dialogAction.SERVER_LOCATION_MENU;
                        for(int x = 0; x < 14; x++) {
                            sender.sendMessage(ChatHelper.getInstance().makeTextComponent(new TextElement("", ChatColor.WHITE)));
                        }
                        sender.sendMessage(ChatHelper.getInstance().makeTextComponent(new TextElement("New Location:", ChatColor.GOLD)));
                        Location l = tracker.locationEdit;
                        String x = "";
                        if (!l.city.equals("")) x += l.city + ", ";
                        if (!l.county.equals("")) x += l.county + ", ";
                        if (!l.state.equals("")) x += l.state + ", ";
                        if (!l.country.equals("")) x += l.country;
                        sender.sendMessage(ChatHelper.getInstance().makeTextComponent(new TextElement(l.detailType.name() + " - ", ChatColor.RED),
                                    new TextElement(x, ChatColor.GOLD)));
                        sender.sendMessage();
                        tracker.currentServer.locations.add(l);
                        tracker.locationEdit = new Location();
                        sendEntryField(sender);
                        return;
                    case LOCATION_REMOVE:
                        try {
                            int i = Integer.parseInt(message.trim());
                            List<Location> locations = tracker.currentServer.locations;
                            if(i > locations.size()) {
                                tracker.dialogAction = dialogAction.SERVER_LOCATION_MENU;
                                sendMenu(sender, true, start);
                                sendEntryField(sender);
                                return;
                            }
                            locations.remove(locations.get(i-1));
                            tracker.dialogAction = dialogAction.SERVER_LOCATION_MENU;
                        } catch (Exception e) {
                            e.printStackTrace();
                            tracker.dialogAction = dialogAction.SERVER_LOCATION_MENU;
                            sendMenu(sender, true, start);
                            sendEntryField(sender);
                            return;
                        }
                }
            }

            sendMenu(sender, false, start);
            sendEntryField(sender);
        } else {
            sendMenu(sender, false, start);
            sendEntryField(sender);
        }
    }

    private boolean getFieldBoolean(CommandSender sender, String message) {
        SetupAdminTracker tracker = liveDialogs.get(sender);
        for (SetupField s : setupFields) {
            if (s.dialogAction == tracker.dialogAction) {
                for (String c : s.deniable) {
                    if (message.toLowerCase().trim().equals(c)) return false;
                }
                for (String c : s.acceptable) {
                    if (message.toLowerCase().trim().equals(c)) return true;
                }
            }
        }
        return false;
    }

    private boolean validateField(CommandSender sender, String message) {
        SetupAdminTracker tracker = liveDialogs.get(sender);
        for(SetupField s : setupFields) {
            if(s.dialogAction == tracker.dialogAction) {
                for(String c : s.deniable) {
                    if(c.equals("*")) return true;
                    if(message.toLowerCase().trim().equals(c)) return true;
                }
                for(String c : s.acceptable) {
                    if(c.equals("*")) return true;
                    if(message.toLowerCase().trim().equals(c)) return true;
                }
            }
        }
        sendMenu(sender, true, false);
        sendEntryField(sender);
        return false;
    }

    private void sendEntryField(CommandSender sender) {
        SetupAdminTracker tracker = liveDialogs.get(sender);
        for(SetupField s : setupFields) {
            if(s.dialogAction == tracker.dialogAction) {
                sender.sendMessage(s.title);
                sender.sendMessage(s.menu);
                sender.sendMessage();
            }
        }
    }

    private void sendMenu(CommandSender sender, boolean error, boolean start) {
        SetupAdminTracker tracker = liveDialogs.get(sender);
        for(int x = 0; x < 12; x++) {
            sender.sendMessage(ChatHelper.getInstance().makeTextComponent(new TextElement("", ChatColor.WHITE)));
        }
        if(error) {
            sender.sendMessage(ChatHelper.getInstance().makeTextComponent(new TextElement("That was an invalid entry! Please try again.", ChatColor.RED)));
            sender.sendMessage();
        }
        if(start) {
            sender.sendMessage(ChatHelper.getInstance().makeTextComponent(new TextElement("-----------------------", ChatColor.GOLD),
                    new TextElement("\nSledgehammer Setup Dialog", ChatColor.YELLOW)));
        }
        sender.sendMessage(ChatHelper.getInstance().makeTextComponent(
                new TextElement("Server ", ChatColor.GRAY),
                new TextElement(String.valueOf(tracker.getCurrentServerIndex()+1), ChatColor.GREEN),
                new TextElement(" of ", ChatColor.GRAY),
                new TextElement(String.valueOf(tracker.getMaxServerIndex()), ChatColor.GREEN),
                new TextElement(" - ", ChatColor.GRAY),
                new TextElement(String.valueOf(tracker.currentBungeeServer.getName()), ChatColor.RED),
                new TextElement("\nType ", ChatColor.GRAY),
                new TextElement("exit", ChatColor.RED),
                new TextElement(" to quit setup, and ", ChatColor.GRAY),
                new TextElement("@", ChatColor.RED),
                new TextElement(" to continue to another server.", ChatColor.GRAY)));
        sender.sendMessage(ChatHelper.getInstance().makeTextComponent(new TextElement("", ChatColor.WHITE)));
    }

    public enum dialogAction {
        SERVER_EDIT, SERVER_PERMISSION_TYPE, SERVER_LOCATION_MENU, LOCATION_TYPE, LOCATION_CITY, LOCATION_COUNTY, LOCATION_STATE,
        LOCATION_COUNTRY, LOCATION_REMOVE
    }
}
