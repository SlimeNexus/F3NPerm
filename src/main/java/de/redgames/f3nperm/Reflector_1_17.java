package de.redgames.f3nperm;

public class Reflector_1_17 extends Reflector_1_8 {
    @Override
    protected String getPacketPlayOutEntityStatusClassName() {
        return "net.minecraft.network.protocol.game.PacketPlayOutEntityStatus";
    }

    @Override
    protected String getEntityClassName() {
        return "net.minecraft.world.entity.Entity";
    }

    @Override
    protected String getPacketClassName() {
        return "net.minecraft.network.protocol.Packet";
    }

    @Override
    protected String getPlayerConnectionFieldName() {
        return "b";
    }
}
