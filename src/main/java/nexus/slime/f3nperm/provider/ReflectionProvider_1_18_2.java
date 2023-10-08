package nexus.slime.f3nperm.provider;

import nexus.slime.f3nperm.reflection.ReflectionException;
import nexus.slime.f3nperm.reflection.Reflections;

public class ReflectionProvider_1_18_2 extends ReflectionProvider_1_18 {
    @Override
    public Object getChannel(Object networkManager) throws ReflectionException {
        return Reflections.getPrivate(networkManager, "m");
    }
}
