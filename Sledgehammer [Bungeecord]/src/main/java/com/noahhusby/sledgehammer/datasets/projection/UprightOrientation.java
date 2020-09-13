package com.noahhusby.sledgehammer.datasets.projection;

import com.noahhusby.sledgehammer.projection.GeographicProjection;
import com.noahhusby.sledgehammer.projection.ProjectionTransform;

public class UprightOrientation extends ProjectionTransform {

    public UprightOrientation (GeographicProjection input) {
        super(input);
    }

    public double[] toGeo(double x, double y) {
        return input.toGeo(x, -y);
    }

    public double[] fromGeo(double lon, double lat) {
        double[] p = input.fromGeo(lon, lat);
        p[1] = -p[1];
        return p;
    }

    public boolean upright() {
        return !input.upright();
    }

    public double[] bounds() {
        double[] b = input.bounds();
        return new double[] {b[0],-b[3],b[2],-b[1]};
    }
}