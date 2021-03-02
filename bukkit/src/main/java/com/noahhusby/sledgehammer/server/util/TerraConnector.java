package com.noahhusby.sledgehammer.server.util;

import com.noahhusby.sledgehammer.server.SledgehammerUtil;
import lombok.NoArgsConstructor;
import net.buildtheearth.terraplusplus.dataset.IScalarDataset;
import net.buildtheearth.terraplusplus.generator.EarthGeneratorPipelines;
import net.buildtheearth.terraplusplus.generator.EarthGeneratorSettings;
import net.buildtheearth.terraplusplus.generator.GeneratorDatasets;
import net.buildtheearth.terraplusplus.projection.OutOfProjectionBoundsException;

import java.util.concurrent.CompletableFuture;

/**
 * @author Noah Husby
 */
@NoArgsConstructor
public class TerraConnector {
    private final EarthGeneratorSettings bteGeneratorSettings = EarthGeneratorSettings.parse(EarthGeneratorSettings.BTE_DEFAULT_SETTINGS);

    public CompletableFuture<Double> getHeight(double x, double z) {
        double[] adjustedProj = SledgehammerUtil.toGeo(x, z);
        double adjustedLon = adjustedProj[0];
        double adjustedLat = adjustedProj[1];
        GeneratorDatasets datasets = new GeneratorDatasets(bteGeneratorSettings);
        CompletableFuture<Double> altFuture;
        try {
            altFuture = datasets.<IScalarDataset>getCustom(EarthGeneratorPipelines.KEY_DATASET_HEIGHTS)
                    .getAsync(adjustedLon, adjustedLat)
                    .thenApply(a -> a + 1.0d);
        } catch (OutOfProjectionBoundsException e) {
            altFuture = CompletableFuture.completedFuture(0.0);
        }
        return altFuture;
    }
}
