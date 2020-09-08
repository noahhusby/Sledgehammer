package com.noahhusby.sledgehammer.data.dialogs.components.location;

import com.noahhusby.sledgehammer.data.dialogs.components.DialogComponent;
import com.noahhusby.sledgehammer.util.TextElement;
import net.md_5.bungee.api.ChatColor;

public class LocationSelectionComponent extends DialogComponent {
    @Override
    public String getKey() {
        return "locationmode";
    }

    @Override
    public String getPrompt() {
        return "What type of location?";
    }

    @Override
    public TextElement[] getExplanation() {
        return new TextElement[]{new TextElement("Enter one of the following modes: ", ChatColor.GRAY),
        new TextElement("City", ChatColor.BLUE), new TextElement(", ", ChatColor.GRAY), new TextElement("County", ChatColor.BLUE),
        new TextElement(", ", ChatColor.GRAY), new TextElement("State", ChatColor.BLUE), new TextElement(", or ", ChatColor.GRAY),
        new TextElement("Country", ChatColor.BLUE)};
    }

    @Override
    public String[] getAcceptableResponses() {
        return new String[]{"*"};
    }

    @Override
    public boolean validateResponse(String v) {
        String vm = v.toLowerCase().trim();
        return vm.equals("city") || vm.equals("county") || vm.equals("state") || vm.equals("country");
    }
}
