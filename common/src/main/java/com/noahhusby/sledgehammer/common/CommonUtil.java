package com.noahhusby.sledgehammer.common;

import com.google.gson.Gson;
import com.noahhusby.sledgehammer.common.projection.GeographicProjection;
import com.noahhusby.sledgehammer.common.projection.ModifiedAirocean;
import com.noahhusby.sledgehammer.common.projection.ScaleProjection;

/**
 * @author Noah Husby
 */
public abstract class CommonUtil {
    public static final Gson GSON = new Gson();

    private static final GeographicProjection projection = new ModifiedAirocean();
    private static final GeographicProjection uprightProj = GeographicProjection.orientProjection(projection, GeographicProjection.Orientation.upright);
    private static final ScaleProjection scaleProj = new ScaleProjection(uprightProj, CommonConstants.SCALE, CommonConstants.SCALE);

    /**
     * Gets the geographical location from in-game coordinates
     * @param x X-Axis in-game
     * @param z Z-Axis in-game
     * @return The geographical location (Long, Lat)
     */
    public static double[] toGeo(double x, double z) {
        return scaleProj.toGeo(x, z);
    }

    /**
     * Gets in-game coordinates from geographical location
     * @param lon Geographical Longitude
     * @param lat Geographic Latitude
     * @return The in-game coordinates (x, z)
     */
    public static double[] fromGeo(double lon, double lat) {
        return scaleProj.fromGeo(lon, lat);
    }

    /**
     * Gets the geographic projection used to convert coordinates
     * @author SmylerMC
     * @return a BTE compatible projection
     */
    public static GeographicProjection getBTEProjection() {
        return scaleProj;
    }
}
