package nexus.slime.f3nperm.provider;

import nexus.slime.f3nperm.reflection.ReflectionException;
import nexus.slime.f3nperm.reflection.Reflections;

public class ReflectionProvider_1_17 extends ReflectionProvider_1_9 {
    @Override
    public Object getPlayerConnection(Object entityPlayer) throws ReflectionException {
        return Reflections.getPrivate(entityPlayer, "b");
    }

    @Override
    public Object getNetworkManager(Object playerConnection) throws ReflectionException {
        return Reflections.getPrivate(playerConnection, "a");
    }

    @Override
    public Object getChannel(Object networkManager) throws ReflectionException {
        return Reflections.getPrivate(networkManager, "k");
    }

    @Override
    public Object makeStatusPacket(Object entityPlayer, byte status) throws ReflectionException {
        return Reflections.make("net.minecraft.network.protocol.game.PacketPlayOutEntityStatus(net.minecraft.world.entity.Entity,byte)", entityPlayer, status);
    }

    @Override
    public boolean isStatusPacket(Object packet) throws ReflectionException {
        return Reflections.resolve("net.minecraft.network.protocol.game.PacketPlayOutEntityStatus").isInstance(packet);
    }

    @Override
    public void sendPacket(Object playerConnection, Object packet) throws ReflectionException {
        Reflections.call(playerConnection, "sendPacket(net.minecraft.network.protocol.Packet)", packet);
    }
}
