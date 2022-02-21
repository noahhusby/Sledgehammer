/*
 * MIT License
 *
 * Copyright 2020-2022 noahhusby
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

    public enum Orientation {
        none, upright, swapped
    }
}
