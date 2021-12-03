package de.redgames.f3nperm;

import org.bukkit.entity.Player;

public abstract class Reflector {
    private final Class<?> entityClass;
    private final Class<?> packetClass;
    private final Class<?> packetPlayOutEntityStatusClass;

    public Reflector() {
        try {
            this.entityClass = Class.forName(getEntityClassName());
            this.packetClass = Class.forName(getPacketClassName());
            this.packetPlayOutEntityStatusClass = Class.forName(getPacketPlayOutEntityStatusClassName());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendEntityStatus(Player p) {
        try {
            Object entityPlayer = p.getClass()
                    .getDeclaredMethod(getGetHandleMethodName())
                    .invoke(p);

            Object playerConnection = entityPlayer.getClass()
                    .getDeclaredField(getPlayerConnectionFieldName())
                    .get(entityPlayer);

            Object packet = packetPlayOutEntityStatusClass
                    .getConstructor(entityClass, byte.class)
                    .newInstance(entityPlayer, getStatusByte());

            playerConnection.getClass()
                    .getDeclaredMethod(getSendPacketMethodName(), packetClass)
                    .invoke(playerConnection, packet);
        } catch (Throwable e) {
            throw new RuntimeException("Error while sending entity status 28", e);
        }
    }

    protected abstract String getNamespace();
    protected abstract byte getStatusByte();
    protected abstract String getEntityClassName();
    protected abstract String getPacketClassName();
    protected abstract String getPacketPlayOutEntityStatusClassName();
    protected abstract String getGetHandleMethodName();
    protected abstract String getPlayerConnectionFieldName();
    protected abstract String getSendPacketMethodName();
}
