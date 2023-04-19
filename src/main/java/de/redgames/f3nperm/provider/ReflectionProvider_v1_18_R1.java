package de.redgames.f3nperm.provider;

import de.redgames.f3nperm.reflection.ReflectionException;
import de.redgames.f3nperm.reflection.Reflections;

public class ReflectionProvider_v1_18_R1 extends ReflectionProvider_v1_17_R1 {
    @Override
    public void sendPacket(Object playerConnection, Object packet) throws ReflectionException {
        Reflections.call(playerConnection, "a(net.minecraft.network.protocol.Packet)", packet);
    }
}
