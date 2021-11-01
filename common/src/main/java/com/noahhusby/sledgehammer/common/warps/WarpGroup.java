/*
 *  Copyright (c) 2021 Noah Husby
 *  Sledgehammer - WarpGroup.java
 *
 *  Sledgehammer is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Sledgehammer is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Sledgehammer.  If not, see <https://github.com/noahhusby/Sledgehammer/blob/master/LICENSE/>.
 */

package com.noahhusby.sledgehammer.common.warps;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.noahhusby.lib.data.storage.Key;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author Noah Husby
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Key("Id")
public class WarpGroup {
    @Expose
    @SerializedName("Id")
    private String id;
    @Expose
    @SerializedName("Name")
    private String name;
    @Expose
    @SerializedName("HeadId")
    private String headId;
    @Expose
    @SerializedName("Type")
    private WarpGroupType type = WarpGroupType.GROUP;
    @Expose
    @SerializedName("Warps")
    private final List<Integer> warps = new ArrayList<>();
    @Expose
    @SerializedName("Servers")
    private final Set<String> servers = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
    @Expose
    @SerializedName("Aliases")
    private final List<String> aliases = new ArrayList<>();
}
