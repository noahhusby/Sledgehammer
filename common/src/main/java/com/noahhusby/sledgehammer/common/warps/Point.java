/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - Point.java
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

package com.noahhusby.sledgehammer.common.warps;

import com.google.gson.annotations.Expose;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Getter
@AllArgsConstructor
public class Point {
    @Expose
    private final double x;
    @Expose
    private final double y;
    @Expose
    private final double z;
    @Expose
    private final double yaw;
    @Expose
    private final double pitch;

    public Point limit() {
        double lX = new BigDecimal(x).setScale(3, RoundingMode.HALF_UP).doubleValue();
        double lY = new BigDecimal(y).setScale(3, RoundingMode.HALF_UP).doubleValue();
        double lZ = new BigDecimal(z).setScale(3, RoundingMode.HALF_UP).doubleValue();
        double lYaw = new BigDecimal(yaw).setScale(3, RoundingMode.HALF_UP).doubleValue();
        double lPitch = new BigDecimal(pitch).setScale(3, RoundingMode.HALF_UP).doubleValue();
        return new Point(lX, lY, lZ, lYaw, lPitch);
    }

    public Point() {
        this(0, 0, 0, 0, 0);
    }
}
