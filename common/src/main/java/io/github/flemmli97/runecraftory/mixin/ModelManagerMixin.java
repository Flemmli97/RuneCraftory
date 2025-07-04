package io.github.flemmli97.runecraftory.mixin;

import com.google.common.collect.Multimap;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.flemmli97.runecraftory.RuneCraftory;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.resources.model.ModelResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Collection;
import java.util.Map;

//Make Texture warning from this mod go away
@Mixin(ModelManager.class)
public abstract class ModelManagerMixin {

    @SuppressWarnings({"unchecked", "rawtypes"})
    @WrapOperation(method = "loadModels",
            at = @At(value = "INVOKE", target = "Lcom/google/common/collect/Multimap;asMap()Ljava/util/Map;"),
            remap = false)
    private static <K, V> Map<K, Collection<V>> supresssWarning(Multimap instance, Operation<Map<K, Collection<V>>> original) {
        instance.keySet().removeIf(k -> ((ModelResourceLocation) k).id().getNamespace().startsWith(RuneCraftory.MODID));
        return original.call(instance);
    }
}
