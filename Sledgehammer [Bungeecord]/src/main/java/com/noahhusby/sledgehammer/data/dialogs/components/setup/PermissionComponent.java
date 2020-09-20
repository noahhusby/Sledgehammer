package com.noahhusby.sledgehammer.data.dialogs.components.setup;

import com.noahhusby.sledgehammer.data.dialogs.components.DialogComponent;
import com.noahhusby.sledgehammer.util.TextElement;
import net.md_5.bungee.api.ChatColor;

public class PermissionComponent extends DialogComponent {
    @Override
    public String getKey() {
        return "permission";
    }

    @Override
    public String getPrompt() {
        return "Where should permissions be handled?";
    }

    @Override
    public TextElement[] getExplanation() {
        return new TextElement[]{new TextElement("Type ", ChatColor.GRAY), new TextElement("global ", ChatColor.RED),
        new TextElement("to have the proxy handle permissions, or ", ChatColor.GRAY), new TextElement("local ", ChatColor.RED),
        new TextElement("to have the local server handle permissions.", ChatColor.GRAY)};
    }

    @Override
    public String[] getAcceptableResponses() {
        return new String[]{"*"};
    }

    @Override
    public boolean validateResponse(String v) {
        String vm = v.toLowerCase().trim();
        return vm.equals("global") || vm.equals("local");
    }
}
