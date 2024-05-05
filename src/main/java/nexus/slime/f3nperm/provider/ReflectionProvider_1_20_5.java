package nexus.slime.f3nperm.provider;

import nexus.slime.f3nperm.OpPermissionLevel;
import nexus.slime.f3nperm.reflection.ReflectionException;
import nexus.slime.f3nperm.reflection.Reflections;
import org.bukkit.entity.Player;

public class ReflectionProvider_1_20_5 extends ReflectionProvider_1_20_2 {
    @Override
    public int getStatusPacketEntity(Object packet) throws ReflectionException {
        return (Integer) Reflections.getPrivate(packet, "b");
    }

    @Override
    public OpPermissionLevel getStatusPacketStatus(Object packet) throws ReflectionException {
        return OpPermissionLevel.fromStatusByte((Byte) Reflections.getPrivate(packet, "c"));
    }

    @Override
    public void setStatusPacketStatus(Object packet, OpPermissionLevel level) throws ReflectionException {
        Reflections.setPrivate(packet, "c", level.toStatusByte());
    }

    @Override
    public Object getNetworkManager(Object playerConnection) throws ReflectionException {
        return Reflections.getPrivate(playerConnection.getClass().getSuperclass(), playerConnection, "e");
    }

    @Override
    public void sendPacket(Object networkManager, Object packet) throws ReflectionException {
        Reflections.call(networkManager, "a(net.minecraft.network.protocol.Packet)", packet);
    }

    @Override
    public void sendPacket(Player player) {
        OpPermissionLevel level = getPlugin().getF3NPermPermissionLevel(player);

        try {
            Object entityPlayer = getEntityPlayer(player);
            Object playerConnection = getPlayerConnection(entityPlayer);
            Object networkManager = getNetworkManager(playerConnection);

            Object packet = makeStatusPacket(entityPlayer, level.toStatusByte());
            sendPacket(networkManager, packet);
        } catch (ReflectionException e) {
            throw new ProviderException("Could not send packet!", e);
        }
    }
}
