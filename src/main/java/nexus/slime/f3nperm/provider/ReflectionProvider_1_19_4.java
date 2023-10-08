package nexus.slime.f3nperm.provider;

import nexus.slime.f3nperm.reflection.ReflectionException;
import nexus.slime.f3nperm.reflection.Reflections;

public class ReflectionProvider_1_19_4 extends ReflectionProvider_1_19 {
    @Override
    public Object getPlayerConnection(Object entityPlayer) throws ReflectionException {
        return Reflections.get(entityPlayer, "b");
    }

    @Override
    public Object getNetworkManager(Object playerConnection) throws ReflectionException {
        return Reflections.getPrivate(playerConnection, "h");
    }
}
