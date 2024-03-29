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

package com.noahhusby.sledgehammer.proxy.dialogs.components.location;

import com.noahhusby.sledgehammer.proxy.ChatUtil;
import com.noahhusby.sledgehammer.proxy.datasets.Location;
import com.noahhusby.sledgehammer.proxy.dialogs.components.DialogComponent;
import com.noahhusby.sledgehammer.proxy.servers.ServerHandler;
import lombok.RequiredArgsConstructor;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;

import java.util.List;

@RequiredArgsConstructor
public class LocationRemovalComponent extends DialogComponent {

    private final ServerInfo server;
    private List<Location> locations;

    @Override
    public String getKey() {
        return "locationremove";
    }

    @Override
    public String getPrompt() {
        return "Which location would you like to delete?";
    }

    @Override
    public TextComponent getExplanation() {
        TextComponent explanation = ChatUtil.combine(ChatColor.GRAY, "Enter the # of the location to delete:");
        locations = ServerHandler.getInstance().getLocationsFromServer(server.getName());
        for (int i = 0; i < locations.size(); i++) {
            explanation.addExtra(ChatUtil.combine(ChatColor.RED, "\n" + i + ". ", ChatColor.GOLD, ChatUtil.capitalize(locations.get(i).detailType.name()), " - ", ChatColor.RED, locations.get(i)));
        }
        return explanation;
    }

    @Override
    public String[] getAcceptableResponses() {
        return new String[]{ "*" };
    }

    @Override
    public boolean validateResponse(String v) {
        try {
            int vm = Integer.parseInt(v.toLowerCase().trim());
            return vm > -1 && vm < locations.size();
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
