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

package com.noahhusby.sledgehammer.common.projection;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class ConformalEstimate extends Airocean {

    final InvertableVectorField inverse;

    final double VECTOR_SCALE_FACTOR = 1 / 1.1473979730192934;

    public ConformalEstimate() {
        InputStream is;


        int sideLength = 256;

        double[][] xs = new double[sideLength + 1][];
        double[][] ys = new double[xs.length][];

        try {
            is = getClass().getClassLoader().getResourceAsStream("assets/sledgehammer/data/conformal.txt");
            Scanner sc = new Scanner(is);

            for (int u = 0; u < xs.length; u++) {
                double[] px = new double[xs.length - u];
                double[] py = new double[xs.length - u];
                xs[u] = px;
                ys[u] = py;
            }

            for (int v = 0; v < xs.length; v++) {
                for (int u = 0; u < xs.length - v; u++) {
                    String line = sc.nextLine();
                    line = line.substring(1, line.length() - 3);
                    String[] split = line.split(", ");
                    xs[u][v] = Double.parseDouble(split[0]) * VECTOR_SCALE_FACTOR;
                    ys[u][v] = Double.parseDouble(split[1]) * VECTOR_SCALE_FACTOR;
                }
            }

            is.close();
        } catch (IOException e) {
            System.err.println("Can't load conformal: " + e);
        }

        inverse = new InvertableVectorField(xs, ys);
    }

    protected double[] triangleTransform(double x, double y, double z) {
        double[] c = super.triangleTransform(x, y, z);

        x = c[0];
        y = c[1];

        c[0] /= ARC;
        c[1] /= ARC;

        c[0] += 0.5;
        c[1] += ROOT3 / 6;

        c = inverse.applyNewtonsMethod(x, y, c[0], c[1], 5);//c[0]/ARC + 0.5, c[1]/ARC + ROOT3/6

        c[0] -= 0.5;
        c[1] -= ROOT3 / 6;

        c[0] *= ARC;
        c[1] *= ARC;


        return c;
    }

    protected double[] inverseTriangleTransform(double x, double y) {


        x /= ARC;
        y /= ARC;

        x += 0.5;
        y += ROOT3 / 6;

        double[] c = inverse.getInterpolatedVector(x, y);

        return super.inverseTriangleTransform(c[0], c[1]);
    }

    public double metersPerUnit() {
        return (40075017 / (2 * Math.PI)) / VECTOR_SCALE_FACTOR;
    }
}
