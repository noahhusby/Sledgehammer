package com.noahhusby.sledgehammer.data.dialogs.components.location;

import com.noahhusby.sledgehammer.config.ServerConfig;
import com.noahhusby.sledgehammer.data.dialogs.components.DialogComponent;
import com.noahhusby.sledgehammer.datasets.Location;
import com.noahhusby.sledgehammer.util.ChatHelper;
import com.noahhusby.sledgehammer.util.TextElement;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.config.ServerInfo;

import java.util.ArrayList;
import java.util.List;

public class LocationListComponent extends DialogComponent {

    ServerInfo server;
    List<Location> locations;

    public LocationListComponent(ServerInfo server) {
        this.server = server;
    }

    @Override
    public String getKey() {
        return "";
    }

    @Override
    public String getPrompt() {
        return "Locations - "+server.getName();
    }

    @Override
    public TextElement[] getExplanation() {
        List<TextElement> text = new ArrayList<>();
        text.add(new TextElement("Type anything to continue", ChatColor.GRAY));

        locations = ServerConfig.getInstance().getLocationsFromServer(server.getName());
        int v = 0;
        for(Location l : locations) {
            String x = "";
            if(!l.city.equals("")) x+= ChatHelper.capitalize(l.city)+", ";
            if(!l.county.equals("")) x+= ChatHelper.capitalize(l.county)+", ";
            if(!l.state.equals("")) x+= ChatHelper.capitalize(l.state)+", ";
            if(!l.country.equals("")) x+= ChatHelper.capitalize(l.country);
            text.add(new TextElement("\n"+v+". ", ChatColor.RED));
            text.add(new TextElement(ChatHelper.capitalize(l.detailType.name() + " - "), ChatColor.GOLD));
            text.add(new TextElement(x, ChatColor.RED));
            v++;
        }

        return text.toArray(text.toArray(new TextElement[text.size()]));
    }

    @Override
    public String[] getAcceptableResponses() {
        return new String[]{"*"};
    }

    @Override
    public boolean validateResponse(String v) {
        return true;
    }
}
