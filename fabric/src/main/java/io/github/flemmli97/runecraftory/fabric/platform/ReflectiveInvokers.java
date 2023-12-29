package io.github.flemmli97.runecraftory.fabric.platform;

import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.entity.schedule.Activity;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ReflectiveInvokers {

    public static Activity activity(String name) {
        try {
            Constructor<Activity> o = Activity.class.getDeclaredConstructor(String.class);
            o.setAccessible(true);
            Activity ret = o.newInstance(name);
            o.setAccessible(false);
            return ret;
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
                 InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static SimpleParticleType simpleParticleType(boolean overrideLimiter) {
        try {
            Constructor<SimpleParticleType> o = SimpleParticleType.class.getDeclaredConstructor(boolean.class);
            o.setAccessible(true);
            SimpleParticleType ret = o.newInstance(overrideLimiter);
            o.setAccessible(false);
            return ret;
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
                 InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}
