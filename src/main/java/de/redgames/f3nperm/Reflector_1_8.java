package de.redgames.f3nperm;

import org.bukkit.Bukkit;

public class Reflector_1_8 extends Reflector {
    @Override
    protected String getNamespace() {
        try {
            return Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    protected byte getStatusByte() {
        return 26;
    }

    @Override
    protected String getEntityClassName() {
        return "net.minecraft.server." + getNamespace() + ".Entity";
    }

    @Override
    protected String getPacketClassName() {
        return "net.minecraft.server." + getNamespace() + ".Packet";
    }

    @Override
    protected String getPacketPlayOutEntityStatusClassName() {
        return "net.minecraft.server." + getNamespace() + ".PacketPlayOutEntityStatus";
    }

    @Override
    protected String getGetHandleMethodName() {
        return "getHandle";
    }

    @Override
    protected String getPlayerConnectionFieldName() {
        return "playerConnection";
    }

    @Override
    protected String getSendPacketMethodName() {
        return "sendPacket";
    }
}
