package de.redgames.f3nperm.reflection;

import org.bukkit.Bukkit;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Reflections {
    // Prevent instantiation
    private Reflections() {}

    private static String versionPackageName;

    private static String getVersionPackageName() {
        if (versionPackageName == null) {
            versionPackageName = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        }

        return versionPackageName;
    }

    // -------------
    // Classes
    // -------------

    private static final Map<String, Class<?>> primitives = new HashMap<String, Class<?>>() {{
        put("int", Integer.TYPE);
        put("byte", Byte.TYPE);
        put("short", Short.TYPE);
        put("long", Long.TYPE);
        put("float", Float.TYPE);
        put("double", Double.TYPE);
        put("boolean", Boolean.TYPE);
        put("char", Character.TYPE);
    }};

    public static Class<?> resolve(String className) throws ReflectionException {
        Class<?> primitiveClass = primitives.get(className);

        if (primitiveClass != null) {
            return primitiveClass;
        }

        String version = getVersionPackageName();
        String lookup = className
                .replace("{nms}", "net.minecraft.server." + version + ".")
                .replace("{obc}", "org.bukkit.craftbukkit." + version + ".");

        try {
            return Class.forName(lookup);
        } catch (ClassNotFoundException e) {
            throw new ReflectionException("Class '" + className + "' could not be resolved!", e);
        }
    }

    // -------------
    // Constructors
    // -------------

    public static Object make(String constructorDescription, Object... params) throws ReflectionException {
        Constructor<?> constructor = findConstructor(constructorDescription, false);

        try {
            return constructor.newInstance(params);
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new ReflectionException("Could not invoke constructor " + constructorDescription, e);
        }
    }

    private static Constructor<?> findConstructor(String constructorDescription, boolean isPrivate) throws ReflectionException {
        try {
            if (constructorDescription.endsWith("()")) {
                String className = constructorDescription.substring(0, constructorDescription.length() - 2);
                Class<?> clazz = resolve(className);

                if (isPrivate) {
                    return clazz.getDeclaredConstructor();
                } else {
                    return clazz.getConstructor();
                }
            }

            int startingBracket = constructorDescription.indexOf('(');
            int endingBracket = constructorDescription.indexOf(')');

            if (startingBracket == -1 || endingBracket == -1) {
                throw new IllegalArgumentException("Constructor declaration must contain start and end brackets!");
            }

            String className = constructorDescription.substring(0, startingBracket);
            Class<?> clazz = resolve(className);
            List<Class<?>> params = new ArrayList<>();

            int from = startingBracket + 1;
            int to = constructorDescription.indexOf(',');

            while (to != -1) {
                String paramName = constructorDescription.substring(from, to);
                params.add(resolve(paramName));

                from = to + 1;
                to = constructorDescription.indexOf(',', to + 1);
            }

            String paramName = constructorDescription.substring(from, endingBracket);
            params.add(resolve(paramName));

            if (isPrivate) {
                return clazz.getDeclaredConstructor(params.toArray(new Class[0]));
            } else {
                return clazz.getConstructor(params.toArray(new Class[0]));
            }
        } catch (NoSuchMethodException e) {
            throw new ReflectionException("Could not find constructor!", e);
        }
    }

    // -------------
    // Methods
    // -------------

    public static Object call(Object target, String methodDescription, Object... parameters) throws ReflectionException {
        Class<?> clazz = target.getClass();
        Method method = findMethod(clazz, methodDescription, false);

        try {
            return method.invoke(target, parameters);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new ReflectionException("Could not call method '" + methodDescription + "' on '" + clazz.getCanonicalName() + "'!");
        }
    }

    private static Method findMethod(Class<?> clazz, String methodDescription, boolean isPrivate) throws ReflectionException {
        try {
            if (methodDescription.endsWith("()")) {
                String methodName = methodDescription.substring(0, methodDescription.length() - 2);

                if (isPrivate) {
                    return clazz.getDeclaredMethod(methodName);
                } else {
                    return clazz.getMethod(methodName);
                }
            }

            int startingBracket = methodDescription.indexOf('(');
            int endingBracket = methodDescription.indexOf(')');

            if (startingBracket == -1 || endingBracket == -1) {
                throw new IllegalArgumentException("Method declaration must contain start and end brackets!");
            }

            String methodName = methodDescription.substring(0, startingBracket);
            List<Class<?>> params = new ArrayList<>();

            int from = startingBracket + 1;
            int to = methodName.indexOf(',');

            while (to != -1) {
                String paramName = methodDescription.substring(from, to);
                params.add(resolve(paramName));

                from = to + 1;
                to = methodName.indexOf(',', to + 1);
            }

            String paramName = methodDescription.substring(from, endingBracket);
            params.add(resolve(paramName));

            if (isPrivate) {
                return clazz.getDeclaredMethod(methodName, params.toArray(new Class[0]));
            } else {
                return clazz.getMethod(methodName, params.toArray(new Class[0]));
            }
        } catch (NoSuchMethodException e) {
            throw new ReflectionException("Could not find method!", e);
        }
    }

    // -------------
    // Fields
    // -------------

    public static Object get(Object target, String name) throws ReflectionException {
        Class<?> clazz = target.getClass();

        try {
            Field field = clazz.getField(name);
            return field.get(target);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new ReflectionException("Could not access field " + name + " on " + clazz.getCanonicalName(), e);
        }
    }

    public static Object getPrivate(Object target, String name) throws ReflectionException {
        Class<?> clazz = target.getClass();

        try {
            Field field = clazz.getDeclaredField(name);
            field.setAccessible(true);
            return field.get(target);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new ReflectionException("Could not access field " + name + " on " + clazz.getCanonicalName(), e);
        }
    }

    public static void setPrivate(Object target, String name, Object value) throws ReflectionException {
        Class<?> clazz = target.getClass();

        try {
            Field field = clazz.getDeclaredField(name);
            field.setAccessible(true);
            field.set(target, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new ReflectionException("Could not write field " + name + " on " + clazz.getCanonicalName(), e);
        }
    }
}
