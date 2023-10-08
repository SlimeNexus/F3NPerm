package de.redgames.f3nperm.provider;

import de.redgames.f3nperm.OpPermissionLevel;
import de.redgames.f3nperm.reflection.ReflectionException;
import de.redgames.f3nperm.reflection.Reflections;
import io.netty.channel.Channel;
import org.bukkit.entity.Player;

public class ReflectionProvider_1_9 extends NettyProvider {
    @Override
    public void sendPacket(Player player) {
        OpPermissionLevel level = getPlugin().getF3NPermPermissionLevel(player);

        try {
            Object entityPlayer = getEntityPlayer(player);
            Object playerConnection = getPlayerConnection(entityPlayer);

            Object packet = makeStatusPacket(entityPlayer, level.toStatusByte());
            sendPacket(playerConnection, packet);
        } catch (ReflectionException e) {
            throw new ProviderException("Could not send packet!", e);
        }
    }

    @Override
    public void adjustPacket(Player player, Object packet) {
        try {
            if (!isStatusPacket(packet)) {
                return;
            }

            int entity = getStatusPacketEntity(packet);

            if (entity != player.getEntityId()) {
                return;
            }

            OpPermissionLevel currentLevel = getStatusPacketStatus(packet);

            if (currentLevel == null) {
                return;
            }

            OpPermissionLevel level = getPlugin().getF3NPermPermissionLevel(player);
            setStatusPacketStatus(packet, level);
        } catch (ReflectionException e) {
            throw new ProviderException("Could not adjust packet!", e);
        }
    }

    @Override
    public Channel getChannel(Player player) {
        try {
            Object entityPlayer = getEntityPlayer(player);
            Object playerConnection = getPlayerConnection(entityPlayer);
            Object networkManager = getNetworkManager(playerConnection);

            return (Channel) getChannel(networkManager);
        } catch (ReflectionException e) {
            throw new ProviderException("Could not retrieve channel for " + player.getName() + "!", e);
        }
    }

    public Object getEntityPlayer(Player player) throws ReflectionException {
        return Reflections.call(player, "getHandle()");
    }

    public Object getPlayerConnection(Object entityPlayer) throws ReflectionException {
        return Reflections.get(entityPlayer, "playerConnection");
    }

    public Object getNetworkManager(Object playerConnection) throws ReflectionException {
        return Reflections.getPrivate(playerConnection, "networkManager");
    }

    public Object getChannel(Object networkManager) throws ReflectionException {
        return Reflections.getPrivate(networkManager, "channel");
    }

    public int getStatusPacketEntity(Object packet) throws ReflectionException {
        return (Integer) Reflections.getPrivate(packet, "a");
    }

    public OpPermissionLevel getStatusPacketStatus(Object packet) throws ReflectionException {
        return OpPermissionLevel.fromStatusByte((Byte) Reflections.getPrivate(packet, "b"));
    }

    public void setStatusPacketStatus(Object packet, OpPermissionLevel level) throws ReflectionException {
        Reflections.setPrivate(packet, "b", level.toStatusByte());
    }

    public Object makeStatusPacket(Object entityPlayer, byte status) throws ReflectionException {
        return Reflections.make("{nms}PacketPlayOutEntityStatus({nms}Entity,byte)", entityPlayer, status);
    }

    public boolean isStatusPacket(Object packet) throws ReflectionException {
        return packet.getClass().getSimpleName().equals("PacketPlayOutEntityStatus");
    }

    public void sendPacket(Object playerConnection, Object packet) throws ReflectionException {
        Reflections.call(playerConnection, "sendPacket({nms}Packet)", packet);
    }
}
