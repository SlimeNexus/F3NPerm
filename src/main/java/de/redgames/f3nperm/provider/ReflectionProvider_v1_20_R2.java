package de.redgames.f3nperm.provider;

import de.redgames.f3nperm.reflection.ReflectionException;
import de.redgames.f3nperm.reflection.Reflections;

public class ReflectionProvider_v1_20_R2 extends ReflectionProvider_v1_20_R1 {
    @Override
    public Object getNetworkManager(Object playerConnection) throws ReflectionException {
        return Reflections.get(playerConnection, "c");
    }

    @Override
    public Object getChannel(Object networkManager) throws ReflectionException {
        return Reflections.getPrivate(networkManager, "n");
    }

    @Override
    public void sendPacket(Object playerConnection, Object packet) throws ReflectionException {
        Reflections.call(playerConnection, "b(net.minecraft.network.protocol.Packet)", packet);
    }
}
