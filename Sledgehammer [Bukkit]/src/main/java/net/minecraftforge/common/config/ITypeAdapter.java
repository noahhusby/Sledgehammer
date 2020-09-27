/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bukkit] - ITypeAdapter.java
 * All rights reserved.
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
