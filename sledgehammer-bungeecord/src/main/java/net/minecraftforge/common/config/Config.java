/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - Config.java
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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Config
{
    /**
    * The mod id that this configuration is associated with.
    */
    String modid();
    /**
     * A user friendly name for the config file,
     * the default will be modid
     */
    String name() default "";

    /**
     * The type this is, right now the only value is INSTANCE.
     * This is intended to be expanded upon later for more Forge controlled
     * configs.
     */
    Type type() default Type.INSTANCE;

    /**
     * Root element category, defaults to "general", if this is an empty string then the root category is disabled.
     * Any primitive fields will cause an error, and you must specify sub-category objects
     */
    String category() default "general";

    public static enum Type
    {
        /**
        * Loaded once, directly after mod construction. Before pre-init.
        * This class must have static fields.
        */
        INSTANCE(true);


        private boolean isStatic = true;
        private Type(boolean isStatic) { this.isStatic = isStatic; }
        public boolean isStatic(){ return this.isStatic; }
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.FIELD, ElementType.TYPE})
    @interface LangKey
    {
        String value();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    @interface Comment
    {
        String[] value();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    @interface Ignore
    {}

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    @interface RangeInt
    {
        int min() default Integer.MIN_VALUE;
        int max() default Integer.MAX_VALUE;
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    @interface RangeDouble
    {
        double min() default Double.MIN_VALUE;
        double max() default Double.MAX_VALUE;
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    @interface Name
    {
        String value();
    }
    
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.FIELD, ElementType.TYPE})
    @interface RequiresMcRestart
    {}
    
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.FIELD, ElementType.TYPE})
    @interface RequiresWorldRestart
    {}

    /**
     * A field marked with this annotation (and {@link RangeInt} or {@link RangeDouble}) will have a slider control attached in the config UI
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    @interface SlidingOption
    {}
}
