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

public final class OutOfProjectionBoundsException extends Exception {
    /**
     * @param x
     * @param y
     * @param maxX
     * @param maxY
     * @throws OutOfProjectionBoundsException if <code> Math.abs(x) > maxX || Math.abs(y) > maxY </code>
     */
    public static void checkInRange(double x, double y, double maxX, double maxY) throws OutOfProjectionBoundsException {
        if (Math.abs(x) > maxX || Math.abs(y) > maxY) {
            throw new OutOfProjectionBoundsException();
        }
    }

    /**
     * @param longitude
     * @param latitude
     * @throws OutOfProjectionBoundsException if <code> Math.abs(longitude) > 180 || Math.abs(latitude) > 90 </code>
     */
    public static void checkLongitudeLatitudeInRange(double longitude, double latitude) throws OutOfProjectionBoundsException {
        checkInRange(longitude, latitude, 180, 90);
    }
}
