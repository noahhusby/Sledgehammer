/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - ProjectionTransform.java
 * All rights reserved.
 */

package com.noahhusby.sledgehammer.projection;

public abstract class ProjectionTransform extends GeographicProjection {
    protected GeographicProjection input;

    public ProjectionTransform(GeographicProjection input) {
        this.input = input;
    }

    public boolean upright() {
        return input.upright();
    }

    public double[] bounds() {
        return input.bounds();
    }

    public double metersPerUnit() {
        return input.metersPerUnit();
    }
}
