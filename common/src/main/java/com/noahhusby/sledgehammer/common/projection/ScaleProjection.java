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

public class ScaleProjection extends ProjectionTransform {

    final double scaleX;
    final double scaleY;

    public ScaleProjection(GeographicProjection input, double scaleX, double scaleY) {
        super(input);
        this.scaleX = scaleX;
        this.scaleY = scaleY;
    }

    public double[] toGeo(double x, double y) {
        return input.toGeo(x / scaleX, y / scaleY);
    }

    public double[] fromGeo(double lon, double lat) {
        double[] p = input.fromGeo(lon, lat);
        p[0] *= scaleX;
        p[1] *= scaleY;
        return p;
    }

    public boolean upright() {
        return (scaleY < 0) ^ input.upright();
    }

    public double[] bounds() {
        double[] b = input.bounds();
        b[0] *= scaleX;
        b[1] *= scaleY;
        b[2] *= scaleX;
        b[3] *= scaleY;
        return b;
    }

    public double metersPerUnit() {
        return input.metersPerUnit() / Math.sqrt((scaleX * scaleX + scaleY * scaleY) / 2); //TODO: better transform
    }
}
