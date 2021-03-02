/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - ServerGroup.java
 *
 * Sledgehammer is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Sledgehammer is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Sledgehammer.  If not, see <https://github.com/noahhusby/Sledgehammer/blob/master/LICENSE/>.
 */

package com.noahhusby.sledgehammer.proxy.config;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class ServerGroup {
    @Expose
    @SerializedName("Id")
    @Getter
    @Setter
    private String ID;
    @Expose
    @SerializedName("HeadId")
    @Getter
    @Setter
    private String headID;
    @Expose
    @SerializedName("Name")
    @Getter
    @Setter
    private String name;
    @Expose
    @SerializedName("Servers")
    @Getter
    private List<String> servers;
    @Expose
    @SerializedName("Aliases")
    @Getter
    private List<String> aliases;

    public ServerGroup() {
        this("", "", "", new ArrayList<>(), new ArrayList<>());
    }
}
