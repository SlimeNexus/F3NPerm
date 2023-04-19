package de.redgames.f3nperm;

import org.bukkit.Bukkit;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ServerVersion {
    public static final ServerVersion v_1_17 = new ServerVersion(1, 17, 0);
    public static final ServerVersion v_1_18 = new ServerVersion(1, 18, 0);
    public static final ServerVersion v_1_18_2 = new ServerVersion(1, 18, 2);
    public static final ServerVersion v_1_19_1 = new ServerVersion(1, 19, 1);

    private static final Pattern PACKAGE_PATTERN = Pattern.compile("v(\\d+)_(\\d+)_R(\\d+)");

    public static ServerVersion fromBukkitVersion() {
        String packageName;

        try {
            packageName = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        } catch (Exception e) {
            return null;
        }

        Matcher matcher = PACKAGE_PATTERN.matcher(packageName);

        if (!matcher.find()) {
            return null;
        }

        int major = Integer.parseInt(matcher.group(1));
        int minor = Integer.parseInt(matcher.group(2));
        int patch = Integer.parseInt(matcher.group(3));

        return new ServerVersion(major, minor, patch);
    }

    private final int major;
    private final int minor;
    private final int patch;

    public ServerVersion(int major, int minor, int patch) {
        this.major = major;
        this.minor = minor;
        this.patch = patch;
    }

    public boolean isLowerThan(ServerVersion version) {
        if (getMajor() < version.getMajor()) return true;
        if (getMajor() > version.getMajor()) return false;
        if (getMinor() < version.getMinor()) return true;
        if (getMinor() > version.getMinor()) return false;
        return getPatch() < version.getPatch();
    }

    public int getMajor() {
        return major;
    }

    public int getMinor() {
        return minor;
    }

    public int getPatch() {
        return patch;
    }

    @Override
    public String toString() {
        return "v" + major + "_" + minor + "_R" + patch;
    }
}
