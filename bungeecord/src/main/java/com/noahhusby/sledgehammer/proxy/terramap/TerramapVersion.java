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

package com.noahhusby.sledgehammer.proxy.terramap;

import com.google.common.base.Strings;
import com.noahhusby.sledgehammer.proxy.Sledgehammer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.Arrays;
import java.util.Map;

/**
 * Represents a version of Terramap.
 * The format for Terramap version number are as follows:</br>
 * [marjor target].[minor target].[build target](-[release type][release build](.[release revision]))(-dev)</br>
 * major target, minor target, build target, release build, and release revision are integers. </br>
 * Release type is one of: alpha, beta, rc or is absent. </br>
 * Parts in parentheses can be omitted (will assume 0 for numbers and normal release for release type.
 * -dev is only present on dev builds that are not available to the public.
 * In all places this class would be use, a null version means Terramap is not installed
 * <p>
 * A ${version} version means the mod is running in a development environment.
 * <p>
 * This file was originally written for Terramap.
 *
 * @author SmylerMC
 */
public class TerramapVersion implements Comparable<TerramapVersion> {

    public final int majorTarget;
    public final int minorTarget;
    public final int buildTarget;

    public final ReleaseType releaseType;

    public final int build;
    public final int revision;

    public final boolean devBuild;
    public final boolean devRun;

    public final String mcVersion;

    /**
     * Creates a new version object
     *
     * @param majorTarget major version number of the target version
     * @param minorTarget minor version number of the target version
     * @param buildTarget build version number of the target version
     * @param releaseType release type (release, release candidate, beta, alpha, or development)
     * @param build       build number of the release part specific part of the version
     * @param revision    revision number of the release part specific part of the version
     * @param devBuild    whether this is a development build
     * @param mcVersion   the minecraft version String
     */
    public TerramapVersion(
            int majorTarget,
            int minorTarget,
            int buildTarget,
            ReleaseType releaseType,
            int build,
            int revision,
            boolean devBuild,
            String mcVersion) {
        super();
        this.majorTarget = majorTarget;
        this.minorTarget = minorTarget;
        this.buildTarget = buildTarget;
        this.releaseType = releaseType;
        this.build = build;
        this.revision = revision;
        this.devBuild = devBuild;
        this.devRun = false;
        this.mcVersion = mcVersion;
    }

    /**
     * @param majorTarget major version number of the target version
     * @param minorTarget minor version number of the target version
     * @param buildTarget build version number of the target version
     * @param type        release type (release, release candidate, beta, alpha, or development)
     * @param build       build number of the release part specific part of the version
     * @param revision    revision number of the release part specific part of the version
     */
    public TerramapVersion(int majorTarget, int minorTarget, int buildTarget, ReleaseType type, int build, int revision) {
        this(majorTarget, minorTarget, buildTarget, type, build, revision, false, "");
    }

    public TerramapVersion(int majorTarget, int minorTarget, int buildTarget, ReleaseType type, int build) {
        this(majorTarget, minorTarget, buildTarget, type, build, 0, false, "");
    }

    public TerramapVersion(int majorTarget, int minorTarget, int buildTarget) {
        this(majorTarget, minorTarget, buildTarget, ReleaseType.RELEASE, 0, 0, false, "");
    }

    /**
     * Creates a version object by parsing a version String
     *
     * @param versionString - version to parse
     * @throws InvalidVersionString if the given string is not a valid version number.
     *                              See other constructors to safely create version numbers if you don't need to do it by parsing a version String.
     */
    public TerramapVersion(String versionString) throws InvalidVersionString {
        if ("${version}".equals(versionString)) {
            this.majorTarget = this.minorTarget = this.buildTarget = this.build = this.revision = 0;
            this.devBuild = false;
            this.devRun = true;
            this.releaseType = ReleaseType.DEV;
            this.mcVersion = "";
        } else {
            String[] versions = versionString.split("\\_");
            String mcVersion;
            if (versions.length == 2) {
                mcVersion = versions[1];
            } else if (versions.length == 1) {
                mcVersion = "";
            } else {
                throw new InvalidVersionString("Invalid version string " + versionString);
            }
            this.mcVersion = mcVersion;
            String[] parts = versions[0].split("-");
            if (parts.length > 3) {
                throw new InvalidVersionString("Invalid version string " + versionString);
            }
            if (parts.length > 0) {
                if ("dev".equals(parts[parts.length - 1])) {
                    this.devBuild = true;
                    parts = Arrays.copyOfRange(parts, 0, parts.length - 1);
                } else {
                    this.devBuild = false;
                }
                String[] target = parts[0].split("\\.");
                if (target.length != 3) {
                    throw new InvalidVersionString("Invalid target version " + parts[0]);
                }
                devRun = false;
                try {
                    this.majorTarget = Integer.parseInt(target[0]);
                } catch (NumberFormatException e) {
                    throw new InvalidVersionString("Invalid target major version: " + target[0]);
                }
                try {
                    this.minorTarget = Integer.parseInt(target[1]);
                } catch (NumberFormatException e) {
                    throw new InvalidVersionString("Invalid target minor version: " + target[1]);
                }
                try {
                    this.buildTarget = Integer.parseInt(target[2]);
                } catch (NumberFormatException e) {
                    throw new InvalidVersionString("Invalid target build version: " + target[2]);
                }
                if (parts.length > 1) {
                    for (ReleaseType type : ReleaseType.values()) {
                        if (type.equals(ReleaseType.RELEASE)) {
                            continue;
                        }
                        if (parts[1].startsWith(type.name)) {
                            this.releaseType = type;
                            parts[1] = parts[1].substring(type.name.length());
                            String[] build = parts[1].split("\\.");
                            if (build.length > 0) {
                                try {
                                    this.build = Integer.parseInt(build[0]);
                                } catch (NumberFormatException e) {
                                    throw new InvalidVersionString("Invalid build version: " + build[0]);
                                }
                                if (build.length > 1) {
                                    try {
                                        this.revision = Integer.parseInt(build[1]);
                                    } catch (NumberFormatException e) {
                                        throw new InvalidVersionString("Invalid revision version: " + build[1]);
                                    }
                                } else {
                                    this.revision = 0;
                                }
                            } else {
                                throw new InvalidVersionString("Invalid target version number: " + parts[1]);
                            }
                            return;
                        }
                    }
                    throw new InvalidVersionString("Invalid target release type: " + parts[1]);
                } else {
                    this.releaseType = ReleaseType.RELEASE;
                    this.build = this.minorTarget;
                    this.revision = this.buildTarget;
                }
            } else {
                throw new InvalidVersionString("Empty version string");
            }
        }
    }

    /**
     * Gets the given player's Terramap version.
     * The player needs to have logged onto a Forge server at least once for this to work.
     * If it's not the case then this will return null, even though that may be wrong.
     *
     * @return the player Terramap version, or null if they don't have it
     */
    public static TerramapVersion getClientVersion(ProxiedPlayer player) {
        Map<String, String> modList = player.getModList();
        TerramapVersion version = null;
        String remoteVersion = modList.get(TerramapModule.TERRAMAP_MODID);
        if (remoteVersion != null) {
            try {
                version = new TerramapVersion(remoteVersion);
            } catch (InvalidVersionString e) {
                Sledgehammer.logger.warning("Failed to parse a client's Terramap version: " + remoteVersion + " : " + e.getLocalizedMessage());
            }
        }
        return version;
    }

    /**
     * Returns a version String for this version
     */
    @Override
    public String toString() {
        String str = this.getTerramapVersionString();
        if (!Strings.isNullOrEmpty(this.mcVersion)) {
            str += "_" + this.mcVersion;
        }
        return str;
    }

    public String getTerramapVersionString() {
        if (this.isDev()) {
            return "${version}";
        }
        String str = "";
        str += this.majorTarget;
        str += "." + this.minorTarget;
        str += "." + this.buildTarget;
        if (!this.releaseType.equals(ReleaseType.RELEASE)) {
            str += "-" + this.releaseType.name;
            str += this.build;
            if (this.revision != 0) {
                str += "." + this.revision;
            }
        }
        if (this.devBuild) {
            str += "-dev";
        }
        return str;
    }

    /**
     * @return true if this version is running in a dev environment (i.e? it does not have a version number)
     */
    public boolean isDev() {
        return this.devRun || this.releaseType.equals(ReleaseType.DEV);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + this.build;
        result = prime * result + this.buildTarget;
        result = prime * result + (this.devBuild ? 1231 : 1237);
        result = prime * result + (this.devRun ? 1231 : 1237);
        result = prime * result + this.majorTarget;
        result = prime * result + this.minorTarget;
        result = prime * result + ((this.releaseType == null) ? 0 : releaseType.hashCode());
        result = prime * result + this.revision;
        return result;
    }

    /**
     * True if the other object is a version equal to this one
     */
    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (!(other instanceof TerramapVersion)) {
            return false;
        }
        return this.compareTo((TerramapVersion) other) == 0;
    }

    /**
     * Returns a positive integer if this version is newer than the other,
     * 0 if they are the same and a negative number otherwise.
     */
    @Override
    public int compareTo(TerramapVersion other) {

        if (other == null) {
            // Null means not installed, so we are always ahead
            return Integer.MAX_VALUE;
        }

        if (this.isDev()) {
            if (other.isDev()) {
                return 0;
            } else {
                return Integer.MAX_VALUE;
            }
        }

        int majorComp = this.majorTarget - other.majorTarget;
        if (majorComp != 0) {
            return majorComp;
        }

        int minorComp = this.minorTarget - other.minorTarget;
        if (minorComp != 0) {
            return minorComp;
        }

        int buildComp = this.buildTarget - other.buildTarget;
        if (buildComp != 0) {
            return buildComp;
        }

        int typeComp = this.releaseType.priority - other.releaseType.priority;
        if (typeComp != 0) {
            return typeComp;
        }

        int rComp = this.build - other.build;
        if (rComp != 0) {
            return rComp;
        }

        return this.revision - other.revision;

    }

    /**
     * Compares this version with another version object
     *
     * @param other version, can be null to specify that Terramap is not installed. Null will always be the oldest.
     * @return true if this version is newer or equal to the other, false otherwise
     */
    public boolean isNewerOrSame(TerramapVersion other) {
        return this.compareTo(other) >= 0;
    }

    /**
     * Compares this version with another version object
     *
     * @param other version, can be null to specify that Terramap is not installed. Null will always be the oldest.
     * @return true if this version is strictly newer than the other, false otherwise
     */
    public boolean isNewer(TerramapVersion other) {
        return this.compareTo(other) > 0;
    }

    /**
     * Compares this version with another version object
     *
     * @param other version, can be null to specify that Terramap is not installed. Null will always be the oldest.
     * @return true if this version is older or equal to the other, false otherwise
     */
    public boolean isOlderOrSame(TerramapVersion other) {
        return this.compareTo(other) <= 0;
    }

    /**
     * Compares this version with another version object
     *
     * @param other version, can be null to specify that Terramap is not installed. Null will always be the oldest.
     * @return true if this version is strictly older than the other, false otherwise
     */
    public boolean isOlder(TerramapVersion other) {
        return this.compareTo(other) < 0;
    }

    /**
     * @return whether this version depends on Terra++ or Terra121
     */
    public TerraDependency getTerraDependency() {
        if (this.isNewer(TerramapModule.OLDEST_TERRA121_TERRAMAP_VERSION)) {
            return TerraDependency.TERRAPLUSPLUS;
        } else {
            return TerraDependency.TERRA121;
        }
    }

    /**
     * Different types of Terramap releases
     *
     * @author SmylerMC
     */
    public enum ReleaseType {

        RELEASE("", 4),
        RELEASE_CANDIDATE("rc", 3),
        BETA("beta", 2),
        ALPHA("alpha", 1),
        DEV("dev", 0); // Running from dev environment, no actual version number

        public final String name;
        public final int priority;

        ReleaseType(String name, int priority) {
            this.name = name;
            this.priority = priority;
        }

    }

    /**
     * Terramap's version dependent dependencies
     *
     * @author SmylerMC
     */
    public enum TerraDependency {
        TERRA121("terra121"), TERRAPLUSPLUS("terraplusplus");

        public final String MODID;

        TerraDependency(String modid) {
            this.MODID = modid;
        }
    }

    public static class InvalidVersionString extends Exception {

        public InvalidVersionString(String message) {
            super(message);
        }

    }

}
