package de.redgames.f3nperm.provider;

import de.redgames.f3nperm.reflection.ReflectionException;
import de.redgames.f3nperm.reflection.Reflections;

public class ReflectionProvider_1_18 extends ReflectionProvider_1_17 {
    @Override
    public void sendPacket(Object playerConnection, Object packet) throws ReflectionException {
        Reflections.call(playerConnection, "a(net.minecraft.network.protocol.Packet)", packet);
    }
}
