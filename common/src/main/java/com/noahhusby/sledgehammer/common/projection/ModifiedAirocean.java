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

public class ModifiedAirocean extends ConformalEstimate {

    protected static final double THETA = -150 * TO_RADIANS;
    protected static final double SIN_THETA = Math.sin(THETA);
    protected static final double COS_THETA = Math.cos(THETA);
    protected static final double BERING_X = -0.3420420960118339;//-0.3282152608138795;
    protected static final double BERING_Y = -0.322211064085279;//-0.3281491467713469;
    protected static final double ARCTIC_Y = -0.2;//-0.3281491467713469;
    protected static final double ARCTIC_M = (ARCTIC_Y - ROOT3 * ARC / 4) / (BERING_X - -0.5 * ARC);
    protected static final double ARCTIC_B = ARCTIC_Y - ARCTIC_M * BERING_X;
    protected static final double ALEUTIAN_Y = -0.5000446805492526;//-0.5127463765943157;
    protected static final double ALEUTIAN_XL = -0.5149231279757507;//-0.4957832938238718;
    protected static final double ALEUTIAN_XR = -0.45;
    protected static final double ALEUTIAN_M = (BERING_Y - ALEUTIAN_Y) / (BERING_X - ALEUTIAN_XR);
    protected static final double ALEUTIAN_B = BERING_Y - ALEUTIAN_M * BERING_X;

    public double[] fromGeo(double lon, double lat) {
        double[] c = super.fromGeo(lon, lat);
        double x = c[0], y = c[1];

        boolean easia = isEurasianPart(x, y);

        y -= 0.75 * ARC * ROOT3;

        if (easia) {
            x += ARC;

            double t = x;
            x = COS_THETA * x - SIN_THETA * y;
            y = SIN_THETA * t + COS_THETA * y;

        } else {
            x -= ARC;
        }

        c[0] = y;
        c[1] = -x;
        return c;
    }

    public double[] toGeo(double x, double y) {
        boolean easia;
        if (y < 0) {
            easia = x > 0;
        } else if (y > ARC / 2) {
            easia = x > -ROOT3 * ARC / 2;
        } else {
            easia = y * -ROOT3 < x;
        }

        double t = x;
        x = -y;
        y = t;

        if (easia) {
            t = x;
            x = COS_THETA * x + SIN_THETA * y;
            y = COS_THETA * y - SIN_THETA * t;
            x -= ARC;

        } else {
            x += ARC;
        }

        y += 0.75 * ARC * ROOT3;

        //check to make sure still in right part
        if (easia != isEurasianPart(x, y)) {
            return OUT_OF_BOUNDS;
        }

        return super.toGeo(x, y);
    }

    protected boolean isEurasianPart(double x, double y) {

        //catch vast majority of cases in not near boundary
        if (x > 0) {
            return false;
        }
        if (x < -0.5 * ARC) {
            return true;
        }

        if (y > ROOT3 * ARC / 4) //above arctic ocean
        {
            return x < 0;
        }

        if (y < ALEUTIAN_Y) //below bering sea
        {
            return y < (ALEUTIAN_Y + ALEUTIAN_XL) - x;
        }

        if (y > BERING_Y) { //boundary across arctic ocean

            if (y < ARCTIC_Y) {
                return x < BERING_X; //in strait
            }

            return y < ARCTIC_M * x + ARCTIC_B; //above strait
        }

        return y > ALEUTIAN_M * x + ALEUTIAN_B;
    }

    public double[] bounds() {
        return new double[]{ -1.5 * ARC * ROOT3, -1.5 * ARC, 3 * ARC, ROOT3 * ARC }; //TODO: 3*ARC is prly to high
    }
}