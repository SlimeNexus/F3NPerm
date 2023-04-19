package de.redgames.f3nperm.provider;

import de.redgames.f3nperm.reflection.ReflectionException;
import de.redgames.f3nperm.reflection.Reflections;

public class ReflectionProvider_v1_19_R1 extends ReflectionProvider_v1_18_R2 {
    @Override
    public Object getNetworkManager(Object playerConnection) throws ReflectionException {
        return Reflections.getPrivate(playerConnection, "b");
    }
}
