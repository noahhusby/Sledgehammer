/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - Attribute.java
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

package com.noahhusby.sledgehammer.players;

import com.noahhusby.lib.data.storage.Storable;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class Attribute implements Storable {
    private UUID uuid;
    private List<String> attributes;

    public Attribute() {
        this(UUID.randomUUID(), new ArrayList<>());
    }

    public Attribute(UUID uuid, List<String> attributes) {
        this.uuid = uuid;
        this.attributes = attributes;
    }

    public List<String> getAttributes() {
        return attributes;
    }

    public UUID getUuid() {
        return uuid;
    }

    @Override
    public Storable load(JSONObject data) {
        String att = (String) data.get("Attributes");
        List<String> atts = new ArrayList<>();
        if(!att.equalsIgnoreCase(""))
            atts = new ArrayList<>(Arrays.asList(att.split(",")));
        return new Attribute(UUID.fromString((String) data.get("UUID")), atts);
    }

    @Override
    public JSONObject save(JSONObject data) {
        data.put("UUID", uuid.toString());
        data.put("Attributes", String.join(",", attributes));
        return data;
    }
}
