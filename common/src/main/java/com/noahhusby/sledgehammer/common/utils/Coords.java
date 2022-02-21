package com.noahhusby.sledgehammer.common.utils;

import com.noahhusby.sledgehammer.common.exceptions.InvalidCoordinatesException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import static com.noahhusby.sledgehammer.common.CommonUtil.*;

/**
 * @author Noah Husby
 */
@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class Coords {
    private final double lat;
    private final double lon;
    private double height = Double.NaN;

    /**
     * Parse coords from an array of arguments
     *
     * @param args Arguments
     * @return {@link Coords}
     * @throws InvalidCoordinatesException if coordinates cannot be parsed from the arguments
     */
    public static Coords of(String[] args) throws InvalidCoordinatesException {
        double altitude = Double.NaN;
        Coords defaultCoords = CoordinateParseUtils.parseVerbatimCoordinates(getRawArguments(args).trim());

        if (defaultCoords == null) {
            Coords possiblePlayerCoords = CoordinateParseUtils.parseVerbatimCoordinates(getRawArguments(selectArray(args, 1)));
            if (possiblePlayerCoords != null) {
                defaultCoords = possiblePlayerCoords;
            }
        }

        if (args.length > 1) {
            Coords possibleHeightCoords = CoordinateParseUtils.parseVerbatimCoordinates(getRawArguments(inverseSelectArray(args, args.length - 1)));
            if (possibleHeightCoords != null) {
                defaultCoords = possibleHeightCoords;
                try {
                    altitude = Double.parseDouble(args[args.length - 1]);
                } catch (Exception ignored) {
                }
            }

            Coords possibleHeightNameCoords = CoordinateParseUtils.parseVerbatimCoordinates(getRawArguments(inverseSelectArray(selectArray(args, 1), selectArray(args, 1).length - 1)));
            if (possibleHeightNameCoords != null) {
                defaultCoords = possibleHeightNameCoords;
                try {
                    altitude = Double.parseDouble(selectArray(args, 1)[selectArray(args, 1).length - 1]);
                } catch (Exception e) {
                    altitude = Double.NaN;
                }
            }
        }

        if (defaultCoords == null) {
            throw new InvalidCoordinatesException();
        }
        return new Coords(defaultCoords.getLat(), defaultCoords.getLon(), altitude);
    }
}
