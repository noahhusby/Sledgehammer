/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - ITypeAdapter.java
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

package net.minecraftforge.common.config;

import net.minecraftforge.common.config.Property.Type;

/**
 * Abstracts the types of properties away. Higher level logic must prevent invalid data types.
 */
interface ITypeAdapter
{
    /**
     * Assigns the default value to the property
     * @param property the property whose default value will be assigned
     * @param value the default value
     */
    void setDefaultValue(Property property, Object value);

    /**
     * Sets the properties value.
     * @param property the property whose value will be set
     * @param value the set value
     */
	void setValue(Property property, Object value);

    /**
     * Retrieves the properties value
     * @param prop the property whose value will be retrieved
     * @return the properties value
     */
	Object getValue(Property prop);

    Type getType();

    boolean isArrayAdapter();
}
