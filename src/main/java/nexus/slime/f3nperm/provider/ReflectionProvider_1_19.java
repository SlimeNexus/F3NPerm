package nexus.slime.f3nperm.provider;

import nexus.slime.f3nperm.reflection.ReflectionException;
import nexus.slime.f3nperm.reflection.Reflections;

public class ReflectionProvider_1_19 extends ReflectionProvider_1_18_2 {
    @Override
    public Object getNetworkManager(Object playerConnection) throws ReflectionException {
        return Reflections.getPrivate(playerConnection, "b");
    }
}
