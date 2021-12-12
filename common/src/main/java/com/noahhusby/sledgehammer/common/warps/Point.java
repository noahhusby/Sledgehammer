/*
 * MIT License
 *
 * Copyright 2020-2021 noahhusby
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy,
 * modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software
 * is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS
 * BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
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
