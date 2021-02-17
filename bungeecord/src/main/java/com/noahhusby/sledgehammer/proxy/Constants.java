/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - Constants.java
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

package com.noahhusby.sledgehammer.proxy;
import com.noahhusby.sledgehammer.common.CommonConstants;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Constants extends CommonConstants {
    public static final String PLUGINID   = "sledgehammer";
    public static final String nominatimAPI = "https://nominatim.openstreetmap.org/reverse.php?osm_type=N&format=json&zoom={zoom}";

    public static final String adminMessagePrefix = "&9&lSH &8&l> ";

    public static final int borderZone = 500;
    public static final int warpIdBuffer = 45;

}
