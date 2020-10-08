/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - MapSession.java
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
 *
 */

package com.noahhusby.sledgehammer.maps;

import java.util.UUID;

public class MapSession {
    public String name;
    public UUID key;
    public long time;
    public long timeout;

    public MapSession() {}
}
