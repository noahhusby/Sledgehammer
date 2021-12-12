/*
 * Copyright (c) 2020 Noah Husby
 * sledgehammer - GeographicProjection.java
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
 * You should have received a copy of the GNU General Public License
 * along with Sledgehammer.  If not, see <https://github.com/noahhusby/Sledgehammer/blob/master/LICENSE/>.
 */

package com.noahhusby.sledgehammer.common.projection;

public class GeographicProjection {

    public static final double EARTH_CIRCUMFERENCE = 40075017;
    public static final double EARTH_POLAR_CIRCUMFERENCE = 40008000;

    public static GeographicProjection orientProjection(GeographicProjection base, Orientation o) {
        if (base.upright()) {
            if (o == Orientation.upright) {
                return base;
            }
            base = new UprightOrientation(base);
        }

        if (o == Orientation.swapped) {
            return new InvertedOrientation(base);
        } else if (o == Orientation.upright) {
            base = new UprightOrientation(base);
        }

        return base;
    }

    public enum Orientation {
        none, upright, swapped
    }

    public double[] toGeo(double x, double y) {
        return new double[]{ x, y };
    }

    public double[] fromGeo(double lon, double lat) {
        return new double[]{ lon, lat };
    }

    public double metersPerUnit() {
        return 100000;
    }

    public double[] bounds() {

        //get max in by using extreme coordinates
        double[] b = new double[]{
                fromGeo(-180, 0)[0],
                fromGeo(0, -90)[1],
                fromGeo(180, 0)[0],
                fromGeo(0, 90)[1]
        };

        if (b[0] > b[2]) {
            double t = b[0];
            b[0] = b[2];
            b[2] = t;
        }

        if (b[1] > b[3]) {
            double t = b[1];
            b[1] = b[3];
            b[3] = t;
        }

        return b;
    }

    public boolean upright() {
        return fromGeo(0, 90)[1] <= fromGeo(0, -90)[1];
    }

    public double[] vector(double x, double y, double north, double east) {
        double[] geo = toGeo(x, y);

        //TODO: east may be slightly off because earth not a sphere
        double[] off = fromGeo(geo[0] + east * 360.0 / (Math.cos(geo[1] * Math.PI / 180.0) * EARTH_CIRCUMFERENCE),
                geo[1] + north * 360.0 / EARTH_POLAR_CIRCUMFERENCE);

        return new double[]{ off[0] - x, off[1] - y };
    }
}
