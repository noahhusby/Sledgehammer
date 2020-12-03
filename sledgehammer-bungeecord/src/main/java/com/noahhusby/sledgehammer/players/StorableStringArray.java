/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - PlayerAttribute.java
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
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.Arrays;

public class StorableStringArray implements Storable {
    public final String[] stringArray;

    public StorableStringArray() {
        this(new String[]{});
    }

    public StorableStringArray(String[] array) {
        this.stringArray = array;
    }

    @Override
    public Storable load(JSONObject data) {
        JSONArray array = new JSONArray();

            array = (JSONArray) data.get("array");

        return new StorableStringArray((String[]) array.toArray(new String[array.size()]));
    }

    @Override
    public JSONObject save(JSONObject data) {
        JSONArray array = new JSONArray();
        array.addAll(Arrays.asList(stringArray));

        data.put("array", array);
        return data;
    }
}
