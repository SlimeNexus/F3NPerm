package de.redgames.f3nperm.provider;

import de.redgames.f3nperm.reflection.ReflectionException;
import de.redgames.f3nperm.reflection.Reflections;

public class ReflectionProvider_v1_18_R2 extends ReflectionProvider_v1_18_R1 {
    @Override
    public Object getChannel(Object networkManager) throws ReflectionException {
        return Reflections.getPrivate(networkManager, "m");
    }
}
