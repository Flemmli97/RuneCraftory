package io.github.flemmli97.runecraftory.mixin;

import net.minecraft.client.resources.model.ModelManager;
import org.spongepowered.asm.mixin.Mixin;

//Make Texture warning from this mod go away
@Mixin(ModelManager.class)
public abstract class AtlasMixin {

//    @WrapOperation(method = {"method_45891"},
//            at = @At(value = "INVOKE", target = "Lcom/google/common/collect/Multimap;put(Ljava/lang/Object;Ljava/lang/Object;)Z"))
//    private static <K, V> boolean supresssWarning(Multimap instance, K k, V v, Operation<Boolean> original) {
//        if (!((Material) v).atlasLocation().getNamespace().startsWith(RuneCraftory.MODID))
//            original.call(instance, k, v);
//        return false;
//    }
}
