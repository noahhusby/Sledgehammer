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

package com.noahhusby.sledgehammer.common;

import com.noahhusby.sledgehammer.common.exceptions.VersionParseException;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Objects;

/**
 * @author Noah Husby
 * A class represtenting a single version of Sledgehammer
 */
@Getter
@RequiredArgsConstructor
public class SledgehammerVersion implements Comparable<SledgehammerVersion> {
    private final int majorVersion;
    private final int minorVersion;
    private final int patchVersion;
    private final ReleaseType type;
    private final int revision;
    private final int build;

    public static final SledgehammerVersion DEV = new SledgehammerVersion(0, 0, 0, ReleaseType.DEV, 0, 0);

    public SledgehammerVersion(@NonNull String version) throws VersionParseException {
        String[] splitBuild = version.split("\\+");
        if (splitBuild.length == 1) {
            // There is no build present
            build = -1;
        } else {
            try {
                build = Integer.parseInt(splitBuild[1]);
            } catch (NumberFormatException ignored) {
                throw new VersionParseException(String.format("Invalid build number input: %s", version));
            }
        }

        String[] splitRelease = splitBuild[0].split("-");
        if (splitRelease.length == 1) {
            // No predefined release type or revision - assumes release
            type = ReleaseType.RELEASE;
            revision = 0;
        } else {
            String[] splitRevision = splitRelease[1].split("\\.");
            type = ReleaseType.of(splitRevision[0]);
            if (splitRevision.length == 2) {
                // Revision is present
                try {
                    revision = Integer.parseInt(splitRevision[1]);
                } catch (NumberFormatException ignored) {
                    throw new VersionParseException(String.format("Invalid revision number input: %s", version));
                }
            } else {
                revision = 0;
            }
        }

        String[] splitVersion = splitRelease[0].split("\\.");
        if (splitVersion.length < 3) {
            throw new VersionParseException(String.format("Invalid target input: %s", version));
        }
        try {
            majorVersion = Integer.parseInt(splitVersion[0]);
            minorVersion = Integer.parseInt(splitVersion[1]);
            patchVersion = Integer.parseInt(splitVersion[2]);
        } catch (NumberFormatException e) {
            throw new VersionParseException(String.format("Invalid target input: %s", version));
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(majorVersion, minorVersion, patchVersion, revision, build);
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof SledgehammerVersion)) {
            return false;
        }
        return this.compareTo((SledgehammerVersion) other) == 0;
    }

    @Override
    public int compareTo(SledgehammerVersion other) {
        if (other == null) {
            return Integer.MAX_VALUE;
        }

        int majorCompare = this.majorVersion - other.majorVersion;
        if (majorCompare != 0) {
            return majorCompare;
        }

        int minorCompare = this.minorVersion - other.minorVersion;
        if (minorCompare != 0) {
            return minorCompare;
        }

        int patchCompare = this.patchVersion - other.patchVersion;
        if (patchCompare != 0) {
            return patchCompare;
        }

        int typeCompare = other.type.ordinal() - this.type.ordinal();
        if (typeCompare != 0) {
            return typeCompare;
        }

        int revisionCompare = this.revision - other.revision;
        if (revisionCompare != 0) {
            return revisionCompare;
        }

        return this.build - other.build;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(String.format("%d.%d.%d", majorVersion, minorVersion, patchVersion));
        if (type != ReleaseType.RELEASE) {
            builder.append("-").append(type.name).append(".").append(revision);
        }
        if (build != -1) {
            builder.append("+").append(build);
        }
        return builder.toString();
    }

    /**
     * Checks whether another version is newer than the local version
     *
     * @param other {@link SledgehammerVersion}
     * @return True if compared version is newer, false if not
     */
    public boolean isNewer(SledgehammerVersion other) {
        return this.compareTo(other) > 0;
    }

    /**
     * Checks whether another version is older than the local version
     *
     * @param other {@link SledgehammerVersion}
     * @return True if compared version is older, false if not
     */
    public boolean isOlder(SledgehammerVersion other) {
        return this.compareTo(other) < 0;
    }

    /**
     * Checks whether another version is the same as the local version
     *
     * @param other {@link SledgehammerVersion}
     * @return True if compared version is the same, false if not
     */
    public boolean isSame(SledgehammerVersion other) {
        return this.compareTo(other) == 0;
    }

    @RequiredArgsConstructor
    public enum ReleaseType {
        RELEASE(""),

        RELEASE_CANDIDATE("rc"),

        BETA("beta"),

        ALPHA("alpha"),

        DEV("dev");

        private final String name;

        public static ReleaseType of(String name) {
            return Arrays.stream(ReleaseType.values())
                    .filter(t -> t.name.equals(name))
                    .findFirst()
                    .orElse(DEV);
        }
    }
}
