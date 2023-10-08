package nexus.slime.f3nperm;

import org.bukkit.Bukkit;

public enum BukkitVersion {
    V1_9_NO_R(0, "1.9-SNAPSHOT"),
    V1_9(1, "1.9-R0.1-SNAPSHOT"),
    V1_9_2(2, "1.9.2-R0.1-SNAPSHOT"),
    V1_9_4(3, "1.9.4-R0.1-SNAPSHOT"),
    V1_10(4, "1.10-R0.1-SNAPSHOT"),
    V1_10_2(5, "1.10.2-R0.1-SNAPSHOT"),
    V1_11(6, "1.11-R0.1-SNAPSHOT"),
    V1_11_1(7, "1.11.1-R0.1-SNAPSHOT"),
    V1_11_2(8, "1.11.2-R0.1-SNAPSHOT"),
    V1_12(9, "1.12-R0.1-SNAPSHOT"),
    V1_12_1(10, "1.12.1-R0.1-SNAPSHOT"),
    V1_12_2(11, "1.12.2-R0.1-SNAPSHOT"),
    V1_13(12, "1.13-R0.1-SNAPSHOT"),
    V1_13_1(13, "1.13.1-R0.1-SNAPSHOT"),
    V1_13_2(14, "1.13.2-R0.1-SNAPSHOT"),
    V1_14(15, "1.14-R0.1-SNAPSHOT"),
    V1_14_1(16, "1.14.1-R0.1-SNAPSHOT"),
    V1_14_2(17, "1.14.2-R0.1-SNAPSHOT"),
    V1_14_3_NO_R(18, "1.14.3-SNAPSHOT"),
    V1_14_3(19, "1.14.3-R0.1-SNAPSHOT"),
    V1_14_4(20, "1.14.4-R0.1-SNAPSHOT"),
    V1_15(21, "1.15-R0.1-SNAPSHOT"),
    V1_15_1(22, "1.15.1-R0.1-SNAPSHOT"),
    V1_15_2(23, "1.15.2-R0.1-SNAPSHOT"),
    V1_16_1(24, "1.16.1-R0.1-SNAPSHOT"),
    V1_16_2(25, "1.16.2-R0.1-SNAPSHOT"),
    V1_16_3(26, "1.16.3-R0.1-SNAPSHOT"),
    V1_16_4(27, "1.16.4-R0.1-SNAPSHOT"),
    V1_16_5(28, "1.16.5-R0.1-SNAPSHOT"),
    V1_17(29, "1.17-R0.1-SNAPSHOT"),
    V1_17_1(30, "1.17.1-R0.1-SNAPSHOT"),
    V1_18(31, "1.18-R0.1-SNAPSHOT"),
    V1_18_1(32, "1.18.1-R0.1-SNAPSHOT"),
    V1_18_2(33, "1.18.2-R0.1-SNAPSHOT"),
    V1_19(34, "1.19-R0.1-SNAPSHOT"),
    V1_19_1(35, "1.19.1-R0.1-SNAPSHOT"),
    V1_19_2(36, "1.19.2-R0.1-SNAPSHOT"),
    V1_19_3(37, "1.19.3-R0.1-SNAPSHOT"),
    V1_19_4(38, "1.19.4-R0.1-SNAPSHOT"),
    V1_20(39, "1.20-R0.1-SNAPSHOT"),
    V1_20_1(40, "1.20.1-R0.1-SNAPSHOT"),
    V1_20_2(41, "1.20.2-R0.1-SNAPSHOT");

    public static BukkitVersion fromBukkitVersion() {
        String bukkitVersion = Bukkit.getServer().getBukkitVersion();

        for (BukkitVersion value : values()) {
            if (value.bukkitVersion.equalsIgnoreCase(bukkitVersion)) {
                return value;
            }
        }

        return null;
    }

    private final int order;
    private final String bukkitVersion;

    BukkitVersion(int order, String bukkitVersion) {
        this.order = order;
        this.bukkitVersion = bukkitVersion;
    }

    public boolean isGreaterOrEqualTo(BukkitVersion other) {
        return order >= other.order;
    }

    @Override
    public String toString() {
        return name();
    }
}
